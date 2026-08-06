import utilizadorRepository from '../repositories/utilizadorRepository';
import { criarUtilizadorModel } from '../models/Utilizador';

const utilizadorService = {
  criar: async ({ nome, email, senha, idPerfil }) => {
    const data = await utilizadorRepository.criar({ nome, email, senha, idPerfil });
    return criarUtilizadorModel(data);
  },

  listar: async () => {
    const dados = await utilizadorRepository.listar();
    return dados.map(criarUtilizadorModel);
  },

  buscar: async (idUtilizador) => {
    const data = await utilizadorRepository.buscar(idUtilizador);
    return criarUtilizadorModel(data);
  },

  atualizar: async (idUtilizador, { nome, email }) => {
    const data = await utilizadorRepository.atualizar(idUtilizador, { nome, email });
    return criarUtilizadorModel(data);
  },

alterarSenha: async (idUtilizador, novaSenha) => {
    await utilizadorRepository.alterarSenha(idUtilizador, novaSenha);
  },

  alternarEstado: async (idUtilizador) => {
    const data = await utilizadorRepository.alternarEstado(idUtilizador);
    return criarUtilizadorModel(data);
  },
};

export default utilizadorService;