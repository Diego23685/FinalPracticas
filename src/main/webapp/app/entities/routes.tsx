import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';

import Cita from './cita';
import Diagnostico from './diagnostico';
import Insumo from './insumo';
import Medico from './medico';
import Paciente from './paciente';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/paciente/*" element={<Paciente />} />
        <Route path="/medico/*" element={<Medico />} />
        <Route path="/cita/*" element={<Cita />} />
        <Route path="/diagnostico/*" element={<Diagnostico />} />
        <Route path="/insumo/*" element={<Insumo />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
