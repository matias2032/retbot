package dev258.retbotbackend.utilizador.service;

import dev258.retbotbackend.utilizador.entity.ConfiguracaoConta;
import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.entity.Perfil;
import dev258.retbotbackend.utilizador.entity.Utilizador;
import dev258.retbotbackend.utilizador.exception.ContaSocialException;
import dev258.retbotbackend.utilizador.exception.UtilizadorNaoEncontradoException;
import dev258.retbotbackend.utilizador.repository.ConfiguracaoContaRepository;
import dev258.retbotbackend.utilizador.repository.ContaSocialRepository;
import dev258.retbotbackend.utilizador.repository.PerfilRepository;
import dev258.retbotbackend.utilizador.repository.UtilizadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilizadorService {

private final UtilizadorRepository utilizadorRepository;
    private final ContaSocialRepository contaSocialRepository;
    private final ConfiguracaoContaRepository configuracaoContaRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

private static final SecureRandom SECURE_RANDOM = new SecureRandom();

// Senha atribuída quando o admin não informa uma senha manualmente
    private static final String SENHA_PADRAO = "12345678";

    // idPerfil atribuído quando o admin não escolhe um perfil (2 = operador)
    private static final Long ID_PERFIL_OPERADOR_PADRAO = 2L;

    // idPerfil que nunca deve aparecer na listagem de utilizadores (1 = administrador)
    private static final Long ID_PERFIL_ADMIN = 1L;

    // ---------- Utilizador ----------

    @Transactional(readOnly = true)
    public List<Utilizador> listarUtilizadores() {
        return utilizadorRepository.findAllExcluindoPerfil(ID_PERFIL_ADMIN);
    }

public Utilizador criarUtilizador(String nome, String email, String senhaPlano, Long idPerfil) {
        if (utilizadorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe um utilizador com este email: " + email);
        }

        Long idPerfilEfetivo = idPerfil != null ? idPerfil : ID_PERFIL_OPERADOR_PADRAO;
        Perfil perfil = perfilRepository.findById(idPerfilEfetivo)
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado com id: " + idPerfilEfetivo));

        String senhaEfetiva = (senhaPlano == null || senhaPlano.isBlank()) ? SENHA_PADRAO : senhaPlano;

        Utilizador utilizador = Utilizador.builder()
                .nome(nome)
                .email(email)
                .senhaHash(passwordEncoder.encode(senhaEfetiva))
                .ativo(true)
                .requerTrocaSenha(true)
                .perfil(perfil)
                .build();

        return utilizadorRepository.save(utilizador);
    }

    public void alterarSenha(Long idUtilizador, String novaSenha) {
        Utilizador utilizador = buscarUtilizador(idUtilizador);
        utilizador.setSenhaHash(passwordEncoder.encode(novaSenha));
        utilizador.setRequerTrocaSenha(false);
        utilizadorRepository.save(utilizador);
    }

public Utilizador actualizarUtilizador(Long idUtilizador, String nome, String email) {
        Utilizador utilizador = buscarUtilizador(idUtilizador);
        utilizador.setNome(nome);
        utilizador.setEmail(email);
        return utilizadorRepository.save(utilizador);
    }

    // Alterna o estado ativo/inactive do utilizador (usado pelo botão toggle na listagem)
    public Utilizador alternarEstadoAtivo(Long idUtilizador) {
        Utilizador utilizador = buscarUtilizador(idUtilizador);
        utilizador.setAtivo(!utilizador.getAtivo());
        return utilizadorRepository.save(utilizador);
    }

    @Transactional(readOnly = true)
    public Utilizador buscarUtilizador(Long idUtilizador) {
        return utilizadorRepository.findById(idUtilizador)
                .orElseThrow(() -> new UtilizadorNaoEncontradoException(idUtilizador));
    }

    // ---------- Conta Social ----------


    @Transactional(readOnly = true)
public List<ContaSocial> listarContasSociais(Long idUtilizador) {
    buscarUtilizador(idUtilizador); // garante 404 se o utilizador não existir
    return contaSocialRepository.findByUtilizador_IdUtilizador(idUtilizador);
}

    public ContaSocial adicionarContaSocial(Long idUtilizador, ContaSocial contaSocial) {
        Utilizador utilizador = buscarUtilizador(idUtilizador);

        boolean jaLigada = contaSocialRepository.existsByUtilizador_IdUtilizadorAndPlataforma(
                idUtilizador, contaSocial.getPlataforma());

        if (jaLigada) {
            throw new ContaSocialException(
                    "Este utilizador já tem uma conta ligada na plataforma " + contaSocial.getPlataforma());
        }

        contaSocial.setUtilizador(utilizador);
        return contaSocialRepository.save(contaSocial);
    }

    public void removerContaSocial(Long idContaSocial) {
        ContaSocial contaSocial = contaSocialRepository.findById(idContaSocial)
                .orElseThrow(() -> new ContaSocialException("Conta social não encontrada com id: " + idContaSocial));

        contaSocialRepository.delete(contaSocial);
    }

    // ---------- Configuração da Conta ----------

    public ConfiguracaoConta actualizarConfiguracaoConta(Long idContaSocial, ConfiguracaoConta novaConfiguracao) {
        ContaSocial contaSocial = contaSocialRepository.findById(idContaSocial)
                .orElseThrow(() -> new ContaSocialException("Conta social não encontrada com id: " + idContaSocial));

        ConfiguracaoConta configuracao = configuracaoContaRepository.findById(idContaSocial)
                .orElseGet(() -> ConfiguracaoConta.builder()
                        .contaSocial(contaSocial)
                        .build());

        configuracao.setIntervaloMinSegundos(novaConfiguracao.getIntervaloMinSegundos());
        configuracao.setMaxAcoes15Min(novaConfiguracao.getMaxAcoes15Min());
        configuracao.setMaxAcoesDia(novaConfiguracao.getMaxAcoesDia());
        configuracao.setAtivo(novaConfiguracao.getAtivo());

        return configuracaoContaRepository.save(configuracao);
    }

    // ---------- OAuth / Tokens ----------

    /**
     * Gera um refresh token opaco e seguro (não é um JWT — é um token de posse
     * guardado em conta_social.refresh_token, usado para renovar o access_token
     * junto da plataforma externa).
     */
    public String gerarRefreshToken() {
        byte[] bytes = new byte[64];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Valida se o access_token de uma conta social ainda está dentro da validade.
     */
    @Transactional(readOnly = true)
    public boolean validarOAuth(Long idContaSocial) {
        ContaSocial contaSocial = contaSocialRepository.findById(idContaSocial)
                .orElseThrow(() -> new ContaSocialException("Conta social não encontrada com id: " + idContaSocial));

        OffsetDateTime expiracao = contaSocial.getTokenExpiraEm();
        return expiracao != null && expiracao.isAfter(OffsetDateTime.now());
    }
}