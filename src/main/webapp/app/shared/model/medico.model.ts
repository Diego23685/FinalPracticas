export interface IMedico {
  id?: number;
  nombres?: string;
  apellidos?: string;
  especialidad?: string;
  licenciaMedica?: string;
  telefono?: string | null;
}

export const defaultValue: Readonly<IMedico> = {};
