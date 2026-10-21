package com.medtech.app.web.rest;

import static com.medtech.app.domain.MedicoAsserts.*;
import static com.medtech.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medtech.app.IntegrationTest;
import com.medtech.app.domain.Medico;
import com.medtech.app.repository.MedicoRepository;
import com.medtech.app.service.dto.MedicoDTO;
import com.medtech.app.service.mapper.MedicoMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link MedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicoResourceIT {

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_ESPECIALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_LICENCIA_MEDICA = "AAAAAAAAAA";
    private static final String UPDATED_LICENCIA_MEDICA = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MedicoMapper medicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicoMockMvc;

    private Medico medico;

    private Medico insertedMedico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createEntity() {
        return new Medico()
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .especialidad(DEFAULT_ESPECIALIDAD)
            .licenciaMedica(DEFAULT_LICENCIA_MEDICA)
            .telefono(DEFAULT_TELEFONO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createUpdatedEntity() {
        return new Medico()
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .especialidad(UPDATED_ESPECIALIDAD)
            .licenciaMedica(UPDATED_LICENCIA_MEDICA)
            .telefono(UPDATED_TELEFONO);
    }

    @BeforeEach
    void initTest() {
        medico = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMedico != null) {
            medicoRepository.delete(insertedMedico);
            insertedMedico = null;
        }
    }

    @Test
    @Transactional
    void createMedico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);
        var returnedMedicoDTO = om.readValue(
            restMedicoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicoDTO.class
        );

        // Validate the Medico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedico = medicoMapper.toEntity(returnedMedicoDTO);
        assertMedicoUpdatableFieldsEquals(returnedMedico, getPersistedMedico(returnedMedico));

        insertedMedico = returnedMedico;
    }

    @Test
    @Transactional
    void createMedicoWithExistingId() throws Exception {
        // Create the Medico with an existing ID
        medico.setId(1L);
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setNombres(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setApellidos(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEspecialidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setEspecialidad(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicenciaMedicaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setLicenciaMedica(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicos() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].especialidad").value(hasItem(DEFAULT_ESPECIALIDAD)))
            .andExpect(jsonPath("$.[*].licenciaMedica").value(hasItem(DEFAULT_LICENCIA_MEDICA)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get the medico
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL_ID, medico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medico.getId().intValue()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.especialidad").value(DEFAULT_ESPECIALIDAD))
            .andExpect(jsonPath("$.licenciaMedica").value(DEFAULT_LICENCIA_MEDICA))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingMedico() throws Exception {
        // Get the medico
        restMedicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedico are not directly saved in db
        em.detach(updatedMedico);
        updatedMedico
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .especialidad(UPDATED_ESPECIALIDAD)
            .licenciaMedica(UPDATED_LICENCIA_MEDICA)
            .telefono(UPDATED_TELEFONO);
        MedicoDTO medicoDTO = medicoMapper.toDto(updatedMedico);

        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicoToMatchAllProperties(updatedMedico);
    }

    @Test
    @Transactional
    void putNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .licenciaMedica(UPDATED_LICENCIA_MEDICA)
            .telefono(UPDATED_TELEFONO);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedico, medico), getPersistedMedico(medico));
    }

    @Test
    @Transactional
    void fullUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .especialidad(UPDATED_ESPECIALIDAD)
            .licenciaMedica(UPDATED_LICENCIA_MEDICA)
            .telefono(UPDATED_TELEFONO);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(partialUpdatedMedico, getPersistedMedico(partialUpdatedMedico));
    }

    @Test
    @Transactional
    void patchNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medico
        restMedicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medico.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicoRepository.count();
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

    protected Medico getPersistedMedico(Medico medico) {
        return medicoRepository.findById(medico.getId()).orElseThrow();
    }

    protected void assertPersistedMedicoToMatchAllProperties(Medico expectedMedico) {
        assertMedicoAllPropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }

    protected void assertPersistedMedicoToMatchUpdatableProperties(Medico expectedMedico) {
        assertMedicoAllUpdatablePropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }
}
