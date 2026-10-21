import dayjs from 'dayjs';

import { EstadoCita } from 'app/shared/model/enumerations/estado-cita.model';
import { IMedico } from 'app/shared/model/medico.model';
import { IPaciente } from 'app/shared/model/paciente.model';

export interface ICita {
  id?: number;
  fechaHora?: dayjs.Dayjs;
  motivo?: string;
  estado?: keyof typeof EstadoCita;
  paciente?: IPaciente | null;
  medico?: IMedico | null;
}

export const defaultValue: Readonly<ICita> = {};
