package com.medtech.app.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> obtenerTipoCambio() {
        try {
            String url = "https://open.er-api.com/v6/latest/USD";
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            return Map.of("status", "error", "message", "No se pudo conectar a la API externa");
        }
    }
}
