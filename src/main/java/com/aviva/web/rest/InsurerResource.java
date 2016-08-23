package com.aviva.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aviva.domain.Insurer;
import com.aviva.repository.InsurerRepository;
import com.aviva.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Insurer.
 */
@RestController
@RequestMapping("/api")
public class InsurerResource {

    private final Logger log = LoggerFactory.getLogger(InsurerResource.class);
        
    @Inject
    private InsurerRepository insurerRepository;
    
    /**
     * POST  /insurers : Create a new insurer.
     *
     * @param insurer the insurer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insurer, or with status 400 (Bad Request) if the insurer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/insurers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Insurer> createInsurer(@RequestBody Insurer insurer) throws URISyntaxException {
        log.debug("REST request to save Insurer : {}", insurer);
        if (insurer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("insurer", "idexists", "A new insurer cannot already have an ID")).body(null);
        }
        Insurer result = insurerRepository.save(insurer);
        return ResponseEntity.created(new URI("/api/insurers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("insurer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insurers : Updates an existing insurer.
     *
     * @param insurer the insurer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insurer,
     * or with status 400 (Bad Request) if the insurer is not valid,
     * or with status 500 (Internal Server Error) if the insurer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/insurers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Insurer> updateInsurer(@RequestBody Insurer insurer) throws URISyntaxException {
        log.debug("REST request to update Insurer : {}", insurer);
        if (insurer.getId() == null) {
            return createInsurer(insurer);
        }
        Insurer result = insurerRepository.save(insurer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("insurer", insurer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insurers : get all the insurers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of insurers in body
     */
    @RequestMapping(value = "/insurers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Insurer> getAllInsurers() {
        log.debug("REST request to get all Insurers");
        List<Insurer> insurers = insurerRepository.findAll();
        return insurers;
    }

    /**
     * GET  /insurers/:id : get the "id" insurer.
     *
     * @param id the id of the insurer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insurer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/insurers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Insurer> getInsurer(@PathVariable Long id) {
        log.debug("REST request to get Insurer : {}", id);
        Insurer insurer = insurerRepository.findOne(id);
        return Optional.ofNullable(insurer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /insurers/:id : delete the "id" insurer.
     *
     * @param id the id of the insurer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/insurers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInsurer(@PathVariable Long id) {
        log.debug("REST request to delete Insurer : {}", id);
        insurerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("insurer", id.toString())).build();
    }

}
