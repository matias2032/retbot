import { useEffect, useState } from 'react';
import authRepository from '../repositories/authRepository';
import { setAccessToken, clearAccessToken } from '../storage/tokenStore';
import AuthContext from './authContextInstance';

export function AuthProvider({ children }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const restaurarSessao = async () => {
      try {
        const { accessToken } = await authRepository.refresh();
        setAccessToken(accessToken);
        setIsAuthenticated(true);
      } catch {
        clearAccessToken();
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
    setIsAuthenticated(true);
  };

  const logout = async () => {
    try {
      await authRepository.logout();
    } finally {
      clearAccessToken();
      setIsAuthenticated(false);
    }
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, isLoading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}