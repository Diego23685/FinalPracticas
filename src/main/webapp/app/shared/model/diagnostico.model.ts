import dayjs from 'dayjs';

import { ICita } from 'app/shared/model/cita.model';

export interface IDiagnostico {
  id?: number;
  fecha?: dayjs.Dayjs;
  descripcion?: string;
  recetaMedica?: string | null;
  cita?: ICita | null;
}

export const defaultValue: Readonly<IDiagnostico> = {};
