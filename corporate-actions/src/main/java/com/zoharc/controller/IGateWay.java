package com.zoharc.controller;

import org.springframework.http.ResponseEntity;

public interface IGateWay {

	public ResponseEntity<Object> findAllEntities();

}
