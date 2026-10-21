import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCitas } from 'app/entities/cita/cita.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './diagnostico.reducer';

export const DiagnosticoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const citas = useAppSelector(state => state.cita.entities);
  const diagnosticoEntity = useAppSelector(state => state.diagnostico.entity);
  const loading = useAppSelector(state => state.diagnostico.loading);
  const updating = useAppSelector(state => state.diagnostico.updating);
  const updateSuccess = useAppSelector(state => state.diagnostico.updateSuccess);

  const handleClose = () => {
    navigate(`/diagnostico${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCitas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.fecha = convertDateTimeToServer(values.fecha);

    const entity = {
      ...diagnosticoEntity,
      ...values,
      cita: citas.find(it => it.id.toString() === values.cita?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fecha: displayDefaultDateTime(),
        }
      : {
          ...diagnosticoEntity,
          fecha: convertDateTimeFromServer(diagnosticoEntity.fecha),
          cita: diagnosticoEntity?.cita?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="medtechApp.diagnostico.home.createOrEditLabel" data-cy="DiagnosticoCreateUpdateHeading">
            Create or edit a Diagnostico
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm mode="all" defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && <ValidatedField name="id" required readOnly id="diagnostico-id" label="ID" validate={{ required: true }} />}
              <ValidatedField
                label="Fecha"
                id="diagnostico-fecha"
                name="fecha"
                data-cy="fecha"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Descripcion"
                id="diagnostico-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Receta Medica"
                id="diagnostico-recetaMedica"
                name="recetaMedica"
                data-cy="recetaMedica"
                type="textarea"
              />
              <ValidatedField id="diagnostico-cita" name="cita" data-cy="cita" label="Cita" type="select">
                <option value="" key="0" />
                {citas
                  ? citas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/diagnostico" replace variant="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DiagnosticoUpdate;
