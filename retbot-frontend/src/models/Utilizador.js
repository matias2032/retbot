// Espelha UtilizadorResponse (Java) — nunca senhaHash, nunca actualizadoEm
// (o Response também não os expõe).

export function criarUtilizadorModel(data) {
  return {
    idUtilizador: data.idUtilizador,
    nome: data.nome,
    email: data.email,
    ativo: data.ativo,
    criadoEm: data.criadoEm,
  };
}