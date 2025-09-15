package com.danialechoes.aiservice.controller;

import com.danialechoes.aiservice.dto.QueryRequest;
import com.danialechoes.aiservice.dto.QueryResponse;
import com.danialechoes.aiservice.service.AiQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiQueryController {

    private final AiQueryService aiQueryService;

    @Autowired
    public AiQueryController(AiQueryService aiQueryService) {
        this.aiQueryService = aiQueryService;
    }

    @PostMapping("/query")
    public ResponseEntity<QueryResponse> processQuery(@RequestBody QueryRequest request) {
        QueryResponse response = aiQueryService.processNaturalLanguageQuery(request.getQuestion());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AI Service is up and running!");
    }

}
