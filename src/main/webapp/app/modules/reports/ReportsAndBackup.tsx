import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Bar, Pie } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

export const ReportsAndBackup = () => {
  const [citasEstado, setCitasEstado] = useState<any[]>([]);
  const [citasMedico, setCitasMedico] = useState<any[]>([]);
  const [insumosCriticos, setInsumosCriticos] = useState<any[]>([]);
  const [pacientesSangre, setPacientesSangre] = useState<any[]>([]);
  const [ultimosDiag, setUltimosDiag] = useState<any[]>([]);
  const [tipoCambio, setTipoCambio] = useState<any>(null);

  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [statusMsg, setStatusMsg] = useState<string>('');

  useEffect(() => {
    axios
      .get('/api/reports/citas-por-estado')
      .then(res => setCitasEstado(Array.isArray(res.data) ? res.data : []))
      .catch(() => setCitasEstado([]));

    axios
      .get('/api/reports/citas-por-medico')
      .then(res => setCitasMedico(Array.isArray(res.data) ? res.data : []))
      .catch(() => setCitasMedico([]));

    axios
      .get('/api/reports/insumos-stock-critico')
      .then(res => setInsumosCriticos(Array.isArray(res.data) ? res.data : []))
      .catch(() => setInsumosCriticos([]));

    axios
      .get('/api/reports/pacientes-por-sangre')
      .then(res => setPacientesSangre(Array.isArray(res.data) ? res.data : []))
      .catch(() => setPacientesSangre([]));

    axios
      .get('/api/reports/ultimos-diagnosticos')
      .then(res => setUltimosDiag(Array.isArray(res.data) ? res.data : []))
      .catch(() => setUltimosDiag([]));

    axios
      .get('/api/reports/tipo-cambio')
      .then(res => setTipoCambio(res.data))
      .catch(() => setTipoCambio(null));
  }, []);

  const handleBackup = () => {
    setStatusMsg('Generando respaldo...');
    axios
      .get('/api/admin/database/backup', { responseType: 'blob' })
      .then(response => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'medtech_backup.sql');
        document.body.appendChild(link);
        link.click();
        link.remove();
        setStatusMsg('Respaldo descargado con éxito.');
      })
      .catch(() => setStatusMsg('Error al generar respaldo.'));
  };

  const handleRestore = (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedFile) {
      setStatusMsg('Selecciona un archivo .sql primero.');
      return;
    }
    const formData = new FormData();
    formData.append('file', selectedFile);

    setStatusMsg('Restaurando base de datos...');
    axios
      .post('/api/admin/database/restore', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      .then(() => setStatusMsg('Base de datos restaurada correctamente.'))
      .catch(() => setStatusMsg('Fallo en la restauración.'));
  };

  const medicosList = Array.isArray(citasMedico) ? citasMedico : [];
  const barData = {
    labels: medicosList.map(m => m?.medico || 'Sin asignar'),
    datasets: [
      {
        label: 'Citas Atendidas',
        data: medicosList.map(m => m?.total || 0),
        backgroundColor: 'rgba(54, 162, 235, 0.6)',
      },
    ],
  };

  const sangreList = Array.isArray(pacientesSangre) ? pacientesSangre : [];
  const pieData = {
    labels: sangreList.map(p => p?.tipo || 'Desconocido'),
    datasets: [
      {
        label: 'Pacientes',
        data: sangreList.map(p => p?.total || 0),
        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
      },
    ],
  };

  return (
    <div className="container mt-4">
      <h2>Módulo de Reportes y Mantenimiento de BD</h2>

      {tipoCambio && (
        <div className="alert alert-info mt-3">
          <strong>API Externa (RestTemplate):</strong> Base {tipoCambio.base_code || 'USD'} | Actualización:{' '}
          {tipoCambio.time_last_update_utc || 'Al día'}
        </div>
      )}

      <div className="card my-4 p-3 border-primary">
        <h4>Respaldo y Restauración de Base de Datos</h4>
        <div className="row mt-2">
          <div className="col-md-6 mb-2">
            <button className="btn btn-success w-100" onClick={handleBackup}>
              Descargar Respaldo (Backup .SQL)
            </button>
          </div>
          <div className="col-md-6">
            <form onSubmit={handleRestore} className="d-flex gap-2">
              <input
                type="file"
                accept=".sql"
                className="form-control"
                onChange={e => setSelectedFile(e.target.files ? e.target.files[0] : null)}
              />
              <button type="submit" className="btn btn-danger">
                Restaurar
              </button>
            </form>
          </div>
        </div>
        {statusMsg && <div className="mt-2 text-primary fw-bold">{statusMsg}</div>}
      </div>

      <div className="row mt-4">
        <div className="col-md-6 mb-4">
          <div className="card p-3 shadow-sm h-100">
            <h5>Reporte 1: Citas Atendidas por Médico</h5>
            {medicosList.length > 0 ? <Bar data={barData} /> : <p className="text-muted">Sin datos registrados</p>}
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card p-3 shadow-sm h-100">
            <h5>Reporte 2: Distribución por Tipo de Sangre</h5>
            {sangreList.length > 0 ? <Pie data={pieData} /> : <p className="text-muted">Sin datos registrados</p>}
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card p-3 shadow-sm h-100">
            <h5>Reporte 3: Resumen de Citas por Estado</h5>
            <ul className="list-group">
              {Array.isArray(citasEstado) &&
                citasEstado.map((c, i) => (
                  <li key={i} className="list-group-item d-flex justify-content-between align-items-center">
                    Estado: {c?.estado}
                    <span className="badge bg-primary rounded-pill">{c?.total}</span>
                  </li>
                ))}
            </ul>
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card p-3 shadow-sm h-100">
            <h5>Reporte 4: Alerta de Insumos (Stock Crítico)</h5>
            <table className="table table-sm">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nombre</th>
                  <th>Stock</th>
                </tr>
              </thead>
              <tbody>
                {Array.isArray(insumosCriticos) &&
                  insumosCriticos.map((item, i) => (
                    <tr key={i} className="table-warning">
                      <td>{item?.codigo}</td>
                      <td>{item?.nombre}</td>
                      <td>
                        <strong>{item?.stock}</strong>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="col-12 mb-4">
          <div className="card p-3 shadow-sm">
            <h5>Reporte 5: Últimos Diagnósticos Emitidos</h5>
            <table className="table table-striped table-sm">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Fecha</th>
                  <th>Paciente</th>
                  <th>Diagnóstico</th>
                </tr>
              </thead>
              <tbody>
                {Array.isArray(ultimosDiag) &&
                  ultimosDiag.map((d, i) => (
                    <tr key={i}>
                      <td>{d?.id}</td>
                      <td>{d?.fecha}</td>
                      <td>{d?.paciente}</td>
                      <td>{d?.descripcion}</td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReportsAndBackup;
