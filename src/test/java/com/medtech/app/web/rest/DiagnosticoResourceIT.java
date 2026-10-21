package com.medtech.app.web.rest;

import static com.medtech.app.domain.DiagnosticoAsserts.*;
import static com.medtech.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medtech.app.IntegrationTest;
import com.medtech.app.domain.Diagnostico;
import com.medtech.app.repository.DiagnosticoRepository;
import com.medtech.app.service.dto.DiagnosticoDTO;
import com.medtech.app.service.mapper.DiagnosticoMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link DiagnosticoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiagnosticoResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.ofEpochMilli(1703594185720L);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_RECETA_MEDICA = "AAAAAAAAAA";
    private static final String UPDATED_RECETA_MEDICA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/diagnosticos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    private DiagnosticoMapper diagnosticoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiagnosticoMockMvc;

    private Diagnostico diagnostico;

    private Diagnostico insertedDiagnostico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostico createEntity() {
        return new Diagnostico().fecha(DEFAULT_FECHA).descripcion(DEFAULT_DESCRIPCION).recetaMedica(DEFAULT_RECETA_MEDICA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostico createUpdatedEntity() {
        return new Diagnostico().fecha(UPDATED_FECHA).descripcion(UPDATED_DESCRIPCION).recetaMedica(UPDATED_RECETA_MEDICA);
    }

    @BeforeEach
    void initTest() {
        diagnostico = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDiagnostico != null) {
            diagnosticoRepository.delete(insertedDiagnostico);
            insertedDiagnostico = null;
        }
    }

    @Test
    @Transactional
    void createDiagnostico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);
        var returnedDiagnosticoDTO = om.readValue(
            restDiagnosticoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DiagnosticoDTO.class
        );

        // Validate the Diagnostico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDiagnostico = diagnosticoMapper.toEntity(returnedDiagnosticoDTO);
        assertDiagnosticoUpdatableFieldsEquals(returnedDiagnostico, getPersistedDiagnostico(returnedDiagnostico));

        insertedDiagnostico = returnedDiagnostico;
    }

    @Test
    @Transactional
    void createDiagnosticoWithExistingId() throws Exception {
        // Create the Diagnostico with an existing ID
        diagnostico.setId(1L);
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiagnosticoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setFecha(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setDescripcion(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiagnosticos() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnostico.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].recetaMedica").value(hasItem(DEFAULT_RECETA_MEDICA)));
    }

    @Test
    @Transactional
    void getDiagnostico() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get the diagnostico
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL_ID, diagnostico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(diagnostico.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.recetaMedica").value(DEFAULT_RECETA_MEDICA));
    }

    @Test
    @Transactional
    void getNonExistingDiagnostico() throws Exception {
        // Get the diagnostico
        restDiagnosticoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiagnostico() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostico
        Diagnostico updatedDiagnostico = diagnosticoRepository.findById(diagnostico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDiagnostico are not directly saved in db
        em.detach(updatedDiagnostico);
        updatedDiagnostico.fecha(UPDATED_FECHA).descripcion(UPDATED_DESCRIPCION).recetaMedica(UPDATED_RECETA_MEDICA);
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(updatedDiagnostico);

        restDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnosticoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDiagnosticoToMatchAllProperties(updatedDiagnostico);
    }

    @Test
    @Transactional
    void putNonExistingDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnosticoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostico using partial update
        Diagnostico partialUpdatedDiagnostico = new Diagnostico();
        partialUpdatedDiagnostico.setId(diagnostico.getId());

        partialUpdatedDiagnostico.fecha(UPDATED_FECHA).descripcion(UPDATED_DESCRIPCION);

        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnostico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiagnosticoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDiagnostico, diagnostico),
            getPersistedDiagnostico(diagnostico)
        );
    }

    @Test
    @Transactional
    void fullUpdateDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostico using partial update
        Diagnostico partialUpdatedDiagnostico = new Diagnostico();
        partialUpdatedDiagnostico.setId(diagnostico.getId());

        partialUpdatedDiagnostico.fecha(UPDATED_FECHA).descripcion(UPDATED_DESCRIPCION).recetaMedica(UPDATED_RECETA_MEDICA);

        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnostico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiagnosticoUpdatableFieldsEquals(partialUpdatedDiagnostico, getPersistedDiagnostico(partialUpdatedDiagnostico));
    }

    @Test
    @Transactional
    void patchNonExistingDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diagnosticoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiagnostico() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the diagnostico
        restDiagnosticoMockMvc
            .perform(delete(ENTITY_API_URL_ID, diagnostico.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return diagnosticoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Diagnostico getPersistedDiagnostico(Diagnostico diagnostico) {
        return diagnosticoRepository.findById(diagnostico.getId()).orElseThrow();
    }

    protected void assertPersistedDiagnosticoToMatchAllProperties(Diagnostico expectedDiagnostico) {
        assertDiagnosticoAllPropertiesEquals(expectedDiagnostico, getPersistedDiagnostico(expectedDiagnostico));
    }

    protected void assertPersistedDiagnosticoToMatchUpdatableProperties(Diagnostico expectedDiagnostico) {
        assertDiagnosticoAllUpdatablePropertiesEquals(expectedDiagnostico, getPersistedDiagnostico(expectedDiagnostico));
    }
}
