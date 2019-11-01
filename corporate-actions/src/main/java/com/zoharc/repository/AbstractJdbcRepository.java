package com.zoharc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

/**
 * @author Zohar Cohen
 * @version 1.0
 * @since 2019=09-23
 *
 * @param <T> - DataBase entity
 */
public abstract class AbstractJdbcRepository<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJdbcRepository.class);

	/**
	 * The method perfroms upsert for the given entity.
	 * First it tries to insert new records, and catch the uniqe constrains error in case the exist exist in the DB.
	 * In case the record with PK in the DB, the method will update the record.
	 * 
	 * @param t - DataBase entity
	 */
	public void upsert(T t) {

		try {
			insert(t);
		} catch (Exception e) {
			if(e instanceof DuplicateKeyException) {
				LOG.info("There is an existing entry for entity: {} with the same PK, updating ", t.getClass().getName());
				update(t);
			}else {
				LOG.error("There is an error during insert {}", e.getMessage());
				throw new RuntimeException(e);
			}

		}
	}

	protected abstract void insert(T t) throws SQLIntegrityConstraintViolationException;
	
	protected abstract void update(T t);
	
	protected abstract Object[] getJdbcQueryArgs(T t);

	protected abstract int[] getJdbcQueryTypes();
	
	protected abstract T mappEntity(ResultSet rs, int rowNum) throws SQLException;
}
