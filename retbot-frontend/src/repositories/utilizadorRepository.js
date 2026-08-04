import axiosInstance from '../api/axiosInstance';

const utilizadorRepository = {
  criar: async ({ nome, email, senha }) => {
    const response = await axiosInstance.post('/utilizadores', { nome, email, senha });
    return response.data; // UtilizadorResponse
  },

  buscar: async (idUtilizador) => {
    const response = await axiosInstance.get(`/utilizadores/${idUtilizador}`);
    return response.data;
  },

  atualizar: async (idUtilizador, { nome, email }) => {
    const response = await axiosInstance.put(`/utilizadores/${idUtilizador}`, { nome, email });
    return response.data;
  },
};

export default utilizadorRepository;