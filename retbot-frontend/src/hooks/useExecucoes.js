import { useState, useCallback } from 'react';
import automacaoService from '../services/automacaoService';

export function useExecucoes() {
  const [execucoes, setExecucoes] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

  const carregarPorAgendamento = useCallback(async (idAgendamento) => {
    if (!idAgendamento) return;
    setCarregando(true);
    setErro(null);
    try {
      const dados = await automacaoService.listarExecucoesPorAgendamento(idAgendamento);
      setExecucoes(dados);
    } catch {
      setErro('Erro ao carregar execuções do agendamento.');
    } finally {
      setCarregando(false);
    }
  }, []);

  const carregarFalhadas = useCallback(async () => {
    setCarregando(true);
    setErro(null);
    try {
      const dados = await automacaoService.listarExecucoesFalhadas();
      setExecucoes(dados);
    } catch {
      setErro('Erro ao carregar execuções falhadas.');
    } finally {
      setCarregando(false);
    }
  }, []);

  const iniciarExecucao = async (dados) => {
    setCarregando(true);
    setErro(null);
    try {
      const nova = await automacaoService.iniciarExecucao(dados);
      setExecucoes((prev) => [nova, ...prev]);
      return nova;
    } catch (err) {
      setErro('Erro ao iniciar execução.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  const finalizarExecucao = async (id, dados) => {
    setCarregando(true);
    setErro(null);
    try {
      const atualizada = await automacaoService.finalizarExecucao(id, dados);
      setExecucoes((prev) =>
        prev.map((item) => (item.idExecucao === id ? atualizada : item))
      );
      return atualizada;
    } catch (err) {
      setErro('Erro ao finalizar execução.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  return {
    execucoes,
    carregando,
    erro,
    carregarPorAgendamento,
    carregarFalhadas,
    iniciarExecucao,
    finalizarExecucao,
  };
}