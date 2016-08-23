package com.aviva.web.rest;

import com.aviva.AvivaApp;
import com.aviva.domain.Insurer;
import com.aviva.repository.InsurerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InsurerResource REST controller.
 *
 * @see InsurerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AvivaApp.class)
@WebAppConfiguration
@IntegrationTest
public class InsurerResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MARITAL_STATUS = "AAAAA";
    private static final String UPDATED_MARITAL_STATUS = "BBBBB";

    private static final Integer DEFAULT_POST_CODE = 1;
    private static final Integer UPDATED_POST_CODE = 2;
    private static final String DEFAULT_EMAIL_ID = "AAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBB";

    @Inject
    private InsurerRepository insurerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInsurerMockMvc;

    private Insurer insurer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InsurerResource insurerResource = new InsurerResource();
        ReflectionTestUtils.setField(insurerResource, "insurerRepository", insurerRepository);
        this.restInsurerMockMvc = MockMvcBuilders.standaloneSetup(insurerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        insurer = new Insurer();
        insurer.setTitle(DEFAULT_TITLE);
        insurer.setFirstName(DEFAULT_FIRST_NAME);
        insurer.setLastName(DEFAULT_LAST_NAME);
        insurer.setDob(DEFAULT_DOB);
        insurer.setMaritalStatus(DEFAULT_MARITAL_STATUS);
        insurer.setPostCode(DEFAULT_POST_CODE);
        insurer.setEmailId(DEFAULT_EMAIL_ID);
    }

    @Test
    @Transactional
    public void createInsurer() throws Exception {
        int databaseSizeBeforeCreate = insurerRepository.findAll().size();

        // Create the Insurer

        restInsurerMockMvc.perform(post("/api/insurers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(insurer)))
                .andExpect(status().isCreated());

        // Validate the Insurer in the database
        List<Insurer> insurers = insurerRepository.findAll();
        assertThat(insurers).hasSize(databaseSizeBeforeCreate + 1);
        Insurer testInsurer = insurers.get(insurers.size() - 1);
        assertThat(testInsurer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInsurer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testInsurer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testInsurer.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testInsurer.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testInsurer.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testInsurer.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
    }

    @Test
    @Transactional
    public void getAllInsurers() throws Exception {
        // Initialize the database
        insurerRepository.saveAndFlush(insurer);

        // Get all the insurers
        restInsurerMockMvc.perform(get("/api/insurers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(insurer.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
                .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
                .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
                .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID.toString())));
    }

    @Test
    @Transactional
    public void getInsurer() throws Exception {
        // Initialize the database
        insurerRepository.saveAndFlush(insurer);

        // Get the insurer
        restInsurerMockMvc.perform(get("/api/insurers/{id}", insurer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(insurer.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsurer() throws Exception {
        // Get the insurer
        restInsurerMockMvc.perform(get("/api/insurers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsurer() throws Exception {
        // Initialize the database
        insurerRepository.saveAndFlush(insurer);
        int databaseSizeBeforeUpdate = insurerRepository.findAll().size();

        // Update the insurer
        Insurer updatedInsurer = new Insurer();
        updatedInsurer.setId(insurer.getId());
        updatedInsurer.setTitle(UPDATED_TITLE);
        updatedInsurer.setFirstName(UPDATED_FIRST_NAME);
        updatedInsurer.setLastName(UPDATED_LAST_NAME);
        updatedInsurer.setDob(UPDATED_DOB);
        updatedInsurer.setMaritalStatus(UPDATED_MARITAL_STATUS);
        updatedInsurer.setPostCode(UPDATED_POST_CODE);
        updatedInsurer.setEmailId(UPDATED_EMAIL_ID);

        restInsurerMockMvc.perform(put("/api/insurers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInsurer)))
                .andExpect(status().isOk());

        // Validate the Insurer in the database
        List<Insurer> insurers = insurerRepository.findAll();
        assertThat(insurers).hasSize(databaseSizeBeforeUpdate);
        Insurer testInsurer = insurers.get(insurers.size() - 1);
        assertThat(testInsurer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInsurer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testInsurer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testInsurer.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testInsurer.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testInsurer.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testInsurer.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
    }

    @Test
    @Transactional
    public void deleteInsurer() throws Exception {
        // Initialize the database
        insurerRepository.saveAndFlush(insurer);
        int databaseSizeBeforeDelete = insurerRepository.findAll().size();

        // Get the insurer
        restInsurerMockMvc.perform(delete("/api/insurers/{id}", insurer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Insurer> insurers = insurerRepository.findAll();
        assertThat(insurers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
