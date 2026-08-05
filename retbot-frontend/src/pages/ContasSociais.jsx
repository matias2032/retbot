import { useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { useContasSociais } from '../hooks/useContasSociais';
import { PlataformaSocial } from '../models/enums';

function ContasSociais() {
  const { utilizador } = useAuth();
  const {
    contas,
    carregando,
    erro,
    carregarContas,
    removerConta,
    iniciarOAuth
  } = useContasSociais(utilizador?.idUtilizador);

  useEffect(() => {
    carregarContas();
  }, [carregarContas]);

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
              <button onClick={() => removerConta(conta.idContaSocial)}>
                Remover
              </button>
            </li>
          ))}
        </ul>
      )}

      <button onClick={() => iniciarOAuth(PlataformaSocial.X)}>
        Ligar nova conta
      </button>
    </div>
  );
}

export default ContasSociais;