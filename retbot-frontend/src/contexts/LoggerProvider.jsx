import { LoggerContext } from './LoggerContext';

export function LoggerProvider({ children }) {
  const logAction = (actionName, details = null) => {
    const timestamp = new Date().toLocaleTimeString();
    if (details) {
      console.log(`[AÇÃO UI ${timestamp}] ${actionName}:`, details);
    } else {
      console.log(`[AÇÃO UI ${timestamp}] ${actionName}`);
    }
  };

  return (
    <LoggerContext.Provider value={{ logAction }}>
      {children}
    </LoggerContext.Provider>
  );
}