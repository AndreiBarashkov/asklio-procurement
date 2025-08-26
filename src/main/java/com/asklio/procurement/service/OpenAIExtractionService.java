package com.asklio.procurement.service;

import com.asklio.procurement.dto.IntakeRequestDTO;
import com.asklio.procurement.dto.openai.OpenAiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIExtractionService {

    @Value("${openai.api.key}")
    private String apiKey;

    public IntakeRequestDTO extractIntakeFromText(String extractedText) throws IOException {
        String prompt = buildPrompt(extractedText);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are an assistant that extracts structured procurement intake data from vendor documents."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<OpenAiResponse> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                request,
                OpenAiResponse.class
        );

        String json = response.getBody().getChoices().getFirst().getMessage().getContent();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(json, IntakeRequestDTO.class);
    }

    private String buildPrompt(String extractedText) {
        return "Extract structured intake data (in JSON format matching this structure: { requestorName, title, vendorName, vatId, commodityGroup, department, orders: [ { description, amount, unit, unitPrice } ] }) from the following PDF content:\n\n" + extractedText;
    }
}

