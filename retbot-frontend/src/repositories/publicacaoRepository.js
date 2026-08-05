import api from "../api/axiosInstance";

const publicacaoRepository = {
  // ---------- Publicação ----------

  criarPublicacao: async (dados) => {
    const response = await api.post('/publicacoes', dados);
    return response.data;
  },

  buscarPorId: async (id) => {
    const response = await api.get(`/publicacoes/${id}`);
    return response.data;
  },

  buscarPorIdExterno: async (idExterno) => {
    const response = await api.get(`/publicacoes/externa/${idExterno}`);
    return response.data;
  },

  listarPorConta: async (idContaSocial) => {
    const response = await api.get(`/publicacoes/conta/${idContaSocial}`);
    return response.data;
  },

  // ---------- Agendamento ----------

  criarAgendamento: async (dados) => {
    const response = await api.post('/agendamentos', dados);
    return response.data;
  },

  buscarAgendamentoPorId: async (id) => {
    const response = await api.get(`/agendamentos/${id}`);
    return response.data;
  },

  listarAgendamentosPorConta: async (idContaSocial) => {
    const response = await api.get(`/agendamentos/conta/${idContaSocial}`);
    return response.data;
  },

  listarAgendamentosPorEstado: async (estado) => {
    const response = await api.get(`/agendamentos/estado/${estado}`);
    return response.data;
  },

  atualizarEstadoAgendamento: async (id, novoEstado) => {
    const response = await api.patch(`/agendamentos/${id}/estado`, { novoEstado });
    return response.data;
  },

  cancelarAgendamento: async (id) => {
    const response = await api.patch(`/agendamentos/${id}/cancelar`);
    return response.data;
  },
};

export default publicacaoRepository;