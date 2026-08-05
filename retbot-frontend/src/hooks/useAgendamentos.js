import { useState, useCallback } from 'react';
import publicacaoService from '../services/publicacaoService';

export function useAgendamentos(idContaSocial = null) {
  const [agendamentos, setAgendamentos] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

const carregarPorConta = useCallback(async (idConta) => {
    const idTarget = idConta || idContaSocial;
    if (!idTarget) return;

    setCarregando(true);
    setErro(null);
    try {
      const dados = await publicacaoService.listarAgendamentosPorConta(idTarget);
      setAgendamentos(dados);
    } catch {
      setErro('Erro ao carregar agendamentos.');
    } finally {
      setCarregando(false);
    }
  }, [idContaSocial]);

  const carregarPorEstado = useCallback(async (estado) => {
    setCarregando(true);
    setErro(null);
    try {
      const dados = await publicacaoService.listarAgendamentosPorEstado(estado);
      setAgendamentos(dados);
    } catch {
      setErro('Erro ao filtrar agendamentos por estado.');
    } finally {
      setCarregando(false);
    }
  }, []);

  const criarAgendamento = async (dadosAgendamento) => {
    setCarregando(true);
    setErro(null);
    try {
      const novo = await publicacaoService.criarAgendamento(dadosAgendamento);
      setAgendamentos((prev) => [novo, ...prev]);
      return novo;
    } catch (err) {
      setErro('Erro ao criar agendamento.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  const cancelarAgendamento = async (idAgendamento) => {
    try {
      const atualizado = await publicacaoService.cancelarAgendamento(idAgendamento);
      setAgendamentos((prev) =>
        prev.map((item) => (item.idAgendamento === idAgendamento ? atualizado : item))
      );
    } catch (err) {
      setErro('Erro ao cancelar agendamento.');
      throw err;
    }
  };

  return {
    agendamentos,
    carregando,
    erro,
    carregarPorConta,
    carregarPorEstado,
    criarAgendamento,
    cancelarAgendamento,
  };
}