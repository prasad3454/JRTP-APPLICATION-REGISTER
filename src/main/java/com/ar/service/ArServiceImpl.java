package com.ar.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ar.binding.CitizenApp;
import com.ar.entity.CitizenAppEntity;
import com.ar.repository.CitizenAppRepository;

@Service
public class ArServiceImpl implements ArService {

    @Autowired
    private CitizenAppRepository appRepository;

    @Override
    public Integer createApplication(CitizenApp app) {
        // Make rest call to ssa-web api with ssn as input
        String endPoint = "http://localhost:9000/ssn/{ssn}";
       RestTemplate rt = new RestTemplate();
//        WebClient webClient = WebClient.create();

        try {
            ResponseEntity<String> resEntity = rt.getForEntity(endPoint, String.class, app.getSsn());
            String stateName = resEntity.getBody();
//        	String stateName = webClient.get()
//        							.uri(endPoint, app.getSsn())
//        							.retrieve()
//        							.bodyToMono(String.class)
//        							.block();

            // Log the stateName and status code
            System.out.println("Response Status Code: " + resEntity.getStatusCode());
            System.out.println("Response Body: " + stateName);

            if ("New Jersey".equals(stateName)) {
                CitizenAppEntity appEntity = new CitizenAppEntity();
                BeanUtils.copyProperties(app, appEntity);
                appEntity.setStateName(stateName);
                CitizenAppEntity save = appRepository.save(appEntity);
                return save.getAppId();
            }

        }catch (WebClientResponseException e) {
            // Log the error details
            System.out.println("Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // Log the error details
            System.out.println("Error: " + e.getMessage());
        }

        return 0;
    }

}
//package com.ar.service;
//
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.scheduler.Schedulers;
//
//import com.ar.binding.CitizenApp;
//import com.ar.entity.CitizenAppEntity;
//import com.ar.repository.CitizenAppRepository;
//
//@Service
//public class ArServiceImpl implements ArService {
//
//    @Autowired
//    private CitizenAppRepository appRepository;
//
//    @Override
//    public Integer createApplication(CitizenApp app) {
//        // Make rest call to ssa-web api with ssn as input
//        String endPoint = "http://localhost:9000/ssn/{ssn}";
//        WebClient webClient = WebClient.create();
//
//        // Execute the WebClient request on a different thread pool
//        return webClient.get()
//                        .uri(endPoint, app.getSsn())
//                        .retrieve()
//                        .bodyToMono(String.class)
//                        .subscribeOn(Schedulers.boundedElastic()) // Offload blocking operations to a separate thread pool
//                        .map(stateName -> {
//                            System.out.println("Response Body: " + stateName);
//
//                            if ("New Jersey".equals(stateName)) {
//                                // Handle saving the entity, which is a blocking operation
//                                CitizenAppEntity appEntity = new CitizenAppEntity();
//                                BeanUtils.copyProperties(app, appEntity);
//                                appEntity.setStateName(stateName);
//                                CitizenAppEntity savedEntity = appRepository.save(appEntity);
//                                return savedEntity.getAppId();
//                            }
//
//                            return 0;
//                        })
//                        .doOnError(e -> System.err.println("Error: " + e.getMessage())) // Log errors
//                        .block(); // Block to get the result
//    }
//}

