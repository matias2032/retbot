// Espelham os enums Java.
// Mantidos sincronizados manualmente.

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

export const EstadoAgendamento = Object.freeze({
  PENDENTE: 'PENDENTE',
  EM_EXECUCAO: 'EM_EXECUCAO',
  CONCLUIDO: 'CONCLUIDO',
  FALHOU: 'FALHOU',
  CANCELADO: 'CANCELADO'
});

export const TipoAcao = Object.freeze({
  PUBLICAR: 'PUBLICAR',
  REPOSTAR: 'REPOSTAR',
  CURTIR: 'CURTIR',
  RESPONDER: 'RESPONDER',
  SEGUIR: 'SEGUIR',
  DEIXAR_DE_SEGUIR: 'DEIXAR_DE_SEGUIR'
});