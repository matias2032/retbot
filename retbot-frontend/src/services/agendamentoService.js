import axiosInstance from '../api/axiosInstance';

const agendamentoService = {
  listar: async () => {
    const response = await axiosInstance.get('/agendamentos');
    return response.data;
  },

  buscarPorId: async (id) => {
    const response = await axiosInstance.get(`/agendamentos/${id}`);
    return response.data;
  },

  criar: async (dados) => {
    const response = await axiosInstance.post('/agendamentos', dados);
    return response.data;
  },

  atualizar: async (id, dados) => {
    const response = await axiosInstance.put(`/agendamentos/${id}`, dados);
    return response.data;
  },

  eliminar: async (id) => {
    await axiosInstance.delete(`/agendamentos/${id}`);
  },
};

export default agendamentoService;