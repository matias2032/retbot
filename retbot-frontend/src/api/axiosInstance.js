import axios from 'axios';
import { getAccessToken, setAccessToken, clearAccessToken } from '../storage/tokenStore';

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosInstance.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

const ROTAS_SEM_REFRESH = ['/auth/login', '/auth/refresh', '/auth/logout'];
let refreshPromise = null;

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const { config, response } = error;
    const isAuthRoute = ROTAS_SEM_REFRESH.some((rota) => config?.url?.includes(rota));

    if (response?.status !== 401 || isAuthRoute || config?._isRetry) {
      return Promise.reject(error);
    }

    config._isRetry = true;

    try {
      if (!refreshPromise) {
        const { default: authRepository } = await import('../repositories/authRepository');
        refreshPromise = authRepository.refresh().finally(() => {
          refreshPromise = null;
        });
      }

      const { accessToken } = await refreshPromise;
      setAccessToken(accessToken);

      config.headers.Authorization = `Bearer ${accessToken}`;
      return axiosInstance(config);
    } catch (refreshError) {
      clearAccessToken();
      window.location.href = '/login';
      return Promise.reject(refreshError);
    }
  }
);

export default axiosInstance;