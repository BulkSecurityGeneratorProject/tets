package com.aviva.repository;

import com.aviva.domain.Insurer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Insurer entity.
 */
@SuppressWarnings("unused")
public interface InsurerRepository extends JpaRepository<Insurer,Long> {

}
