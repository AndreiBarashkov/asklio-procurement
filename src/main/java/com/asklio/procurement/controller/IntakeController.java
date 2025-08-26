package com.asklio.procurement.controller;

import com.asklio.procurement.dto.IntakeRequestDTO;
import com.asklio.procurement.dto.IntakeResponseDTO;
import com.asklio.procurement.entity.IntakeStatus;
import com.asklio.procurement.service.IntakeService;
import com.asklio.procurement.service.OpenAIExtractionService;
import com.asklio.procurement.service.PdfExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/intake")
public class IntakeController {

    private final IntakeService intakeService;
    private final PdfExtractionService pdfExtractionService;
    private final OpenAIExtractionService openAiExtractionService;

    @Autowired
    public IntakeController(IntakeService intakeService, PdfExtractionService pdfExtractionService, OpenAIExtractionService openAiExtractionService) {
        this.intakeService = intakeService;
        this.pdfExtractionService = pdfExtractionService;
        this.openAiExtractionService = openAiExtractionService;
    }

    @GetMapping
    public List<IntakeResponseDTO> getAllIntakes() {
        return intakeService.getAllIntakes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntakeResponseDTO> getIntake(@PathVariable String id) {
        return intakeService.getIntakeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IntakeResponseDTO> createIntake(@RequestBody IntakeRequestDTO intakeDTO) {
        IntakeResponseDTO savedIntake = intakeService.createIntake(intakeDTO);
        return ResponseEntity.ok(savedIntake);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable String id,
            @RequestParam IntakeStatus status) {
        boolean updated = intakeService.updateStatus(id, status);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntakeResponseDTO> updateIntake(
            @PathVariable String id,
            @RequestBody IntakeRequestDTO intakeRequestDTO) {
        return intakeService.updateIntake(id, intakeRequestDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submit(@PathVariable String id) {

        Boolean result = intakeService.submitIntake(id);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/import-pdf")
    public ResponseEntity<IntakeRequestDTO> importIntakeFromPdf(@RequestParam("file") MultipartFile file) {
        try {
            String text = pdfExtractionService.extractText(file);
            IntakeRequestDTO intake = openAiExtractionService.extractIntakeFromText(text);
            return ResponseEntity.ok(intake);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
