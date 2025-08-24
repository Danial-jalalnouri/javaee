package com.danialechoes.depositSystem.integration;

import com.danialechoes.depositSystem.config.CustomerClientConfig;
import com.danialechoes.depositSystem.dto.CustomerDto;
import com.danialechoes.depositSystem.dto.FileType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "customerClient",
        url = "http://localhost:8080/api/customers",
        contextId = "customerClient",
        configuration = CustomerClientConfig.class
)
public interface CustomerClient {
    @GetMapping("/{id}")
    CustomerDto getCustomerById(@PathVariable("id") Long id);

}
