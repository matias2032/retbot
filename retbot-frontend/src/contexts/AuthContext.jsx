import { useEffect, useState } from 'react';
import authRepository from '../repositories/authRepository';
import { setAccessToken, clearAccessToken } from '../storage/tokenStore';
import AuthContext from './authContextInstance';

export function AuthProvider({ children }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [utilizador, setUtilizador] = useState(null);

  useEffect(() => {
    const restaurarSessao = async () => {
      try {
        const { accessToken } = await authRepository.refresh();
        setAccessToken(accessToken);
        const dadosUtilizador = await authRepository.me();
        setUtilizador(dadosUtilizador);
        setIsAuthenticated(true);
      } catch {
        clearAccessToken();
        setUtilizador(null);
        setIsAuthenticated(false);
      } finally {
        setIsLoading(false);
      }
    };

    restaurarSessao();
  }, []);

  const login = async ({ email, senha }) => {
    const { accessToken } = await authRepository.login({ email, senha });
    setAccessToken(accessToken);
    const dadosUtilizador = await authRepository.me();
    setUtilizador(dadosUtilizador);
    setIsAuthenticated(true);
    return dadosUtilizador;
  };

  const atualizarEstadoUtilizador = (novosDados) => {
    setUtilizador((prev) => ({ ...prev, ...novosDados }));
  };

  const logout = async () => {
    try {
      await authRepository.logout();
    } finally {
      clearAccessToken();
      setUtilizador(null);
      setIsAuthenticated(false);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        isLoading,
        utilizador,
        requerTrocaSenha: utilizador?.requerTrocaSenha ?? false,
        login,
        logout,
        atualizarEstadoUtilizador,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}