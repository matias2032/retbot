import axiosInstance from '../api/axiosInstance';

const utilizadorRepository = {
  criar: async ({ nome, email, senha, idPerfil }) => {
    const response = await axiosInstance.post('/utilizadores', { nome, email, senha, idPerfil });
    return response.data;
  },

  buscar: async (idUtilizador) => {
    const response = await axiosInstance.get(`/utilizadores/${idUtilizador}`);
    return response.data;
  },

  atualizar: async (idUtilizador, { nome, email }) => {
    const response = await axiosInstance.put(`/utilizadores/${idUtilizador}`, { nome, email });
    return response.data;
  },

  alterarSenha: async (idUtilizador, novaSenha) => {
    await axiosInstance.put(`/utilizadores/${idUtilizador}/senha`, novaSenha, {
      headers: { 'Content-Type': 'text/plain' },
    });
  },
};

export default utilizadorRepository;