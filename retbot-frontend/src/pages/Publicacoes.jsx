import { useState } from 'react';
import { usePublicacoes } from '../hooks/usePublicacoes';

function Publicacoes() {
  const [idContaSocial, setIdContaSocial] = useState('');
  const [texto, setTexto] = useState('');
  const [idPublicacaoExterna, setIdPublicacaoExterna] = useState('');
  
  const { publicacoes, carregando, erro, carregarPublicacoes, criarPublicacao } = usePublicacoes();

  const handleBuscar = (e) => {
    e.preventDefault();
    if (idContaSocial) carregarPublicacoes(idContaSocial);
  };

  const handleCriar = async (e) => {
    e.preventDefault();
    await criarPublicacao({
      idContaSocial: Number(idContaSocial),
      idPublicacaoExterna,
      texto,
      publicadoEm: new Date().toISOString()
    });
    setTexto('');
    setIdPublicacaoExterna('');
  };

  return (
    <div>
      <h1>Gerir Publicações</h1>

      <form onSubmit={handleBuscar}>
        <label>ID Conta Social:</label>
        <input 
          type="number" 
          value={idContaSocial} 
          onChange={(e) => setIdContaSocial(e.target.value)} 
          required 
        />
        <button type="submit">Carregar Publicações</button>
      </form>

      <hr />

      {idContaSocial && (
        <>
          <h2>Nova Publicação</h2>
          <form onSubmit={handleCriar}>
            <div>
              <label>ID Externo:</label>
              <input 
                type="text" 
                value={idPublicacaoExterna} 
                onChange={(e) => setIdPublicacaoExterna(e.target.value)} 
                required 
              />
            </div>
            <div>
              <label>Texto:</label>
              <textarea 
                value={texto} 
                onChange={(e) => setTexto(e.target.value)} 
                required 
              />
            </div>
            <button type="submit" disabled={carregando}>Guardar Publicação</button>
          </form>

          <hr />

          <h2>Histórico</h2>
          {carregando && <p>A carregar...</p>}
          {erro && <p role="alert">{erro}</p>}
          <ul>
            {publicacoes.map((pub) => (
              <li key={pub.idPublicacao}>
                <strong>ID Ext: {pub.idPublicacaoExterna}</strong> — {pub.texto}
              </li>
            ))}
          </ul>
        </>
      )}
    </div>
  );
}

export default Publicacoes;