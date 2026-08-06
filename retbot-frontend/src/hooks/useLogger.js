import { useContext } from 'react';
import { LoggerContext } from '../context/LoggerContext';

export const useLogger = () => {
  const context = useContext(LoggerContext);
  if (!context) {
    throw new Error('useLogger deve ser usado dentro de um LoggerProvider');
  }
  return context;
};