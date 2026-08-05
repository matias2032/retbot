import publicacaoRepository from '../repositories/publicacaoRepository';
import Publicacao from '../models/Publicacao';
import Agendamento from '../models/Agendamento';

const publicacaoService = {
  // ---------- Publicação ----------

  criarPublicacao: async (dados) => {
    const response = await publicacaoRepository.criarPublicacao(dados);
    return new Publicacao(response);
  },

  buscarPorId: async (id) => {
    const response = await publicacaoRepository.buscarPorId(id);
    return new Publicacao(response);
  },

  buscarPorIdExterno: async (idExterno) => {
    const response = await publicacaoRepository.buscarPorIdExterno(idExterno);
    return new Publicacao(response);
  },

  listarPorConta: async (idContaSocial) => {
    const list = await publicacaoRepository.listarPorConta(idContaSocial);
    return list.map((item) => new Publicacao(item));
  },

  // ---------- Agendamento ----------

  criarAgendamento: async (dados) => {
    const response = await publicacaoRepository.criarAgendamento(dados);
    return new Agendamento(response);
  },

  buscarAgendamentoPorId: async (id) => {
    const response = await publicacaoRepository.buscarAgendamentoPorId(id);
    return new Agendamento(response);
  },

  listarAgendamentosPorConta: async (idContaSocial) => {
    const list = await publicacaoRepository.listarAgendamentosPorConta(idContaSocial);
    return list.map((item) => new Agendamento(item));
  },

  listarAgendamentosPorEstado: async (estado) => {
    const list = await publicacaoRepository.listarAgendamentosPorEstado(estado);
    return list.map((item) => new Agendamento(item));
  },

  atualizarEstadoAgendamento: async (id, novoEstado) => {
    const response = await publicacaoRepository.atualizarEstadoAgendamento(id, novoEstado);
    return new Agendamento(response);
  },

  cancelarAgendamento: async (id) => {
    const response = await publicacaoRepository.cancelarAgendamento(id);
    return new Agendamento(response);
  },
};

export default publicacaoService;