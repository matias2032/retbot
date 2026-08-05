import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuth from '../hooks/useAuth';
import useUtilizador from '../hooks/useUtilizador';

export default function PrimeiroAcesso() {
  const { utilizador, atualizarEstadoUtilizador, logout } = useAuth();
  const { alterarSenha, carregando } = useUtilizador();
  const navigate = useNavigate();

  const [novaSenha, setNovaSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [erro, setErro] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro('');

    if (novaSenha.length < 8) {
      setErro('A senha deve ter no mínimo 8 caracteres.');
      return;
    }

    if (novaSenha !== confirmarSenha) {
      setErro('As senhas não coincidem.');
      return;
    }

    try {
      await alterarSenha(utilizador.idUtilizador, novaSenha);
      atualizarEstadoUtilizador({ requerTrocaSenha: false });
      navigate('/', { replace: true });
    } catch {
      setErro('Erro ao atualizar a senha. Tente novamente.');
    }
  };

  return (
    <div className="primeiro-acesso-container">
      <div className="primeiro-acesso-card">
        <h2>Primeiro Acesso</h2>
        <p>
          Olá, <strong>{utilizador?.nome}</strong>. Por motivos de segurança, precisa de redefinir a sua senha antes de continuar.
        </p>

        {erro && <div className="alerta-erro">{erro}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="novaSenha">Nova Senha</label>
            <input
              id="novaSenha"
              type="password"
              value={novaSenha}
              onChange={(e) => setNovaSenha(e.target.value)}
              placeholder="Mínimo de 8 caracteres"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmarSenha">Confirmar Nova Senha</label>
            <input
              id="confirmarSenha"
              type="password"
              value={confirmarSenha}
              onChange={(e) => setConfirmarSenha(e.target.value)}
              placeholder="Repita a nova senha"
              required
            />
          </div>

          <div className="acoes">
            <button type="submit" disabled={carregando} className="btn-primary">
              {carregando ? 'A guardar...' : 'Atualizar Senha e Entrar'}
            </button>
            <button type="button" onClick={logout} disabled={carregando} className="btn-secondary">
              Sair / Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}