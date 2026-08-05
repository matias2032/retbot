// src/pages/ContasSociais.jsx
import { useEffect, useState } from 'react';
import { useAuth } from '../hooks/useAuth';
import contaSocialService from '../services/contaSocialService';
import { PlataformaSocial } from '../models/enums';

function ContasSociais() {
  const { utilizador } = useAuth();
  const [contas, setContas] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);

  useEffect(() => {
    if (!utilizador) return;

    contaSocialService
      .listarPorUtilizador(utilizador.idUtilizador)
      .then(setContas)
      .catch(() => setErro('Não foi possível carregar as contas sociais.'))
      .finally(() => setCarregando(false));
  }, [utilizador]);

  const ligarNovaConta = (plataforma) => {
    // TODO: urlInstancia só é relevante para Mastodon; para as outras
    // plataformas passamos string vazia até termos um seletor de plataforma na UI.
    contaSocialService.iniciarOAuth(utilizador.idUtilizador, plataforma, '');
  };

  if (carregando) return <p>A carregar contas sociais...</p>;
  if (erro) return <p role="alert">{erro}</p>;

  return (
    <div>
      <h1>Contas Sociais</h1>

      {contas.length === 0 ? (
        <p>Ainda não tens contas sociais ligadas.</p>
      ) : (
        <ul>
          {contas.map((conta) => (
            <li key={conta.idContaSocial}>
              <strong>{conta.plataforma}</strong> — {conta.nomeExibicao ?? conta.username}
              {' '}
              <span>({conta.estado})</span>
            </li>
          ))}
        </ul>
      )}

      <button onClick={() => ligarNovaConta(PlataformaSocial.X)}>
        Ligar nova conta
      </button>
    </div>
  );
}

export default ContasSociais;