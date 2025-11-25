package com.tutomato.catalogservice.interfaces;

import com.tutomato.catalogservice.domain.Catalog;
import com.tutomato.catalogservice.domain.CatalogService;
import com.tutomato.catalogservice.interfaces.dto.CatalogResponse;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogApiController {

    private final CatalogService catalogService;
    private final Environment environment;

    public CatalogApiController(CatalogService catalogService, Environment environment) {
        this.catalogService = catalogService;
        this.environment = environment;
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<CatalogResponse>> getCatalogs() {
        List<Catalog> catalogs = catalogService.findAll();

        return ResponseEntity.ok(CatalogResponse.fromList(catalogs));
    }


    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format("Catalog service is running on local port %s (server port: %s)",
            environment.getProperty("local.server.port"),
            environment.getProperty("local.server.port"));
    }
}
