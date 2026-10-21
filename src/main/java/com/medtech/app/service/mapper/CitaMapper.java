package com.medtech.app.service.mapper;

import com.medtech.app.domain.Cita;
import com.medtech.app.domain.Medico;
import com.medtech.app.domain.Paciente;
import com.medtech.app.service.dto.CitaDTO;
import com.medtech.app.service.dto.MedicoDTO;
import com.medtech.app.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cita} and its DTO {@link CitaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CitaMapper extends EntityMapper<CitaDTO, Cita> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteIdentificacion")
    @Mapping(target = "medico", source = "medico", qualifiedByName = "medicoApellidos")
    CitaDTO toDto(Cita s);

    @Named("pacienteIdentificacion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificacion", source = "identificacion")
    PacienteDTO toDtoPacienteIdentificacion(Paciente paciente);

    @Named("medicoApellidos")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "apellidos", source = "apellidos")
    MedicoDTO toDtoMedicoApellidos(Medico medico);
}
