package com.medtech.app.service.mapper;

import com.medtech.app.domain.Insumo;
import com.medtech.app.service.dto.InsumoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Insumo} and its DTO {@link InsumoDTO}.
 */
@Mapper(componentModel = "spring")
public interface InsumoMapper extends EntityMapper<InsumoDTO, Insumo> {}
