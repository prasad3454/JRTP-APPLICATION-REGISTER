package com.ar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ar.binding.CitizenApp;
import com.ar.service.ArServiceImpl;

@RestController
public class ArRestController {

    @Autowired
    private ArServiceImpl serviceImpl;

    @PostMapping("/app")
    public ResponseEntity<String> createApplication(@RequestBody CitizenApp app) {
        Integer appId = serviceImpl.createApplication(app);

        if (appId > 0) {
            return new ResponseEntity<>("App created with App Id: " + appId, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Invalid SSN", HttpStatus.BAD_REQUEST);
        }
    }
}
