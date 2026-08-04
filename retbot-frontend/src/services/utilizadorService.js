import utilizadorRepository from '../repositories/utilizadorRepository';
import { criarUtilizadorModel } from '../models/Utilizador';

const utilizadorService = {
  criar: async ({ nome, email, senha }) => {
    const data = await utilizadorRepository.criar({ nome, email, senha });
    return criarUtilizadorModel(data);
  },

  buscar: async (idUtilizador) => {
    const data = await utilizadorRepository.buscar(idUtilizador);
    return criarUtilizadorModel(data);
  },

  atualizar: async (idUtilizador, { nome, email }) => {
    const data = await utilizadorRepository.atualizar(idUtilizador, { nome, email });
    return criarUtilizadorModel(data);
  },
};

export default utilizadorService;