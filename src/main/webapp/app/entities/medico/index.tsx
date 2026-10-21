import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Medico from './medico';
import MedicoDeleteDialog from './medico-delete-dialog';
import MedicoDetail from './medico-detail';
import MedicoUpdate from './medico-update';

const MedicoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Medico />} />
    <Route path="new" element={<MedicoUpdate />} />
    <Route path=":id">
      <Route index element={<MedicoDetail />} />
      <Route path="edit" element={<MedicoUpdate />} />
      <Route path="delete" element={<MedicoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MedicoRoutes;
