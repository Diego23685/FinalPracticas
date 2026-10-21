package com.medtech.app.web.rest;

import com.medtech.app.repository.InsumoRepository;
import com.medtech.app.service.InsumoService;
import com.medtech.app.service.dto.InsumoDTO;
import com.medtech.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.medtech.app.domain.Insumo}.
 */
@RestController
@RequestMapping("/api/insumos")
public class InsumoResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsumoResource.class);

    private static final String ENTITY_NAME = "insumo";

    @Value("${jhipster.clientApp.name:medtechApp}")
    private String applicationName;

    private final InsumoService insumoService;

    private final InsumoRepository insumoRepository;

    public InsumoResource(InsumoService insumoService, InsumoRepository insumoRepository) {
        this.insumoService = insumoService;
        this.insumoRepository = insumoRepository;
    }

    /**
     * {@code POST  /insumos} : Create a new insumo.
     *
     * @param insumoDTO the insumoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insumoDTO, or with status {@code 400 (Bad Request)} if the insumo already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsumoDTO> createInsumo(@Valid @RequestBody InsumoDTO insumoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Insumo : {}", insumoDTO);
        if (insumoDTO.getId() != null) {
            throw new BadRequestAlertException("A new insumo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insumoDTO = insumoService.save(insumoDTO);
        return ResponseEntity.created(new URI("/api/insumos/" + insumoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insumoDTO.getId().toString()))
            .body(insumoDTO);
    }

    /**
     * {@code PUT  /insumos/:id} : Updates an existing insumo.
     *
     * @param id the id of the insumoDTO to save.
     * @param insumoDTO the insumoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insumoDTO,
     * or with status {@code 400 (Bad Request)} if the insumoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insumoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsumoDTO> updateInsumo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InsumoDTO insumoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Insumo : {}, {}", id, insumoDTO);
        if (insumoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insumoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insumoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insumoDTO = insumoService.update(insumoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insumoDTO.getId().toString()))
            .body(insumoDTO);
    }

    /**
     * {@code PATCH  /insumos/:id} : Partial updates given fields of an existing insumo, field will ignore if it is null
     *
     * @param id the id of the insumoDTO to save.
     * @param insumoDTO the insumoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insumoDTO,
     * or with status {@code 400 (Bad Request)} if the insumoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the insumoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the insumoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsumoDTO> partialUpdateInsumo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InsumoDTO insumoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Insumo : {}, {}", id, insumoDTO);
        if (insumoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insumoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insumoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsumoDTO> result = insumoService.partialUpdate(insumoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insumoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /insumos} : get all the Insumos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Insumos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsumoDTO>> getAllInsumos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Insumos");
        Page<InsumoDTO> page = insumoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insumos/:id} : get the "id" insumo.
     *
     * @param id the id of the insumoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insumoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsumoDTO> getInsumo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Insumo : {}", id);
        Optional<InsumoDTO> insumoDTO = insumoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insumoDTO);
    }

    /**
     * {@code DELETE  /insumos/:id} : delete the "id" insumo.
     *
     * @param id the id of the insumoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsumo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Insumo : {}", id);
        insumoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
