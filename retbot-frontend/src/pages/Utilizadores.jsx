import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axiosInstance from '../api/axiosInstance';
import { useLogger } from '../hooks/useLogger';

export default function Utilizadores() {
  const [utilizadores, setUtilizadores] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');
  
  // Hook do Logger
  const { logAction } = useLogger();

  useEffect(() => {
    const buscarUtilizadores = async () => {
      logAction('INICIAR_BUSCA_UTILIZADORES');

      try {
        const response = await axiosInstance.get('/utilizadores');
        setUtilizadores(response.data);

        // Registo de Sucesso
        logAction('UTILIZADORES_CARREGADOS_SUCESSO', { 
          totalRecebido: response.data.length 
        });
      } catch (err) {
        console.error(err);
        setErro('Erro ao carregar a lista de utilizadores.');

        // Registo de Falha
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

  return (
    <div className="page-container">
      <div className="page-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2>Gestão de Utilizadores</h2>
        <Link 
          to="/signup" 
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
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}