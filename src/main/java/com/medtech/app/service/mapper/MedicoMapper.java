package com.medtech.app.service.mapper;

import com.medtech.app.domain.Medico;
import com.medtech.app.service.dto.MedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medico} and its DTO {@link MedicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicoMapper extends EntityMapper<MedicoDTO, Medico> {}
