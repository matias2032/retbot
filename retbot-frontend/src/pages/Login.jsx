import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState(null);
  const [aEnviar, setAEnviar] = useState(false);

  const destino = location.state?.from?.pathname ?? '/';

  const submeter = async (evento) => {
    evento.preventDefault();
    setErro(null);
    setAEnviar(true);

    try {
      await login({ email, senha });
      navigate(destino, { replace: true });
    } catch {
      // authRepository não distingue 401 de erro de rede — ambos chegam aqui.
      setErro('Email ou senha inválidos.');
    } finally {
      setAEnviar(false);
    }
  };

  return (
    <div>
      <h1>Entrar</h1>

      <form onSubmit={submeter}>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          autoComplete="email"
        />

        <label htmlFor="senha">Senha</label>
        <input
          id="senha"
          type="password"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          required
          autoComplete="current-password"
        />

        {erro && <p role="alert">{erro}</p>}

        <button type="submit" disabled={aEnviar}>
          {aEnviar ? 'A entrar...' : 'Entrar'}
        </button>
      </form>
    </div>
  );
}

export default Login;