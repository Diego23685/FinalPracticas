import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cita.reducer';

export const CitaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const citaEntity = useAppSelector(state => state.cita.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="citaDetailsHeading">Cita</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{citaEntity.id}</dd>
          <dt>
            <span id="fechaHora">Fecha Hora</span>
          </dt>
          <dd>{citaEntity.fechaHora ? <TextFormat value={citaEntity.fechaHora} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="motivo">Motivo</span>
          </dt>
          <dd>{citaEntity.motivo}</dd>
          <dt>
            <span id="estado">Estado</span>
          </dt>
          <dd>{citaEntity.estado}</dd>
          <dt>Paciente</dt>
          <dd>{citaEntity.paciente ? citaEntity.paciente.identificacion : ''}</dd>
          <dt>Medico</dt>
          <dd>{citaEntity.medico ? citaEntity.medico.apellidos : ''}</dd>
        </dl>
        <Button as={Link as any} to="/cita" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/cita/${citaEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CitaDetail;
