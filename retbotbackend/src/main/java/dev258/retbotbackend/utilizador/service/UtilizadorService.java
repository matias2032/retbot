package dev258.retbotbackend.utilizador.service;

import dev258.retbotbackend.utilizador.entity.ConfiguracaoConta;
import dev258.retbotbackend.utilizador.entity.ContaSocial;
import dev258.retbotbackend.utilizador.entity.Utilizador;
import dev258.retbotbackend.utilizador.exception.ContaSocialException;
import dev258.retbotbackend.utilizador.exception.UtilizadorNaoEncontradoException;
import dev258.retbotbackend.utilizador.repository.ConfiguracaoContaRepository;
import dev258.retbotbackend.utilizador.repository.ContaSocialRepository;
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
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // ---------- Utilizador ----------

    public Utilizador criarUtilizador(String nome, String email, String senhaPlano) {
        if (utilizadorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe um utilizador com este email: " + email);
        }

        Utilizador utilizador = Utilizador.builder()
                .nome(nome)
                .email(email)
                .senhaHash(passwordEncoder.encode(senhaPlano))
                .ativo(true)
                .build();

        return utilizadorRepository.save(utilizador);
    }

    public Utilizador actualizarUtilizador(Long idUtilizador, String nome, String email) {
        Utilizador utilizador = buscarUtilizador(idUtilizador);
        utilizador.setNome(nome);
        utilizador.setEmail(email);
        return utilizadorRepository.save(utilizador);
    }

    @Transactional(readOnly = true)
    public Utilizador buscarUtilizador(Long idUtilizador) {
        return utilizadorRepository.findById(idUtilizador)
                .orElseThrow(() -> new UtilizadorNaoEncontradoException(idUtilizador));
    }

    // ---------- Conta Social ----------

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