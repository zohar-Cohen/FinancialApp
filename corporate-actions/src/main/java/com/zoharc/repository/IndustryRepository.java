package com.zoharc.repository;

import org.springframework.data.repository.CrudRepository;

import com.zoharc.persistence.IndustryTbl;

public interface IndustryRepository extends CrudRepository<IndustryTbl, Long> {

}
