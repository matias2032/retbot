import { useState, useCallback } from 'react';
import publicacaoService from '../services/publicacaoService';

export function usePublicacoes(idContaSocial = null) {
  const [publicacoes, setPublicacoes] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

const carregarPublicacoes = useCallback(async (idConta) => {
    const idTarget = idConta || idContaSocial;
    if (!idTarget) return;

    setCarregando(true);
    setErro(null);
    try {
      const dados = await publicacaoService.listarPorConta(idTarget);
      setPublicacoes(dados);
    } catch {
      setErro('Erro ao carregar publicações da conta.');
    } finally {
      setCarregando(false);
    }
  }, [idContaSocial]);

  const criarPublicacao = async (dadosPublicacao) => {
    setCarregando(true);
    setErro(null);
    try {
      const novaPublicacao = await publicacaoService.criarPublicacao(dadosPublicacao);
      setPublicacoes((prev) => [novaPublicacao, ...prev]);
      return novaPublicacao;
    } catch (err) {
      setErro('Erro ao criar publicação.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  return {
    publicacoes,
    carregando,
    erro,
    carregarPublicacoes,
    criarPublicacao,
  };
}