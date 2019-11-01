package com.zoharc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.zoharc.persistence.Industry;

public interface IndustryRepository extends CrudRepository<Industry, Integer> {
	
	/**
	 * The method returns default industry code, once there is no industry code found or it is empty
	 * @return default industry object
	 */
	@Query("SELECT i from Industry i WHERE i.industryCode = 1234")
	public Industry findDefaultIndustry(); 

}
