package com.medtech.app.web.rest;

import static com.medtech.app.domain.CitaAsserts.*;
import static com.medtech.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medtech.app.IntegrationTest;
import com.medtech.app.domain.Cita;
import com.medtech.app.domain.enumeration.EstadoCita;
import com.medtech.app.repository.CitaRepository;
import com.medtech.app.service.CitaService;
import com.medtech.app.service.dto.CitaDTO;
import com.medtech.app.service.mapper.CitaMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CitaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CitaResourceIT {

    private static final Instant DEFAULT_FECHA_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_HORA = Instant.ofEpochMilli(1703594185720L);

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final EstadoCita DEFAULT_ESTADO = EstadoCita.PENDIENTE;
    private static final EstadoCita UPDATED_ESTADO = EstadoCita.COMPLETADA;

    private static final String ENTITY_API_URL = "/api/citas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CitaRepository citaRepository;

    @Mock
    private CitaRepository citaRepositoryMock;

    @Autowired
    private CitaMapper citaMapper;

    @Mock
    private CitaService citaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitaMockMvc;

    private Cita cita;

    private Cita insertedCita;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createEntity() {
        return new Cita().fechaHora(DEFAULT_FECHA_HORA).motivo(DEFAULT_MOTIVO).estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createUpdatedEntity() {
        return new Cita().fechaHora(UPDATED_FECHA_HORA).motivo(UPDATED_MOTIVO).estado(UPDATED_ESTADO);
    }

    @BeforeEach
    void initTest() {
        cita = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCita != null) {
            citaRepository.delete(insertedCita);
            insertedCita = null;
        }
    }

    @Test
    @Transactional
    void createCita() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);
        var returnedCitaDTO = om.readValue(
            restCitaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CitaDTO.class
        );

        // Validate the Cita in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCita = citaMapper.toEntity(returnedCitaDTO);
        assertCitaUpdatableFieldsEquals(returnedCita, getPersistedCita(returnedCita));

        insertedCita = returnedCita;
    }

    @Test
    @Transactional
    void createCitaWithExistingId() throws Exception {
        // Create the Cita with an existing ID
        cita.setId(1L);
        CitaDTO citaDTO = citaMapper.toDto(cita);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaHoraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setFechaHora(null);

        // Create the Cita, which fails.
        CitaDTO citaDTO = citaMapper.toDto(cita);

        restCitaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setMotivo(null);

        // Create the Cita, which fails.
        CitaDTO citaDTO = citaMapper.toDto(cita);

        restCitaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setEstado(null);

        // Create the Cita, which fails.
        CitaDTO citaDTO = citaMapper.toDto(cita);

        restCitaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCitas() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get all the citaList
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(DEFAULT_FECHA_HORA.toString())))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitasWithEagerRelationshipsIsEnabled() throws Exception {
        when(citaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(citaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(citaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(citaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCita() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        // Get the cita
        restCitaMockMvc
            .perform(get(ENTITY_API_URL_ID, cita.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cita.getId().intValue()))
            .andExpect(jsonPath("$.fechaHora").value(DEFAULT_FECHA_HORA.toString()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCita() throws Exception {
        // Get the cita
        restCitaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCita() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita
        Cita updatedCita = citaRepository.findById(cita.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCita are not directly saved in db
        em.detach(updatedCita);
        updatedCita.fechaHora(UPDATED_FECHA_HORA).motivo(UPDATED_MOTIVO).estado(UPDATED_ESTADO);
        CitaDTO citaDTO = citaMapper.toDto(updatedCita);

        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCitaToMatchAllProperties(updatedCita);
    }

    @Test
    @Transactional
    void putNonExistingCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.fechaHora(UPDATED_FECHA_HORA).motivo(UPDATED_MOTIVO).estado(UPDATED_ESTADO);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCita, cita), getPersistedCita(cita));
    }

    @Test
    @Transactional
    void fullUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.fechaHora(UPDATED_FECHA_HORA).motivo(UPDATED_MOTIVO).estado(UPDATED_ESTADO);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitaUpdatableFieldsEquals(partialUpdatedCita, getPersistedCita(partialUpdatedCita));
    }

    @Test
    @Transactional
    void patchNonExistingCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCita() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.saveAndFlush(cita);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cita
        restCitaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cita.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return citaRepository.count();
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

    protected Cita getPersistedCita(Cita cita) {
        return citaRepository.findById(cita.getId()).orElseThrow();
    }

    protected void assertPersistedCitaToMatchAllProperties(Cita expectedCita) {
        assertCitaAllPropertiesEquals(expectedCita, getPersistedCita(expectedCita));
    }

    protected void assertPersistedCitaToMatchUpdatableProperties(Cita expectedCita) {
        assertCitaAllUpdatablePropertiesEquals(expectedCita, getPersistedCita(expectedCita));
    }
}
