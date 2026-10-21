import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Link, useParams } from 'react-router';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './medico.reducer';

export const MedicoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const medicoEntity = useAppSelector(state => state.medico.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="medicoDetailsHeading">Medico</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{medicoEntity.id}</dd>
          <dt>
            <span id="nombres">Nombres</span>
          </dt>
          <dd>{medicoEntity.nombres}</dd>
          <dt>
            <span id="apellidos">Apellidos</span>
          </dt>
          <dd>{medicoEntity.apellidos}</dd>
          <dt>
            <span id="especialidad">Especialidad</span>
          </dt>
          <dd>{medicoEntity.especialidad}</dd>
          <dt>
            <span id="licenciaMedica">Licencia Medica</span>
          </dt>
          <dd>{medicoEntity.licenciaMedica}</dd>
          <dt>
            <span id="telefono">Telefono</span>
          </dt>
          <dd>{medicoEntity.telefono}</dd>
        </dl>
        <Button as={Link as any} to="/medico" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/medico/${medicoEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MedicoDetail;
