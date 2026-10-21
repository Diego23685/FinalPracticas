import dayjs from 'dayjs';

export interface IPaciente {
  id?: number;
  nombres?: string;
  apellidos?: string;
  identificacion?: string;
  fechaNacimiento?: dayjs.Dayjs;
  telefono?: string | null;
  tipoSangre?: string | null;
}

export const defaultValue: Readonly<IPaciente> = {};
