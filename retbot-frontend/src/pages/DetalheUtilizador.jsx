import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useUtilizador } from '../hooks/useUtilizador';
import { useContasSociais } from '../hooks/useContasSociais';
import { useLogger } from '../hooks/useLogger';

export default function DetalheUtilizador() {
  const { idUtilizador } = useParams();
  const navigate = useNavigate();
  const { logAction } = useLogger();

  const {
    utilizador,
    carregando,
    erro,
    buscarUtilizador,
    atualizarUtilizador,
    alternarEstado,
  } = useUtilizador();

  const {
    contas,
    carregando: carregandoContas,
    erro: erroContas,
    carregarContas,
  } = useContasSociais(idUtilizador);

const [aEditar, setAEditar] = useState(false);
  const [formData, setFormData] = useState({ nome: '', email: '' });
  const [aAlternar, setAAlternar] = useState(false);

  // Estado derivado de `utilizador`, ajustado durante o render (não num Effect):
  // formData é editável pelo utilizador, por isso não pode ser 100% derivado a
  // cada render — só precisa de ser (re)inicializado quando os dados do
  // utilizador carregado mudam (ex: navegação para outro idUtilizador).
  const [idFormInicializado, setIdFormInicializado] = useState(null);
  if (utilizador && idFormInicializado !== utilizador.idUtilizador) {
    setIdFormInicializado(utilizador.idUtilizador);
    setFormData({ nome: utilizador.nome, email: utilizador.email });
  }

  useEffect(() => {
    logAction('ABRIR_DETALHE_UTILIZADOR', { idUtilizador });
    buscarUtilizador(idUtilizador);
    carregarContas();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [idUtilizador]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleGuardar = async (e) => {
    e.preventDefault();
    logAction('SUBMIT_EDITAR_UTILIZADOR', { idUtilizador });
    try {
      await atualizarUtilizador(idUtilizador, formData);
      logAction('EDITAR_UTILIZADOR_SUCESSO', { idUtilizador });
      setAEditar(false);
    } catch {
      // erro já fica disponível em `erro` (vindo do hook) e é exibido abaixo
    }
  };

  const handleAlternarEstado = async () => {
    logAction('CLIQUE_ALTERNAR_ESTADO_DETALHE', { idUtilizador });
    setAAlternar(true);
    try {
      await alternarEstado(idUtilizador);
      logAction('ALTERNAR_ESTADO_DETALHE_SUCESSO', { idUtilizador });
    } catch {
      // erro já fica disponível em `erro`
    } finally {
      setAAlternar(false);
    }
  };

  if (carregando && !utilizador) return <p>A carregar utilizador...</p>;
  if (erro && !utilizador) return <p role="alert">{erro}</p>;
  if (!utilizador) return null;

  return (
    <div className="page-container">
      <div
        className="page-header"
        style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}
      >
        <h2>Detalhe do Utilizador</h2>
        <button onClick={() => navigate('/utilizadores')}>Voltar</button>
      </div>

      {erro && <div className="alerta-erro">{erro}</div>}

      {!aEditar ? (
        <div className="detalhe-card">
          <p><strong>Nome:</strong> {utilizador.nome}</p>
          <p><strong>Email:</strong> {utilizador.email}</p>
          <p><strong>Perfil:</strong> {utilizador.perfil || 'Sem Perfil'}</p>
          <p><strong>Estado:</strong> {utilizador.ativo ? 'Ativo' : 'Inativo'}</p>
          <p><strong>Troca de senha pendente:</strong> {utilizador.requerTrocaSenha ? 'Sim' : 'Não'}</p>
          <p><strong>Criado em:</strong> {new Date(utilizador.criadoEm).toLocaleString('pt-PT')}</p>

          <div className="acoes" style={{ marginTop: '1rem' }}>
            {/* <button onClick={() => setAEditar(true)}>Editar</button> */}
            {' '}
            <button onClick={handleAlternarEstado} disabled={aAlternar}>
              {aAlternar ? 'A processar...' : utilizador.ativo ? 'Desactivar' : 'Activar'}
            </button>
          </div>
        </div>
      ) : (
        <form onSubmit={handleGuardar} className="detalhe-card">
          <div className="form-group">
            <label htmlFor="nome">Nome</label>
            <input
              id="nome"
              name="nome"
              type="text"
              value={formData.nome}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="acoes">
            <button type="submit" disabled={carregando} className="btn-primary">
              {carregando ? 'A guardar...' : 'Guardar'}
            </button>
            <button type="button" onClick={() => setAEditar(false)} disabled={carregando}>
              Cancelar
            </button>
          </div>
        </form>
      )}

      <h3 style={{ marginTop: '2rem' }}>Contas Sociais Ligadas</h3>
      {carregandoContas && <p>A carregar contas sociais...</p>}
      {erroContas && <p role="alert">{erroContas}</p>}
      {!carregandoContas && !erroContas && (
        contas.length === 0 ? (
          <p>Este utilizador ainda não tem contas sociais ligadas.</p>
        ) : (
          <ul>
            {contas.map((conta) => (
              <li key={conta.idContaSocial}>
                <strong>{conta.plataforma}</strong> — {conta.nomeExibicao ?? conta.username}
                {' '}
                <span>({conta.estado})</span>
              </li>
            ))}
          </ul>
        )
      )}
    </div>
  );
}