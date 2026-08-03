package dev258.retbotbackend.utilizador.dto;

import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdicionarContaSocialRequest(

        @NotNull(message = "Plataforma é obrigatória")
        PlataformaSocial plataforma,

        @NotBlank(message = "Id do utilizador na plataforma é obrigatório")
        String idUtilizadorPlataforma,

        @NotBlank(message = "Username é obrigatório")
        String username,

        String nomeExibicao,

        @NotBlank(message = "Access token é obrigatório")
        String accessToken,

        String refreshToken
) {}