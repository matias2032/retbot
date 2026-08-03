package dev258.retbotbackend.automacao.entity;

import dev258.retbotbackend.utilizador.entity.ContaSocial;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "rate_limit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateLimit {

    @EmbeddedId
    private RateLimitId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idContaSocial")
    @JoinColumn(name = "id_conta_social")
    private ContaSocial contaSocial;

    private Integer limite;

    private Integer restante;

    private OffsetDateTime reiniciaEm;
}