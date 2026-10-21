package com.medtech.app.service;

import com.medtech.app.domain.Insumo;
import com.medtech.app.repository.InsumoRepository;
import com.medtech.app.service.dto.InsumoDTO;
import com.medtech.app.service.mapper.InsumoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.medtech.app.domain.Insumo}.
 */
@Service
@Transactional
public class InsumoService {

    private static final Logger LOG = LoggerFactory.getLogger(InsumoService.class);

    private final InsumoRepository insumoRepository;

    private final InsumoMapper insumoMapper;

    public InsumoService(InsumoRepository insumoRepository, InsumoMapper insumoMapper) {
        this.insumoRepository = insumoRepository;
        this.insumoMapper = insumoMapper;
    }

    /**
     * Save a insumo.
     *
     * @param insumoDTO the entity to save.
     * @return the persisted entity.
     */
    public InsumoDTO save(InsumoDTO insumoDTO) {
        LOG.debug("Request to save Insumo : {}", insumoDTO);
        Insumo insumo = insumoMapper.toEntity(insumoDTO);
        insumo = insumoRepository.save(insumo);
        return insumoMapper.toDto(insumo);
    }

    /**
     * Update a insumo.
     *
     * @param insumoDTO the entity to save.
     * @return the persisted entity.
     */
    public InsumoDTO update(InsumoDTO insumoDTO) {
        LOG.debug("Request to update Insumo : {}", insumoDTO);
        Insumo insumo = insumoMapper.toEntity(insumoDTO);
        insumo = insumoRepository.save(insumo);
        return insumoMapper.toDto(insumo);
    }

    /**
     * Partially update a insumo.
     *
     * @param insumoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsumoDTO> partialUpdate(InsumoDTO insumoDTO) {
        LOG.debug("Request to partially update Insumo : {}", insumoDTO);

        return insumoRepository
            .findById(insumoDTO.getId())
            .map(existingInsumo -> {
                insumoMapper.partialUpdate(existingInsumo, insumoDTO);

                return existingInsumo;
            })
            .map(insumoRepository::save)
            .map(insumoMapper::toDto);
    }

    /**
     * Get all the insumos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InsumoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Insumos");
        return insumoRepository.findAll(pageable).map(insumoMapper::toDto);
    }

    /**
     * Get one insumo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsumoDTO> findOne(Long id) {
        LOG.debug("Request to get Insumo : {}", id);
        return insumoRepository.findById(id).map(insumoMapper::toDto);
    }

    /**
     * Delete the insumo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Insumo : {}", id);
        insumoRepository.deleteById(id);
    }
}
