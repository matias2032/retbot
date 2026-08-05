import { useState } from 'react';
import { useExecucoes } from '../hooks/useExecucoes';
import { useRateLimits } from '../hooks/useRateLimits';

function Automacao() {
  const [idAgendamento, setIdAgendamento] = useState('');
  const [idContaSocial, setIdContaSocial] = useState('');

  const { 
    execucoes, 
    carregando: carregandoExec, 
    erro: erroExec, 
    carregarPorAgendamento, 
    carregarFalhadas 
  } = useExecucoes();

  const { 
    rateLimits, 
    carregando: carregandoRate, 
    erro: erroRate, 
    carregarPorConta 
  } = useRateLimits();

  const handleBuscarExecucoes = (e) => {
    e.preventDefault();
    if (idAgendamento) carregarPorAgendamento(Number(idAgendamento));
  };

  const handleBuscarRateLimits = (e) => {
    e.preventDefault();
    if (idContaSocial) carregarPorConta(Number(idContaSocial));
  };

  return (
    <div>
      <h1>Monitorização de Automação</h1>

      <section>
        <h2>Rate Limits por Conta</h2>
        <form onSubmit={handleBuscarRateLimits}>
          <label>ID Conta Social:</label>
          <input 
            type="number" 
            value={idContaSocial} 
            onChange={(e) => setIdContaSocial(e.target.value)} 
            required 
          />
          <button type="submit">Consultar Limites</button>
        </form>

        {carregandoRate && <p>A carregar rate limits...</p>}
        {erroRate && <p role="alert">{erroRate}</p>}

        <ul>
          {rateLimits.map((rl) => (
            <li key={`${rl.idContaSocial}-${rl.endpoint}`}>
              <strong>Endpoint:</strong> {rl.endpoint} | 
              <strong> Restante:</strong> {rl.restante}/{rl.limite} | 
              <strong> Reinicia em:</strong> {rl.reiniciaEm?.toLocaleString()}
            </li>
          ))}
        </ul>
      </section>

      <hr />

      <section>
        <h2>Histórico de Execuções</h2>
        <form onSubmit={handleBuscarExecucoes}>
          <label>ID Agendamento:</label>
          <input 
            type="number" 
            value={idAgendamento} 
            onChange={(e) => setIdAgendamento(e.target.value)} 
            required 
          />
          <button type="submit">Listar por Agendamento</button>
          <button type="button" onClick={carregarFalhadas}>Ver Apenas Falhas</button>
        </form>

        {carregandoExec && <p>A carregar execuções...</p>}
        {erroExec && <p role="alert">{erroExec}</p>}

        <ul>
          {execucoes.map((ex) => (
            <li key={ex.idExecucao}>
              <strong>ID Execução:</strong> {ex.idExecucao} | 
              <strong> Sucesso:</strong> {ex.sucesso ? 'Sim' : 'Não'} | 
              <strong> Código HTTP:</strong> {ex.codigoHttp || 'N/A'} | 
              <strong> Mensagem:</strong> {ex.mensagem || 'Sem mensagem'}
            </li>
          ))}
        </ul>
      </section>
    </div>
  );
}

export default Automacao;