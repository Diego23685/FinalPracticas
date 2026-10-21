package com.medtech.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.medtech.app.domain.Medico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombres;

    @NotNull
    private String apellidos;

    @NotNull
    private String especialidad;

    @NotNull
    private String licenciaMedica;

    private String telefono;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getLicenciaMedica() {
        return licenciaMedica;
    }

    public void setLicenciaMedica(String licenciaMedica) {
        this.licenciaMedica = licenciaMedica;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicoDTO)) {
            return false;
        }

        MedicoDTO medicoDTO = (MedicoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicoDTO{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            ", licenciaMedica='" + getLicenciaMedica() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
