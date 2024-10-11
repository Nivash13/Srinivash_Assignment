package com.example.demo.controller;

import com.example.demo.model.Load;
import com.example.demo.repository.LoadRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/load")
public class LoadController {

    @Autowired
    private LoadRepository loadRepository;

    @PostMapping
    public ResponseEntity<String> createLoad(@RequestBody Load load) {
        loadRepository.save(load);
        return ResponseEntity.ok("Load details added successfully");
    }

    @GetMapping
    public ResponseEntity<String> getLoads(@RequestParam String shipperId) throws JsonProcessingException {
        List<Load> loads = loadRepository.findByShipperId(shipperId);


        StringBuilder responseMessage = new StringBuilder();
        responseMessage.append("List of loads with this shipperId:\n");


        ObjectMapper objectMapper = new ObjectMapper();
        String loadsJson;

        if (loads.isEmpty()) {
            loadsJson = "{\"loads\": []}"; // Empty JSON array if no loads found
        } else {
            loadsJson = objectMapper.writeValueAsString(loads);
        }


        String finalResponse = responseMessage.toString() + loadsJson;

        return ResponseEntity.ok(finalResponse);
    }





    @GetMapping("/{loadId}")
    public ResponseEntity<Load> getLoadById(@PathVariable Long loadId) {
        return loadRepository.findById(loadId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{loadId}")
    public ResponseEntity<Load> updateLoad(@PathVariable Long loadId, @RequestBody Load loadDetails) {
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new RuntimeException("Load not found"));
        load.setLoadingPoint(loadDetails.getLoadingPoint());
        load.setUnloadingPoint(loadDetails.getUnloadingPoint());
        load.setProductType(loadDetails.getProductType());
        load.setTruckType(loadDetails.getTruckType());
        load.setNoOfTrucks(loadDetails.getNoOfTrucks());
        load.setWeight(loadDetails.getWeight());
        load.setComment(loadDetails.getComment());
        load.setShipperId(loadDetails.getShipperId());
        load.setDate(loadDetails.getDate());
        return ResponseEntity.ok(loadRepository.save(load));
    }

    @DeleteMapping("/{loadId}")
    public ResponseEntity<String> deleteLoad(@PathVariable Long loadId) {
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new RuntimeException("Load not found"));
        loadRepository.delete(load);
        return ResponseEntity.ok("Load deleted successfully");
    }

}
