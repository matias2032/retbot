export function criarUtilizadorModel(data) {
  return {
    idUtilizador: data.idUtilizador,
    nome: data.nome,
    email: data.email,
    ativo: data.ativo,
    requerTrocaSenha: data.requerTrocaSenha ?? false,
    perfil: data.perfil ?? null,
    criadoEm: data.criadoEm,
  };
}