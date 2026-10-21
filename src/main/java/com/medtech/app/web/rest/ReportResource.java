package com.medtech.app.web.rest;

import com.medtech.app.service.ExternalApiService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportResource {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping("/citas-por-estado")
    public ResponseEntity<List<Map<String, Object>>> getCitasPorEstado() {
        List<Object[]> rows = em.createNativeQuery("SELECT estado, count(*) FROM cita GROUP BY estado").getResultList();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("estado", row[0] != null ? row[0].toString() : "DESCONOCIDO");
            map.put("total", row[1]);
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/citas-por-medico")
    public ResponseEntity<List<Map<String, Object>>> getCitasPorMedico() {
        String sql =
            "SELECT m.nombres || ' ' || m.apellidos, m.especialidad, count(c.id) " +
            "FROM medico m LEFT JOIN cita c ON c.medico_id = m.id " +
            "GROUP BY m.id, m.nombres, m.apellidos, m.especialidad";
        List<Object[]> rows = em.createNativeQuery(sql).getResultList();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("medico", row[0] != null ? row[0].toString() : "Sin asignar");
            map.put("especialidad", row[1] != null ? row[1].toString() : "");
            map.put("total", row[2]);
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/insumos-stock-critico")
    public ResponseEntity<List<Map<String, Object>>> getInsumosCriticos() {
        String sql = "SELECT codigo, nombre, stock, precio_unitario FROM insumo WHERE stock <= 10 ORDER BY stock ASC";
        List<Object[]> rows = em.createNativeQuery(sql).getResultList();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("codigo", row[0]);
            map.put("nombre", row[1]);
            map.put("stock", row[2]);
            map.put("precioUnitario", row[3]);
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pacientes-por-sangre")
    public ResponseEntity<List<Map<String, Object>>> getPacientesPorSangre() {
        String sql = "SELECT COALESCE(tipo_sangre, 'No especificado'), count(*) FROM paciente GROUP BY tipo_sangre";
        List<Object[]> rows = em.createNativeQuery(sql).getResultList();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("tipo", row[0]);
            map.put("total", row[1]);
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ultimos-diagnosticos")
    public ResponseEntity<List<Map<String, Object>>> getUltimosDiagnosticos() {
        String sql =
            "SELECT d.id, d.fecha, d.descripcion, p.nombres || ' ' || p.apellidos " +
            "FROM diagnostico d " +
            "JOIN cita c ON d.cita_id = c.id " +
            "JOIN paciente p ON c.paciente_id = p.id " +
            "ORDER BY d.fecha DESC LIMIT 10";
        List<Object[]> rows = em.createNativeQuery(sql).getResultList();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("fecha", row[1] != null ? row[1].toString() : "");
            map.put("descripcion", row[2]);
            map.put("paciente", row[3]);
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tipo-cambio")
    public ResponseEntity<Map<String, Object>> getTipoCambio() {
        return ResponseEntity.ok(externalApiService.obtenerTipoCambio());
    }
}
