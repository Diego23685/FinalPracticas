import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostico.reducer';

export const DiagnosticoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const diagnosticoEntity = useAppSelector(state => state.diagnostico.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="diagnosticoDetailsHeading">Diagnostico</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{diagnosticoEntity.id}</dd>
          <dt>
            <span id="fecha">Fecha</span>
          </dt>
          <dd>{diagnosticoEntity.fecha ? <TextFormat value={diagnosticoEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="descripcion">Descripcion</span>
          </dt>
          <dd>{diagnosticoEntity.descripcion}</dd>
          <dt>
            <span id="recetaMedica">Receta Medica</span>
          </dt>
          <dd>{diagnosticoEntity.recetaMedica}</dd>
          <dt>Cita</dt>
          <dd>{diagnosticoEntity.cita ? diagnosticoEntity.cita.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/diagnostico" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/diagnostico/${diagnosticoEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DiagnosticoDetail;
