-- V1__criar_schema_inicial.sql
-- Schema inicial do RetbotBackend.
-- Módulos cobertos: utilizador, conta_social, configuracao_conta,
--                    publicacao, agendamento, execucao, rate_limit.
--
-- DECISÃO DE DESENHO (importante, não mudar sem avisar):
-- Os "enums" de domínio (PlataformaSocial, EstadoConta, TipoAcao,
-- EstadoAgendamento) são mapeados aqui como VARCHAR + CHECK constraint,
-- e NÃO como tipo ENUM nativo do Postgres.
-- Motivo: as entities JPA usam apenas @Enumerated(EnumType.STRING), sem
-- @JdbcTypeCode(SqlTypes.NAMED_ENUM). Um ENUM nativo do Postgres sem essa
-- anotação extra no Hibernate 6+/7 causa erro em runtime no primeiro INSERT:
-- "column X is of type Y but expression is of type character varying".
-- VARCHAR + CHECK dá a mesma garantia de valores válidos sem essa
-- fragilidade e sem exigir alterações às entities já testadas.

-- ============================================================
-- Utilizador
-- ============================================================
CREATE TABLE utilizador (
    id_utilizador BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    actualizado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- Conta Social
-- ============================================================
CREATE TABLE conta_social (
    id_conta_social BIGSERIAL PRIMARY KEY,
    id_utilizador BIGINT NOT NULL,

    plataforma VARCHAR(20) NOT NULL
        CHECK (plataforma IN ('X', 'BLUESKY', 'MASTODON', 'LINKEDIN', 'THREADS')),

    id_utilizador_plataforma VARCHAR(80) NOT NULL,
    username VARCHAR(50) NOT NULL,
    nome_exibicao VARCHAR(150),
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    token_expira_em TIMESTAMPTZ,

    estado VARCHAR(20) NOT NULL DEFAULT 'ATIVA'
        CHECK (estado IN ('ATIVA', 'PAUSADA', 'REVOGADA', 'ERRO_AUTENTICACAO')),

    ultimo_sync TIMESTAMPTZ,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_conta_utilizador
        FOREIGN KEY (id_utilizador)
        REFERENCES utilizador (id_utilizador),

    CONSTRAINT uk_plataforma_user
        UNIQUE (plataforma, id_utilizador_plataforma)
);

-- ============================================================
-- Configuração da Conta
-- ============================================================
CREATE TABLE configuracao_conta (
    id_conta_social BIGINT PRIMARY KEY,
    intervalo_min_segundos INTEGER NOT NULL DEFAULT 120,
    max_acoes_15_min INTEGER NOT NULL DEFAULT 50,
    max_acoes_dia INTEGER,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_intervalo CHECK (intervalo_min_segundos > 0),
    CONSTRAINT chk_max_15 CHECK (max_acoes_15_min >= 0),
    CONSTRAINT chk_max_dia CHECK (max_acoes_dia IS NULL OR max_acoes_dia >= 0),

    FOREIGN KEY (id_conta_social)
        REFERENCES conta_social (id_conta_social)
        ON DELETE CASCADE
);

-- ============================================================
-- Publicação
-- ============================================================
CREATE TABLE publicacao (
    id_publicacao BIGSERIAL PRIMARY KEY,
    id_conta_social BIGINT NOT NULL,
    id_publicacao_externa VARCHAR(80) NOT NULL,
    texto TEXT,
    publicado_em TIMESTAMPTZ,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    FOREIGN KEY (id_conta_social)
        REFERENCES conta_social (id_conta_social),

    UNIQUE (id_publicacao_externa)
);

-- ============================================================
-- Agendamento
-- ============================================================
CREATE TABLE agendamento (
    id_agendamento BIGSERIAL PRIMARY KEY,
    id_conta_social BIGINT NOT NULL,

    tipo VARCHAR(20) NOT NULL
        CHECK (tipo IN ('PUBLICAR', 'REPOSTAR', 'CURTIR', 'RESPONDER', 'SEGUIR', 'DEIXAR_DE_SEGUIR')),

    id_publicacao BIGINT,
    executar_em TIMESTAMPTZ NOT NULL,
    prioridade SMALLINT NOT NULL DEFAULT 0,

    estado VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
        CHECK (estado IN ('PENDENTE', 'EM_EXECUCAO', 'CONCLUIDO', 'FALHOU', 'CANCELADO')),

    tentativas SMALLINT NOT NULL DEFAULT 0,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_prioridade CHECK (prioridade >= 0),
    CONSTRAINT chk_tentativas CHECK (tentativas >= 0),

    FOREIGN KEY (id_conta_social)
        REFERENCES conta_social (id_conta_social),

    FOREIGN KEY (id_publicacao)
        REFERENCES publicacao (id_publicacao)
);

-- ============================================================
-- Execução
-- ============================================================
CREATE TABLE execucao (
    id_execucao BIGSERIAL PRIMARY KEY,
    id_agendamento BIGINT NOT NULL,
    iniciado_em TIMESTAMPTZ NOT NULL,
    terminado_em TIMESTAMPTZ,
    sucesso BOOLEAN NOT NULL,
    codigo_http INTEGER,
    mensagem TEXT,
    request_id VARCHAR(200),

    FOREIGN KEY (id_agendamento)
        REFERENCES agendamento (id_agendamento)
        ON DELETE CASCADE
);

-- ============================================================
-- Rate Limit (chave composta: id_conta_social + endpoint)
-- ============================================================
CREATE TABLE rate_limit (
    id_conta_social BIGINT NOT NULL,
    endpoint VARCHAR(150) NOT NULL,
    limite INTEGER NOT NULL,
    restante INTEGER NOT NULL,
    reinicia_em TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (id_conta_social, endpoint),

    FOREIGN KEY (id_conta_social)
        REFERENCES conta_social (id_conta_social)
        ON DELETE CASCADE
);

-- ============================================================
-- Índices
-- ============================================================
CREATE INDEX idx_agendamento_execucao ON agendamento (estado, executar_em);
CREATE INDEX idx_execucao_agendamento ON execucao (id_agendamento);
CREATE INDEX idx_publicacao_conta ON publicacao (id_conta_social);
CREATE INDEX idx_conta_estado ON conta_social (estado);
CREATE INDEX idx_publicacao_externa ON publicacao (id_publicacao_externa);
CREATE INDEX idx_conta_utilizador ON conta_social (id_utilizador);
CREATE INDEX idx_agendamento_conta ON agendamento (id_conta_social);