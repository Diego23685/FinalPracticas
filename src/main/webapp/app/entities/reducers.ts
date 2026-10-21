import cita from 'app/entities/cita/cita.reducer';
import diagnostico from 'app/entities/diagnostico/diagnostico.reducer';
import insumo from 'app/entities/insumo/insumo.reducer';
import medico from 'app/entities/medico/medico.reducer';
import paciente from 'app/entities/paciente/paciente.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  paciente,
  medico,
  cita,
  diagnostico,
  insumo,
  // jhipster-needle-add-reducer-combine - JHipster will add reducer here
};

export default entitiesReducers;
