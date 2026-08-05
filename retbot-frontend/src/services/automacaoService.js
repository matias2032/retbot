import automacaoRepository from '../repositories/automacaoRepository';
import Execucao from '../models/Execucao';
import RateLimit from '../models/RateLimit';

const automacaoService = {
  // ---------- Execuções ----------

  iniciarExecucao: async (dados) => {
    const response = await automacaoRepository.iniciarExecucao(dados);
    return new Execucao(response);
  },

  finalizarExecucao: async (id, dados) => {
    const response = await automacaoRepository.finalizarExecucao(id, dados);
    return new Execucao(response);
  },

  listarExecucoesPorAgendamento: async (idAgendamento) => {
    const list = await automacaoRepository.listarExecucoesPorAgendamento(idAgendamento);
    return list.map((item) => new Execucao(item));
  },

  listarExecucoesFalhadas: async () => {
    const list = await automacaoRepository.listarExecucoesFalhadas();
    return list.map((item) => new Execucao(item));
  },

  // ---------- Rate Limits ----------

  obterOuCriarRateLimit: async (dados) => {
    const response = await automacaoRepository.obterOuCriarRateLimit(dados);
    return new RateLimit(response);
  },

  consumirRateLimit: async (dados) => {
    const response = await automacaoRepository.consumirRateLimit(dados);
    return new RateLimit(response);
  },

  listarRateLimitsPorConta: async (idContaSocial) => {
    const list = await automacaoRepository.listarRateLimitsPorConta(idContaSocial);
    return list.map((item) => new RateLimit(item));
  },
};

export default automacaoService;