package com.medtech.app.web.rest;

import com.medtech.app.repository.DiagnosticoRepository;
import com.medtech.app.service.DiagnosticoService;
import com.medtech.app.service.dto.DiagnosticoDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.medtech.app.domain.Diagnostico}.
 */
@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticoResource.class);

    private static final String ENTITY_NAME = "diagnostico";

    @Value("${jhipster.clientApp.name:medtechApp}")
    private String applicationName;

    private final DiagnosticoService diagnosticoService;

    private final DiagnosticoRepository diagnosticoRepository;

    public DiagnosticoResource(DiagnosticoService diagnosticoService, DiagnosticoRepository diagnosticoRepository) {
        this.diagnosticoService = diagnosticoService;
        this.diagnosticoRepository = diagnosticoRepository;
    }

    /**
     * {@code POST  /diagnosticos} : Create a new diagnostico.
     *
     * @param diagnosticoDTO the diagnosticoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diagnosticoDTO, or with status {@code 400 (Bad Request)} if the diagnostico already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DiagnosticoDTO> createDiagnostico(@Valid @RequestBody DiagnosticoDTO diagnosticoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Diagnostico : {}", diagnosticoDTO);
        if (diagnosticoDTO.getId() != null) {
            throw new BadRequestAlertException("A new diagnostico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        diagnosticoDTO = diagnosticoService.save(diagnosticoDTO);
        return ResponseEntity.created(new URI("/api/diagnosticos/" + diagnosticoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, diagnosticoDTO.getId().toString()))
            .body(diagnosticoDTO);
    }

    /**
     * {@code PUT  /diagnosticos/:id} : Updates an existing diagnostico.
     *
     * @param id the id of the diagnosticoDTO to save.
     * @param diagnosticoDTO the diagnosticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticoDTO,
     * or with status {@code 400 (Bad Request)} if the diagnosticoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> updateDiagnostico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DiagnosticoDTO diagnosticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Diagnostico : {}, {}", id, diagnosticoDTO);
        if (diagnosticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diagnosticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        diagnosticoDTO = diagnosticoService.update(diagnosticoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, diagnosticoDTO.getId().toString()))
            .body(diagnosticoDTO);
    }

    /**
     * {@code PATCH  /diagnosticos/:id} : Partial updates given fields of an existing diagnostico, field will ignore if it is null
     *
     * @param id the id of the diagnosticoDTO to save.
     * @param diagnosticoDTO the diagnosticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticoDTO,
     * or with status {@code 400 (Bad Request)} if the diagnosticoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the diagnosticoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DiagnosticoDTO> partialUpdateDiagnostico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DiagnosticoDTO diagnosticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Diagnostico : {}, {}", id, diagnosticoDTO);
        if (diagnosticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diagnosticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DiagnosticoDTO> result = diagnosticoService.partialUpdate(diagnosticoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, diagnosticoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /diagnosticos} : get all the Diagnosticos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Diagnosticos in body.
     */
    @GetMapping("")
    @Transactional(readOnly = true)
    public ResponseEntity<List<DiagnosticoDTO>> getAllDiagnosticos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Diagnosticos");
        Page<DiagnosticoDTO> page = diagnosticoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /diagnosticos/:id} : get the "id" diagnostico.
     *
     * @param id the id of the diagnosticoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diagnosticoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> getDiagnostico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Diagnostico : {}", id);
        Optional<DiagnosticoDTO> diagnosticoDTO = diagnosticoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(diagnosticoDTO);
    }

    /**
     * {@code DELETE  /diagnosticos/:id} : delete the "id" diagnostico.
     *
     * @param id the id of the diagnosticoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagnostico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Diagnostico : {}", id);
        diagnosticoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
