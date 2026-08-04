import axios from "axios";
import { getAccessToken, setAccessToken, clearAccessToken } from "../storage/tokenStore";

const api = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  withCredentials: true,
});

api.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Rotas de auth nunca entram no fluxo de refresh automático,
// para evitar loop infinito quando o próprio refresh falha.
const ROTAS_SEM_REFRESH = ["/auth/login", "/auth/refresh", "/auth/logout"];

// Promise partilhada: evita disparar múltiplos /auth/refresh
// em paralelo quando vários pedidos falham com 401 ao mesmo tempo.
let refreshPromise = null;

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const { config, response } = error;

    const isAuthRoute = ROTAS_SEM_REFRESH.some((rota) => config?.url?.includes(rota));

    if (response?.status !== 401 || isAuthRoute || config._isRetry) {
      return Promise.reject(error);
    }

    config._isRetry = true;

    try {
      if (!refreshPromise) {
        // Import tardio para evitar dependência circular no topo do ficheiro.
        const { default: authRepository } = await import("../repositories/authRepository");
        refreshPromise = authRepository.refresh().finally(() => {
          refreshPromise = null;
        });
      }

      const { accessToken } = await refreshPromise;
      setAccessToken(accessToken);

      config.headers.Authorization = `Bearer ${accessToken}`;
      return api(config);
    } catch (refreshError) {
      clearAccessToken();
      return Promise.reject(refreshError);
    }
  }
);

export default api;