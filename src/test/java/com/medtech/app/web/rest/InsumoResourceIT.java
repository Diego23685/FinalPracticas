package com.medtech.app.web.rest;

import static com.medtech.app.domain.InsumoAsserts.*;
import static com.medtech.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.medtech.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medtech.app.IntegrationTest;
import com.medtech.app.domain.Insumo;
import com.medtech.app.repository.InsumoRepository;
import com.medtech.app.service.dto.InsumoDTO;
import com.medtech.app.service.mapper.InsumoMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link InsumoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsumoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;

    private static final BigDecimal DEFAULT_PRECIO_UNITARIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_UNITARIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/insumos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private InsumoMapper insumoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsumoMockMvc;

    private Insumo insumo;

    private Insumo insertedInsumo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insumo createEntity() {
        return new Insumo().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO).stock(DEFAULT_STOCK).precioUnitario(DEFAULT_PRECIO_UNITARIO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insumo createUpdatedEntity() {
        return new Insumo().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).stock(UPDATED_STOCK).precioUnitario(UPDATED_PRECIO_UNITARIO);
    }

    @BeforeEach
    void initTest() {
        insumo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsumo != null) {
            insumoRepository.delete(insertedInsumo);
            insertedInsumo = null;
        }
    }

    @Test
    @Transactional
    void createInsumo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Insumo
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);
        var returnedInsumoDTO = om.readValue(
            restInsumoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insumoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsumoDTO.class
        );

        // Validate the Insumo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInsumo = insumoMapper.toEntity(returnedInsumoDTO);
        assertInsumoUpdatableFieldsEquals(returnedInsumo, getPersistedInsumo(returnedInsumo));

        insertedInsumo = returnedInsumo;
    }

    @Test
    @Transactional
    void createInsumoWithExistingId() throws Exception {
        // Create the Insumo with an existing ID
        insumo.setId(1L);
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsumoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insumoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insumo.setNombre(null);

        // Create the Insumo, which fails.
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        restInsumoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insumoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insumo.setCodigo(null);

        // Create the Insumo, which fails.
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        restInsumoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insumoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insumo.setStock(null);

        // Create the Insumo, which fails.
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        restInsumoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insumoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioUnitarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insumo.setPrecioUnitario(null);

        // Create the Insumo, which fails.
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        restInsumoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insumoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInsumos() throws Exception {
        // Initialize the database
        insertedInsumo = insumoRepository.saveAndFlush(insumo);

        // Get all the insumoList
        restInsumoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insumo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(sameNumber(DEFAULT_PRECIO_UNITARIO))));
    }

    @Test
    @Transactional
    void getInsumo() throws Exception {
        // Initialize the database
        insertedInsumo = insumoRepository.saveAndFlush(insumo);

        // Get the insumo
        restInsumoMockMvc
            .perform(get(ENTITY_API_URL_ID, insumo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insumo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.precioUnitario").value(sameNumber(DEFAULT_PRECIO_UNITARIO)));
    }

    @Test
    @Transactional
    void getNonExistingInsumo() throws Exception {
        // Get the insumo
        restInsumoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsumo() throws Exception {
        // Initialize the database
        insertedInsumo = insumoRepository.saveAndFlush(insumo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insumo
        Insumo updatedInsumo = insumoRepository.findById(insumo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsumo are not directly saved in db
        em.detach(updatedInsumo);
        updatedInsumo.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).stock(UPDATED_STOCK).precioUnitario(UPDATED_PRECIO_UNITARIO);
        InsumoDTO insumoDTO = insumoMapper.toDto(updatedInsumo);

        restInsumoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insumoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insumoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsumoToMatchAllProperties(updatedInsumo);
    }

    @Test
    @Transactional
    void putNonExistingInsumo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insumo.setId(longCount.incrementAndGet());

        // Create the Insumo
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsumoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insumoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insumoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsumo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insumo.setId(longCount.incrementAndGet());

        // Create the Insumo
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsumoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insumoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsumo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insumo.setId(longCount.incrementAndGet());

        // Create the Insumo
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsumoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insumoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsumoWithPatch() throws Exception {
        // Initialize the database
        insertedInsumo = insumoRepository.saveAndFlush(insumo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insumo using partial update
        Insumo partialUpdatedInsumo = new Insumo();
        partialUpdatedInsumo.setId(insumo.getId());

        partialUpdatedInsumo.codigo(UPDATED_CODIGO).stock(UPDATED_STOCK).precioUnitario(UPDATED_PRECIO_UNITARIO);

        restInsumoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsumo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsumo))
            )
            .andExpect(status().isOk());

        // Validate the Insumo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsumoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedInsumo, insumo), getPersistedInsumo(insumo));
    }

    @Test
    @Transactional
    void fullUpdateInsumoWithPatch() throws Exception {
        // Initialize the database
        insertedInsumo = insumoRepository.saveAndFlush(insumo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insumo using partial update
        Insumo partialUpdatedInsumo = new Insumo();
        partialUpdatedInsumo.setId(insumo.getId());

        partialUpdatedInsumo.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).stock(UPDATED_STOCK).precioUnitario(UPDATED_PRECIO_UNITARIO);

        restInsumoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsumo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsumo))
            )
            .andExpect(status().isOk());

        // Validate the Insumo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsumoUpdatableFieldsEquals(partialUpdatedInsumo, getPersistedInsumo(partialUpdatedInsumo));
    }

    @Test
    @Transactional
    void patchNonExistingInsumo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insumo.setId(longCount.incrementAndGet());

        // Create the Insumo
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsumoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insumoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insumoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsumo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insumo.setId(longCount.incrementAndGet());

        // Create the Insumo
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsumoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insumoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsumo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insumo.setId(longCount.incrementAndGet());

        // Create the Insumo
        InsumoDTO insumoDTO = insumoMapper.toDto(insumo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsumoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insumoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insumo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsumo() throws Exception {
        // Initialize the database
        insertedInsumo = insumoRepository.saveAndFlush(insumo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insumo
        restInsumoMockMvc
            .perform(delete(ENTITY_API_URL_ID, insumo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insumoRepository.count();
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

    protected Insumo getPersistedInsumo(Insumo insumo) {
        return insumoRepository.findById(insumo.getId()).orElseThrow();
    }

    protected void assertPersistedInsumoToMatchAllProperties(Insumo expectedInsumo) {
        assertInsumoAllPropertiesEquals(expectedInsumo, getPersistedInsumo(expectedInsumo));
    }

    protected void assertPersistedInsumoToMatchUpdatableProperties(Insumo expectedInsumo) {
        assertInsumoAllUpdatablePropertiesEquals(expectedInsumo, getPersistedInsumo(expectedInsumo));
    }
}
