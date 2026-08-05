import { useState, useCallback } from 'react';
import automacaoService from '../services/automacaoService';

export function useRateLimits() {
  const [rateLimits, setRateLimits] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

  const carregarPorConta = useCallback(async (idContaSocial) => {
    if (!idContaSocial) return;
    setCarregando(true);
    setErro(null);
    try {
      const dados = await automacaoService.listarRateLimitsPorConta(idContaSocial);
      setRateLimits(dados);
    } catch {
      setErro('Erro ao carregar rate limits da conta.');
    } finally {
      setCarregando(false);
    }
  }, []);

  const obterOuCriarRateLimit = async (dados) => {
    setCarregando(true);
    setErro(null);
    try {
      const res = await automacaoService.obterOuCriarRateLimit(dados);
      setRateLimits((prev) => {
        const idx = prev.findIndex(
          (r) => r.idContaSocial === res.idContaSocial && r.endpoint === res.endpoint
        );
        if (idx >= 0) {
          const updated = [...prev];
          updated[idx] = res;
          return updated;
        }
        return [...prev, res];
      });
      return res;
    } catch (err) {
      setErro('Erro ao definir rate limit.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  const consumir = async (dados) => {
    setCarregando(true);
    setErro(null);
    try {
      const atualizado = await automacaoService.consumirRateLimit(dados);
      setRateLimits((prev) =>
        prev.map((r) =>
          r.idContaSocial === atualizado.idContaSocial && r.endpoint === atualizado.endpoint
            ? atualizado
            : r
        )
      );
      return atualizado;
    } catch (err) {
      setErro('Erro ao consumir rate limit.');
      throw err;
    } finally {
      setCarregando(false);
    }
  };

  return {
    rateLimits,
    carregando,
    erro,
    carregarPorConta,
    obterOuCriarRateLimit,
    consumir,
  };
}