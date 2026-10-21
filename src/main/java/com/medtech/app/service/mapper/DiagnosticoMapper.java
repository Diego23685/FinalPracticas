package com.medtech.app.service.mapper;

import com.medtech.app.domain.Cita;
import com.medtech.app.domain.Diagnostico;
import com.medtech.app.service.dto.CitaDTO;
import com.medtech.app.service.dto.DiagnosticoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Diagnostico} and its DTO {@link DiagnosticoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiagnosticoMapper extends EntityMapper<DiagnosticoDTO, Diagnostico> {
    @Mapping(target = "cita", source = "cita", qualifiedByName = "citaId")
    DiagnosticoDTO toDto(Diagnostico s);

    @Named("citaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoCitaId(Cita cita);
}
