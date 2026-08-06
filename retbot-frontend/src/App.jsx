import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './routes/ProtectedRoute';
import Login from './pages/Login';
import PrimeiroAcesso from './pages/PrimeiroAcesso';
import Dashboard from './pages/Dashboard';
import Utilizadores from './pages/Utilizadores';
import CriarUtilizador from './pages/CriarUtilizador';
import DetalheUtilizador from './pages/DetalheUtilizador';
import ContasSociais from './pages/ContasSociais';
import Publicacoes from './pages/Publicacoes';
import Agendamentos from './pages/Agendamentos';
import Automacao from './pages/Automacao';
import { useAuth } from './hooks/useAuth';
import './App.css';

function App() {
  const { isAuthenticated, requerTrocaSenha } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={<Login />} />


      <Route
        path="/primeiro-acesso"
        element={
          !isAuthenticated ? (
            <Navigate to="/login" replace />
          ) : !requerTrocaSenha ? (
            <Navigate to="/" replace />
          ) : (
            <PrimeiroAcesso />
          )
        }
      />

      <Route element={<ProtectedRoute />}>
        {/* Rota inicial apontando para o Dashboard */}
<Route path="/" element={<Dashboard />} />
<Route path="/utilizadores" element={<Utilizadores />} />
        <Route path="/utilizadores/novo" element={<CriarUtilizador />} />
        <Route path="/utilizadores/:idUtilizador" element={<DetalheUtilizador />} />
        <Route path="/contas" element={<ContasSociais />} />
        <Route path="/publicacoes" element={<Publicacoes />} />
        <Route path="/agendamentos" element={<Agendamentos />} />
        <Route path="/automacao" element={<Automacao />} />
      </Route>
    </Routes>
  );
}

export default App;