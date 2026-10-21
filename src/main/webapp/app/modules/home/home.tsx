import './home.scss';

import React from 'react';
import { Link } from 'react-router';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="container py-4">
      {/* Banner Principal */}
      <div className="p-4 mb-4 bg-primary text-white rounded-3 shadow-sm">
        <div className="container-fluid py-2">
          <h1 className="display-6 fw-bold">MedTech — Plataforma Clínica</h1>
          <p className="col-md-9 fs-6 mb-2">
            Sistema integral de gestión de expedientes clínicos, citas médicas, recetas e insumos farmacéuticos.
          </p>
          {account?.login ? (
            <span className="badge bg-light text-dark p-2 fs-7">
              Sesión activa: <strong>{account.login}</strong>
            </span>
          ) : (
            <Link to="/sign-in" className="btn btn-light btn-sm text-primary fw-bold mt-2">
              Iniciar Sesión
            </Link>
          )}
        </div>
      </div>

      {/* Barra de acceso a Reportes */}
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h4 className="text-secondary mb-0">Módulos del Sistema</h4>
        <Link to="/reports" className="btn btn-outline-primary btn-sm fw-bold">
          Ir a Reportes y Respaldo BD
        </Link>
      </div>

      {/* Grid de Formularios */}
      <div className="row g-3">
        {/* Formulario 1: Pacientes */}
        <div className="col-md-4 col-sm-6">
          <div className="card h-100 border-0 shadow-sm">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 className="card-title text-primary fw-bold">Pacientes</h5>
                <p className="card-text text-muted small">Gestión de expedientes demográficos, datos de contacto y tipo de sangre.</p>
              </div>
              <div className="mt-3 d-flex gap-2">
                <Link to="/paciente" className="btn btn-primary btn-sm w-100">
                  Ver Listado
                </Link>
                <Link to="/paciente/new" className="btn btn-outline-primary btn-sm">
                  +
                </Link>
              </div>
            </div>
          </div>
        </div>

        {/* Formulario 2: Médicos */}
        <div className="col-md-4 col-sm-6">
          <div className="card h-100 border-0 shadow-sm">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 className="card-title text-primary fw-bold">Médicos</h5>
                <p className="card-text text-muted small">Administración del personal médico, licencias profesionales y especialidades.</p>
              </div>
              <div className="mt-3 d-flex gap-2">
                <Link to="/medico" className="btn btn-primary btn-sm w-100">
                  Ver Listado
                </Link>
                <Link to="/medico/new" className="btn btn-outline-primary btn-sm">
                  +
                </Link>
              </div>
            </div>
          </div>
        </div>

        {/* Formulario 3: Citas */}
        <div className="col-md-4 col-sm-6">
          <div className="card h-100 border-0 shadow-sm">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 className="card-title text-primary fw-bold">Citas Médicas</h5>
                <p className="card-text text-muted small">Programación de consultas, vinculación médico-paciente y control de estados.</p>
              </div>
              <div className="mt-3 d-flex gap-2">
                <Link to="/cita" className="btn btn-primary btn-sm w-100">
                  Ver Listado
                </Link>
                <Link to="/cita/new" className="btn btn-outline-primary btn-sm">
                  +
                </Link>
              </div>
            </div>
          </div>
        </div>

        {/* Formulario 4: Diagnósticos */}
        <div className="col-md-4 col-sm-6">
          <div className="card h-100 border-0 shadow-sm">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 className="card-title text-primary fw-bold">Diagnósticos</h5>
                <p className="card-text text-muted small">Emisión de diagnósticos clínicos y prescripción de recetas médicas.</p>
              </div>
              <div className="mt-3 d-flex gap-2">
                <Link to="/diagnostico" className="btn btn-primary btn-sm w-100">
                  Ver Listado
                </Link>
                <Link to="/diagnostico/new" className="btn btn-outline-primary btn-sm">
                  +
                </Link>
              </div>
            </div>
          </div>
        </div>

        {/* Formulario 5: Insumos */}
        <div className="col-md-4 col-sm-6">
          <div className="card h-100 border-0 shadow-sm">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 className="card-title text-primary fw-bold">Insumos y Farmacia</h5>
                <p className="card-text text-muted small">Inventario de insumos clínicos, monitoreo de stock crítico y precios.</p>
              </div>
              <div className="mt-3 d-flex gap-2">
                <Link to="/insumo" className="btn btn-primary btn-sm w-100">
                  Ver Listado
                </Link>
                <Link to="/insumo/new" className="btn btn-outline-primary btn-sm">
                  +
                </Link>
              </div>
            </div>
          </div>
        </div>

        {/* Mantenimiento de BD */}
        <div className="col-md-4 col-sm-6">
          <div className="card h-100 border-0 shadow-sm bg-light">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 className="card-title text-dark fw-bold">Mantenimiento BD</h5>
                <p className="card-text text-muted small">Generación de backups `.sql` y restauración de la base de datos PostgreSQL.</p>
              </div>
              <div className="mt-3">
                <Link to="/reports" className="btn btn-dark btn-sm w-100">
                  Panel de Mantenimiento
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Ficha Técnica de Arquitectura */}
      <div className="mt-5 p-3 bg-white border rounded shadow-sm">
        <h6 className="fw-bold text-uppercase text-muted mb-2">Especificación Arquitectónica del Proyecto</h6>
        <div className="row text-center small text-muted">
          <div className="col-md-2 col-4 mb-2">
            <strong>Backend</strong>
            <br />
            Spring Boot 4 / Java 21
          </div>
          <div className="col-md-2 col-4 mb-2">
            <strong>Frontend</strong>
            <br />
            React + Vite
          </div>
          <div className="col-md-2 col-4 mb-2">
            <strong>Seguridad</strong>
            <br />
            Keycloak / OAuth2
          </div>
          <div className="col-md-2 col-4 mb-2">
            <strong>Persistencia</strong>
            <br />
            PostgreSQL + Liquibase
          </div>
          <div className="col-md-2 col-4 mb-2">
            <strong>API Externa</strong>
            <br />
            RestTemplate
          </div>
          <div className="col-md-2 col-4 mb-2">
            <strong>Despliegue</strong>
            <br />
            Docker Engine
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
