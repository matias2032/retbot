import { useState, useCallback } from 'react';
import utilizadorService from '../services/utilizadorService';

export function useUtilizador() {
  const [utilizador, setUtilizador] = useState(null);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

  const buscarUtilizador = useCallback(async (idUtilizador) => {
    if (!idUtilizador) return;
    setCarregando(true);
    setErro(null);
    try {
      const dados = await utilizadorService.buscar(idUtilizador);
      setUtilizador(dados);
      return dados;
    } catch {
      setErro('Erro ao carregar dados do utilizador.');
    } finally {
      setCarregando(false);
    }
  }, []);

  const atualizarUtilizador = async (idUtilizador, dadosNovos) => {
    setCarregando(true);
    setErro(null);
    try {
      const atualizado = await utilizadorService.atualizar(idUtilizador, dadosNovos);
      setUtilizador(atualizado);
      return atualizado;
    } catch (err) {
      setErro('Erro ao atualizar utilizador.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

const alterarSenha = async (idUtilizador, novaSenha) => {
    setCarregando(true);
    setErro(null);
    try {
      await utilizadorService.alterarSenha(idUtilizador, novaSenha);
    } catch (err) {
      setErro('Erro ao alterar senha.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  return {
    utilizador,
    carregando,
    erro,
    buscarUtilizador,
    atualizarUtilizador,
    alterarSenha,
  };
}