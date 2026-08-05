package dev258.retbotbackend.utilizador.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long idPerfil;

    @Column(name = "nome", length = 50, nullable = false, unique = true)
    private String nome; // Ex: "ROLE_ADMIN", "ROLE_OPERADOR"
}