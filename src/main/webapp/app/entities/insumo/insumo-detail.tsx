import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Link, useParams } from 'react-router';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './insumo.reducer';

export const InsumoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const insumoEntity = useAppSelector(state => state.insumo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="insumoDetailsHeading">Insumo</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{insumoEntity.id}</dd>
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{insumoEntity.nombre}</dd>
          <dt>
            <span id="codigo">Codigo</span>
          </dt>
          <dd>{insumoEntity.codigo}</dd>
          <dt>
            <span id="stock">Stock</span>
          </dt>
          <dd>{insumoEntity.stock}</dd>
          <dt>
            <span id="precioUnitario">Precio Unitario</span>
          </dt>
          <dd>{insumoEntity.precioUnitario}</dd>
        </dl>
        <Button as={Link as any} to="/insumo" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/insumo/${insumoEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InsumoDetail;
