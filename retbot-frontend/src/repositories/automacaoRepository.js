import api from "../api/axiosInstance";

const automacaoRepository = {
  // ---------- Execuções ----------

  iniciarExecucao: async (dados) => {
    const response = await api.post('/execucoes', dados);
    return response.data;
  },

  finalizarExecucao: async (id, dados) => {
    const response = await api.patch(`/execucoes/${id}/finalizar`, dados);
    return response.data;
  },

  listarExecucoesPorAgendamento: async (idAgendamento) => {
    const response = await api.get(`/execucoes/agendamento/${idAgendamento}`);
    return response.data;
  },

  listarExecucoesFalhadas: async () => {
    const response = await api.get('/execucoes/falhadas');
    return response.data;
  },

  // ---------- Rate Limits ----------

  obterOuCriarRateLimit: async (dados) => {
    const response = await api.post('/rate-limits', dados);
    return response.data;
  },

  consumirRateLimit: async (dados) => {
    const response = await api.post('/rate-limits/consumir', dados);
    return response.data;
  },

  listarRateLimitsPorConta: async (idContaSocial) => {
    const response = await api.get(`/rate-limits/conta/${idContaSocial}`);
    return response.data;
  },
};

export default automacaoRepository;