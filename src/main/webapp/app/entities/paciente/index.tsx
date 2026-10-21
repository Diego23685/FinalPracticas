import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Paciente from './paciente';
import PacienteDeleteDialog from './paciente-delete-dialog';
import PacienteDetail from './paciente-detail';
import PacienteUpdate from './paciente-update';

const PacienteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Paciente />} />
    <Route path="new" element={<PacienteUpdate />} />
    <Route path=":id">
      <Route index element={<PacienteDetail />} />
      <Route path="edit" element={<PacienteUpdate />} />
      <Route path="delete" element={<PacienteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PacienteRoutes;
