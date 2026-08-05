import axiosInstance from '../api/axiosInstance';

const contaSocialRepository = {
  // NOTA: este endpoint aceita accessToken/refreshToken diretamente no corpo —
  // é o caminho manual (usado, por exemplo, em testes). O caminho normal de
  // ligação de conta social é iniciarOAuth() + callback tratado pelo backend.
  adicionar: async (idUtilizador, dadosContaSocial) => {
    const response = await axiosInstance.post(
      `/utilizadores/${idUtilizador}/contas`,
      dadosContaSocial
    );
    return response.data; // ContaSocialResponse
  },

  remover: async (idContaSocial) => {
    await axiosInstance.delete(`/utilizadores/contas/${idContaSocial}`);
  },

  atualizarConfiguracao: async (idContaSocial, configuracao) => {
    const response = await axiosInstance.put(
      `/utilizadores/contas/${idContaSocial}/configuracao`,
      configuracao
    );
    return response.data; // ConfiguracaoContaResponse
  },

  listarPorUtilizador: async (idUtilizador) => {
  const response = await axiosInstance.get(`/utilizadores/${idUtilizador}/contas`);
  return response.data; // ContaSocialResponse[]
},

  /**
   * Navegação de página inteira, não uma chamada axios: o backend responde
   * com 302 a redirecionar para o provedor OAuth externo, e o utilizador
   * precisa de autenticar-se lá — um XHR não pode fazer isso.
   */
  iniciarOAuth: (idUtilizador, plataforma, urlInstancia) => {
    const baseURL = axiosInstance.defaults.baseURL;
    const params = new URLSearchParams({ urlInstancia });
    const plataformaPath = plataforma.toLowerCase();

    window.location.href =
      `${baseURL}/utilizadores/${idUtilizador}/contas-sociais/oauth/${plataformaPath}/iniciar?${params}`;
  },
};

export default contaSocialRepository;