import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Insumo from './insumo';
import InsumoDeleteDialog from './insumo-delete-dialog';
import InsumoDetail from './insumo-detail';
import InsumoUpdate from './insumo-update';

const InsumoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Insumo />} />
    <Route path="new" element={<InsumoUpdate />} />
    <Route path=":id">
      <Route index element={<InsumoDetail />} />
      <Route path="edit" element={<InsumoUpdate />} />
      <Route path="delete" element={<InsumoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InsumoRoutes;
