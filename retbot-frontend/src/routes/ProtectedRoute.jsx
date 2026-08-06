import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export default function ProtectedRoute() {
  const { isAuthenticated, isLoading, requerTrocaSenha } = useAuth();

  if (isLoading) {
    return <div>A carregar...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Redireciona obrigatoriamente para /primeiro-acesso se for o primeiro login
  if (requerTrocaSenha) {
    return <Navigate to="/primeiro-acesso" replace />;
  }

  return <Outlet />;
}