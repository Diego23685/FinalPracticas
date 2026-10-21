import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/paciente">
        Paciente
      </MenuItem>
      <MenuItem icon="asterisk" to="/medico">
        Medico
      </MenuItem>
      <MenuItem icon="asterisk" to="/cita">
        Cita
      </MenuItem>
      <MenuItem icon="asterisk" to="/diagnostico">
        Diagnostico
      </MenuItem>
      <MenuItem icon="asterisk" to="/insumo">
        Insumo
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
