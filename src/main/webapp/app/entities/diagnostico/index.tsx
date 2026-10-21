import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Diagnostico from './diagnostico';
import DiagnosticoDeleteDialog from './diagnostico-delete-dialog';
import DiagnosticoDetail from './diagnostico-detail';
import DiagnosticoUpdate from './diagnostico-update';

const DiagnosticoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Diagnostico />} />
    <Route path="new" element={<DiagnosticoUpdate />} />
    <Route path=":id">
      <Route index element={<DiagnosticoDetail />} />
      <Route path="edit" element={<DiagnosticoUpdate />} />
      <Route path="delete" element={<DiagnosticoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DiagnosticoRoutes;
