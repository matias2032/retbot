import axiosInstance from '../api/axiosInstance';

const authRepository = {
  /**
   * Autentica o utilizador. O backend define o cookie httpOnly
   * `refresh_token` na resposta; o access_token vem no corpo.
   */
login: async ({ email, senha }) => {
    const response = await axiosInstance.post('/auth/login', {
      email,
      senha,
    });
    return response.data; // esperado: LoginResponse { accessToken, tipo, expiraEmSegundos }
  },

  /**
   * Pede um novo access_token usando o refresh_token do cookie httpOnly.
   * Não precisa de corpo — o cookie viaja automaticamente (withCredentials).
   */
  refresh: async () => {
    const response = await axiosInstance.post('/auth/refresh');
    return response.data; // esperado: { accessToken, ... }
  },

  /**
   * Devolve os dados do utilizador autenticado, a partir do access_token
   * enviado no header Authorization (já tratado pelo axiosInstance).
   */
  me: async () => {
    const response = await axiosInstance.get('/auth/me');
    return response.data; // esperado: UtilizadorResponse { idUtilizador, nome, email, ... }
  },

  /**
   * Invalida a sessão no backend e limpa o cookie refresh_token.
   * A limpeza do access_token em memória (tokenStore) é responsabilidade
   * de quem chamar isto (AuthContext), não do repository.
   */
  logout: async () => {
    await axiosInstance.post('/auth/logout');
  },
};

export default authRepository;