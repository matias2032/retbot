import contaSocialRepository from '../repositories/contaSocialRepository';
import { criarContaSocialModel } from '../models/ContaSocial';
import { criarConfiguracaoContaModel } from '../models/ConfiguracaoConta';

const contaSocialService = {
  listarPorUtilizador: async (idUtilizador) => {
    const data = await contaSocialRepository.listarPorUtilizador(idUtilizador);
    return data.map(criarContaSocialModel);
  },

  adicionar: async (idUtilizador, dadosContaSocial) => {
    const data = await contaSocialRepository.adicionar(idUtilizador, dadosContaSocial);
    return criarContaSocialModel(data);
  },

  remover: async (idContaSocial) => {
    await contaSocialRepository.remover(idContaSocial);
  },

  atualizarConfiguracao: async (idContaSocial, configuracao) => {
    const data = await contaSocialRepository.atualizarConfiguracao(idContaSocial, configuracao);
    return criarConfiguracaoContaModel(data);
  },

  iniciarOAuth: (idUtilizador, plataforma, urlInstancia) => {
    contaSocialRepository.iniciarOAuth(idUtilizador, plataforma, urlInstancia);
  },
};

export default contaSocialService;