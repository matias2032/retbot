// Espelha ConfiguracaoContaResponse (Java).

export function criarConfiguracaoContaModel(data) {
  return {
    idContaSocial: data.idContaSocial,
    intervaloMinSegundos: data.intervaloMinSegundos,
    maxAcoes15Min: data.maxAcoes15Min,
    maxAcoesDia: data.maxAcoesDia,
    ativo: data.ativo,
  };
}