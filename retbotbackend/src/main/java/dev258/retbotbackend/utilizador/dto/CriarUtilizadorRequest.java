package dev258.retbotbackend.utilizador.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarUtilizadorRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150)
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 200)
        String email,

        // Agora opcional: se vier em branco/omitida, o service atribui a senha padrão
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String senha,

        Long idPerfil
) {}