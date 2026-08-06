import { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { useContasSociais } from '../hooks/useContasSociais';
import { useLogger } from '../hooks/useLogger';
import agendamentoService from '../services/agendamentoService';
import publicacaoService from '../services/publicacaoService';
import { TipoAcao } from '../models/enums';

function Agendamentos() {
  const { utilizador } = useAuth();
  const { contas } = useContasSociais(utilizador?.idUtilizador);
  const { logAction } = useLogger();

  const [agendamentos, setAgendamentos] = useState([]);
  const [publicacoes, setPublicacoes] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);

  const [form, setForm] = useState({
    idContaSocial: '',
    idPublicacao: '',
    tipoAcao: TipoAcao.PUBLICAR,
    agendadoPara: '',
    repetirMinutos: '',
  });

  useEffect(() => {
    if (!utilizador) return;

    logAction('CARREGAR_AGENDAMENTOS_INICIO', { idUtilizador: utilizador.idUtilizador });

    Promise.all([
      agendamentoService.listarPorUtilizador(utilizador.idUtilizador),
      publicacaoService.listarPorUtilizador(utilizador.idUtilizador)
    ])
      .then(([dadosAgendamentos, dadosPublicacoes]) => {
        setAgendamentos(dadosAgendamentos);
        setPublicacoes(dadosPublicacoes);
        logAction('CARREGAR_AGENDAMENTOS_SUCESSO', {
          totalAgendamentos: dadosAgendamentos.length,
          totalPublicacoes: dadosPublicacoes.length
        });
      })
      .catch((err) => {
        setErro('Erro ao carregar dados de agendamentos.');
        logAction('ERRO_CARREGAR_AGENDAMENTOS', { mensagem: err?.message });
      })
      .finally(() => setCarregando(false));
  }, [utilizador]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro(null);

    const payload = {
      idContaSocial: Number(form.idContaSocial),
      idPublicacao: form.idPublicacao ? Number(form.idPublicacao) : null,
      tipoAcao: form.tipoAcao,
      agendadoPara: new Date(form.agendadoPara).toISOString(),
      repetirMinutos: form.repetirMinutos ? Number(form.repetirMinutos) : null,
    };

    logAction('SUBMIT_CRIAR_AGENDAMENTO', payload);

    try {
      const novoAgendamento = await agendamentoService.criar(payload);
      setAgendamentos((prev) => [novoAgendamento, ...prev]);
      logAction('CRIAR_AGENDAMENTO_SUCESSO', { idAgendamento: novoAgendamento.idAgendamento });

      setForm({
        idContaSocial: '',
        idPublicacao: '',
        tipoAcao: TipoAcao.PUBLICAR,
        agendadoPara: '',
        repetirMinutos: '',
      });
    } catch (err) {
      setErro('Erro ao criar agendamento.');
      logAction('ERRO_CRIAR_AGENDAMENTO', { mensagem: err?.message });
    }
  };

  const handleCancelar = async (idAgendamento) => {
    logAction('SOLICITAR_CANCELAMENTO_AGENDAMENTO', { idAgendamento });

    try {
      await agendamentoService.cancelar(idAgendamento);
      setAgendamentos((prev) =>
        prev.map((a) => (a.idAgendamento === idAgendamento ? { ...a, estado: 'CANCELADO' } : a))
      );
      logAction('CANCELAR_AGENDAMENTO_SUCESSO', { idAgendamento });
    } catch (err) {
      setErro('Erro ao cancelar agendamento.');
      logAction('ERRO_CANCELAR_AGENDAMENTO', { idAgendamento, mensagem: err?.message });
    }
  };

  if (carregando) return <p>A carregar agendamentos...</p>;

  return (
    <div>
      <h1>Gestão de Agendamentos</h1>

      {erro && <p role="alert">{erro}</p>}

      <section>
        <h2>Novo Agendamento</h2>
        <form onSubmit={handleSubmit}>
          <label htmlFor="idContaSocial">Conta Social</label>
          <select
            id="idContaSocial"
            name="idContaSocial"
            value={form.idContaSocial}
            onChange={handleChange}
            required
          >
            <option value="">Selecione uma conta</option>
            {contas.map((c) => (
              <option key={c.idContaSocial} value={c.idContaSocial}>
                {c.plataforma} — {c.username}
              </option>
            ))}
          </select>

          <label htmlFor="idPublicacao">Publicação (opcional)</label>
          <select
            id="idPublicacao"
            name="idPublicacao"
            value={form.idPublicacao}
            onChange={handleChange}
          >
            <option value="">Nenhuma / Ação Direta</option>
            {publicacoes.map((p) => (
              <option key={p.idPublicacao} value={p.idPublicacao}>
                {p.texto.substring(0, 30)}...
              </option>
            ))}
          </select>

          <label htmlFor="tipoAcao">Tipo de Ação</label>
          <select
            id="tipoAcao"
            name="tipoAcao"
            value={form.tipoAcao}
            onChange={handleChange}
            required
          >
            {Object.keys(TipoAcao).map((key) => (
              <option key={key} value={key}>
                {key}
              </option>
            ))}
          </select>

          <label htmlFor="agendadoPara">Data e Hora</label>
          <input
            id="agendadoPara"
            name="agendadoPara"
            type="datetime-local"
            value={form.agendadoPara}
            onChange={handleChange}
            required
          />

          <label htmlFor="repetirMinutos">Repetir em (minutos, opcional)</label>
          <input
            id="repetirMinutos"
            name="repetirMinutos"
            type="number"
            min="1"
            value={form.repetirMinutos}
            onChange={handleChange}
          />

          <button type="submit">Criar Agendamento</button>
        </form>
      </section>

      <hr />

      <section>
        <h2>Agendamentos Existentes</h2>
        {agendamentos.length === 0 ? (
          <p>Sem agendamentos registados.</p>
        ) : (
          <ul>
            {agendamentos.map((a) => (
              <li key={a.idAgendamento}>
                <strong>{a.tipoAcao}</strong> — Data: {new Date(a.agendadoPara).toLocaleString()} | 
                Estado: <span>{a.estado}</span>
                {a.estado !== 'CANCELADO' && a.estado !== 'CONCLUIDO' && (
                  <button onClick={() => handleCancelar(a.idAgendamento)}>Cancelar</button>
                )}
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}

export default Agendamentos;