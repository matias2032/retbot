import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import utilizadorService from '../services/utilizadorService';
import { useLogger } from '../hooks/useLogger';

export default function Utilizadores() {
  const [utilizadores, setUtilizadores] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');
  const [aAlternar, setAAlternar] = useState(null); // idUtilizador em processamento, evita duplo-clique

  const navigate = useNavigate();
  const { logAction } = useLogger();

  useEffect(() => {
    const buscarUtilizadores = async () => {
      logAction('INICIAR_BUSCA_UTILIZADORES');

      try {
        const dados = await utilizadorService.listar();
        setUtilizadores(dados);

        logAction('UTILIZADORES_CARREGADOS_SUCESSO', {
          totalRecebido: dados.length
        });
      } catch (err) {
        console.error(err);
        setErro('Erro ao carregar a lista de utilizadores.');

        logAction('ERRO_BUSCA_UTILIZADORES', {
          status: err.response?.status,
          mensagem: err.message
        });
      } finally {
        setCarregando(false);
      }
    };

    buscarUtilizadores();
  }, []);

  const handleVerDetalhes = (idUtilizador) => {
    logAction('CLIQUE_VER_DETALHES_UTILIZADOR', { idUtilizador });
    navigate(`/utilizadores/${idUtilizador}`);
  };

  const handleAlternarEstado = async (idUtilizador) => {
    logAction('CLIQUE_ALTERNAR_ESTADO_UTILIZADOR', { idUtilizador });
    setAAlternar(idUtilizador);

    try {
      const atualizado = await utilizadorService.alternarEstado(idUtilizador);

      setUtilizadores((prev) =>
        prev.map((u) => (u.idUtilizador === idUtilizador ? atualizado : u))
      );

      logAction('ALTERNAR_ESTADO_UTILIZADOR_SUCESSO', {
        idUtilizador,
        novoEstado: atualizado.ativo
      });
    } catch (err) {
      logAction('ERRO_ALTERNAR_ESTADO_UTILIZADOR', {
        idUtilizador,
        status: err.response?.status,
        mensagem: err.message
      });
      setErro('Erro ao alternar o estado do utilizador.');
    } finally {
      setAAlternar(null);
    }
  };

  return (
    <div className="page-container">
      <div className="page-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2>Gestão de Utilizadores</h2>
        <Link 
          to="/utilizadores/novo" 
          className="btn-primary" 
          style={{ padding: '0.5rem 1rem', textDecoration: 'none' }}
          onClick={() => logAction('CLIQUE_NOVO_UTILIZADOR_BOTAO')}
        >
          + Criar Novo Utilizador
        </Link>
      </div>

      {carregando && <p>A carregar utilizadores...</p>}
      {erro && <div className="alerta-erro">{erro}</div>}

      {!carregando && !erro && (
        <table className="tabela-utilizadores" style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #ccc' }}>
              <th>Nome</th>
              <th>Email</th>
              <th>Perfil</th>
              <th>Estado</th>
              <th>Troca Pendente</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {utilizadores.map((u) => (
              <tr key={u.idUtilizador} style={{ borderBottom: '1px solid #eee' }}>
                <td>{u.nome}</td>
                <td>{u.email}</td>
                <td>{u.perfil || 'Sem Perfil'}</td>
                <td>{u.ativo ? 'Ativo' : 'Inativo'}</td>
                <td>{u.requerTrocaSenha ? 'Sim' : 'Não'}</td>
                <td>
                  <button onClick={() => handleVerDetalhes(u.idUtilizador)}>
                    Ver detalhes
                  </button>
                  {' '}
                  <button
                    onClick={() => handleAlternarEstado(u.idUtilizador)}
                    disabled={aAlternar === u.idUtilizador}
                  >
                    {aAlternar === u.idUtilizador
                      ? 'A processar...'
                      : u.ativo ? 'Desactivar' : 'Activar'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}


