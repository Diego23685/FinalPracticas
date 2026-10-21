import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './paciente.reducer';

export const PacienteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const pacienteEntity = useAppSelector(state => state.paciente.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pacienteDetailsHeading">Paciente</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{pacienteEntity.id}</dd>
          <dt>
            <span id="nombres">Nombres</span>
          </dt>
          <dd>{pacienteEntity.nombres}</dd>
          <dt>
            <span id="apellidos">Apellidos</span>
          </dt>
          <dd>{pacienteEntity.apellidos}</dd>
          <dt>
            <span id="identificacion">Identificacion</span>
          </dt>
          <dd>{pacienteEntity.identificacion}</dd>
          <dt>
            <span id="fechaNacimiento">Fecha Nacimiento</span>
          </dt>
          <dd>
            {pacienteEntity.fechaNacimiento ? (
              <TextFormat value={pacienteEntity.fechaNacimiento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="telefono">Telefono</span>
          </dt>
          <dd>{pacienteEntity.telefono}</dd>
          <dt>
            <span id="tipoSangre">Tipo Sangre</span>
          </dt>
          <dd>{pacienteEntity.tipoSangre}</dd>
        </dl>
        <Button as={Link as any} to="/paciente" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/paciente/${pacienteEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PacienteDetail;
