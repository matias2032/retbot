// Espelham os enums Java (utilizador.enums.PlataformaSocial / EstadoConta).
// Mantidos sincronizados manualmente — se o backend adicionar uma plataforma,
// atualizar aqui também.

export const PlataformaSocial = Object.freeze({
  X: 'X',
  BLUESKY: 'BLUESKY',
  MASTODON: 'MASTODON',
  LINKEDIN: 'LINKEDIN',
  THREADS: 'THREADS',
});

export const EstadoConta = Object.freeze({
  ATIVA: 'ATIVA',
  PAUSADA: 'PAUSADA',
  REVOGADA: 'REVOGADA',
  ERRO_AUTENTICACAO: 'ERRO_AUTENTICACAO',
});