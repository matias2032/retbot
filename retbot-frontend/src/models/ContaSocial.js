// Espelha ContaSocialResponse (Java) — sem accessToken/refreshToken/
// idUtilizadorPlataforma, porque o Response do backend também os omite.

export function criarContaSocialModel(data) {
  return {
    idContaSocial: data.idContaSocial,
    plataforma: data.plataforma,
    username: data.username,
    nomeExibicao: data.nomeExibicao,
    estado: data.estado,
    ultimoSync: data.ultimoSync,
    criadoEm: data.criadoEm,
    urlInstancia: data.urlInstancia, // só relevante para Mastodon
  };
}