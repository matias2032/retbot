import { useState, useCallback } from 'react';
import contaSocialService from '../services/contaSocialService';

export function useContasSociais(idUtilizador) {
  const [contas, setContas] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

  const carregarContas = useCallback(async () => {
    if (!idUtilizador) return;
    setCarregando(true);
    setErro(null);
    try {
      const dados = await contaSocialService.listarPorUtilizador(idUtilizador);
      setContas(dados);
    } catch {
      setErro('Não foi possível carregar as contas sociais.');
    } finally {
      setCarregando(false);
    }
  }, [idUtilizador]);

  const removerConta = async (idContaSocial) => {
    setCarregando(true);
    setErro(null);
    try {
      await contaSocialService.remover(idContaSocial);
      setContas((prev) => prev.filter((c) => c.idContaSocial !== idContaSocial));
    } catch (err) {
      setErro('Erro ao remover conta social.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  const atualizarConfiguracao = async (idContaSocial, configuracao) => {
    setCarregando(true);
    setErro(null);
    try {
      const novaConfig = await contaSocialService.atualizarConfiguracao(idContaSocial, configuracao);
      return novaConfig;
    } catch (err) {
      setErro('Erro ao atualizar configuração da conta.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  const iniciarOAuth = (plataforma, urlInstancia = '') => {
    if (!idUtilizador) return;
    contaSocialService.iniciarOAuth(idUtilizador, plataforma, urlInstancia);
  };

  return {
    contas,
    carregando,
    erro,
    carregarContas,
    removerConta,
    atualizarConfiguracao,
    iniciarOAuth,
  };
}