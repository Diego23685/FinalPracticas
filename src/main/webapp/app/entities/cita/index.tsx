import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cita from './cita';
import CitaDeleteDialog from './cita-delete-dialog';
import CitaDetail from './cita-detail';
import CitaUpdate from './cita-update';

const CitaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Cita />} />
    <Route path="new" element={<CitaUpdate />} />
    <Route path=":id">
      <Route index element={<CitaDetail />} />
      <Route path="edit" element={<CitaUpdate />} />
      <Route path="delete" element={<CitaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CitaRoutes;
