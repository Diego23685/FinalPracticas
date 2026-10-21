package com.medtech.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.medtech.app.domain.Diagnostico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiagnosticoDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant fecha;

    @NotNull
    private String descripcion;

    @Lob
    private String recetaMedica;

    private CitaDTO cita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRecetaMedica() {
        return recetaMedica;
    }

    public void setRecetaMedica(String recetaMedica) {
        this.recetaMedica = recetaMedica;
    }

    public CitaDTO getCita() {
        return cita;
    }

    public void setCita(CitaDTO cita) {
        this.cita = cita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiagnosticoDTO)) {
            return false;
        }

        DiagnosticoDTO diagnosticoDTO = (DiagnosticoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, diagnosticoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiagnosticoDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", recetaMedica='" + getRecetaMedica() + "'" +
            ", cita=" + getCita() +
            "}";
    }
}
