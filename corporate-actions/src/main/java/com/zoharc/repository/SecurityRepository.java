package com.zoharc.repository;

import org.springframework.data.repository.CrudRepository;

import com.zoharc.persistence.SecurityTbl;

public interface SecurityRepository extends CrudRepository<SecurityTbl, Long> {

}
