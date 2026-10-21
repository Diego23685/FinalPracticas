import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getMedicos } from 'app/entities/medico/medico.reducer';
import { getEntities as getPacientes } from 'app/entities/paciente/paciente.reducer';
import { EstadoCita } from 'app/shared/model/enumerations/estado-cita.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './cita.reducer';

export const CitaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pacientes = useAppSelector(state => state.paciente.entities);
  const medicos = useAppSelector(state => state.medico.entities);
  const citaEntity = useAppSelector(state => state.cita.entity);
  const loading = useAppSelector(state => state.cita.loading);
  const updating = useAppSelector(state => state.cita.updating);
  const updateSuccess = useAppSelector(state => state.cita.updateSuccess);
  const estadoCitaValues = Object.keys(EstadoCita);

  const handleClose = () => {
    navigate(`/cita${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPacientes({}));
    dispatch(getMedicos({}));
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
    values.fechaHora = convertDateTimeToServer(values.fechaHora);

    const entity = {
      ...citaEntity,
      ...values,
      paciente: pacientes.find(it => it.id.toString() === values.paciente?.toString()),
      medico: medicos.find(it => it.id.toString() === values.medico?.toString()),
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
          fechaHora: displayDefaultDateTime(),
        }
      : {
          estado: 'PENDIENTE',
          ...citaEntity,
          fechaHora: convertDateTimeFromServer(citaEntity.fechaHora),
          paciente: citaEntity?.paciente?.id,
          medico: citaEntity?.medico?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="medtechApp.cita.home.createOrEditLabel" data-cy="CitaCreateUpdateHeading">
            Create or edit a Cita
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm mode="all" defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && <ValidatedField name="id" required readOnly id="cita-id" label="ID" validate={{ required: true }} />}
              <ValidatedField
                label="Fecha Hora"
                id="cita-fechaHora"
                name="fechaHora"
                data-cy="fechaHora"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Motivo"
                id="cita-motivo"
                name="motivo"
                data-cy="motivo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Estado" id="cita-estado" name="estado" data-cy="estado" type="select">
                {estadoCitaValues.map(estadoCita => (
                  <option value={estadoCita} key={estadoCita}>
                    {estadoCita}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="cita-paciente" name="paciente" data-cy="paciente" label="Paciente" type="select">
                <option value="" key="0" />
                {pacientes
                  ? pacientes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.identificacion}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="cita-medico" name="medico" data-cy="medico" label="Medico" type="select">
                <option value="" key="0" />
                {medicos
                  ? medicos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.apellidos}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/cita" replace variant="info">
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

export default CitaUpdate;
