import { useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { useContasSociais } from '../hooks/useContasSociais';
import { PlataformaSocial } from '../models/enums';
import { useLogger } from '../hooks/useLogger';

function ContasSociais() {
  const { utilizador } = useAuth();
  const { logAction } = useLogger();

  const {
    contas,
    carregando,
    erro,
    carregarContas,
    removerConta,
    iniciarOAuth
  } = useContasSociais(utilizador?.idUtilizador);

  useEffect(() => {
    logAction('CARREGAR_CONTAS_SOCIAIS', { idUtilizador: utilizador?.idUtilizador });
    carregarContas();
  }, [carregarContas]);

  const handleRemoverConta = (idContaSocial, plataforma) => {
    logAction('REMOVER_CONTA_SOCIAL', { 
      idContaSocial, 
      plataforma,
      idUtilizador: utilizador?.idUtilizador 
    });
    removerConta(idContaSocial);
  };

  const handleIniciarOAuth = (plataforma) => {
    logAction('INICIAR_OAUTH_CONTA_SOCIAL', { 
      plataforma,
      idUtilizador: utilizador?.idUtilizador 
    });
    iniciarOAuth(plataforma);
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
              {' '}
              <button onClick={() => handleRemoverConta(conta.idContaSocial, conta.plataforma)}>
                Remover
              </button>
            </li>
          ))}
        </ul>
      )}

      <button onClick={() => handleIniciarOAuth(PlataformaSocial.X)}>
        Ligar nova conta
      </button>
    </div>
  );
}

export default ContasSociais;