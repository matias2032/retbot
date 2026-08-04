package dev258.retbotbackend.integration.oauth;

import dev258.retbotbackend.utilizador.enums.PlataformaSocial;

public record EstadoAutorizacaoOAuth(Long idUtilizador, PlataformaSocial plataforma, String urlInstancia) {}