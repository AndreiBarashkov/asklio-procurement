package com.asklio.procurement.controller;

import com.asklio.procurement.dto.IntakeRequestDTO;
import com.asklio.procurement.dto.IntakeResponseDTO;
import com.asklio.procurement.entity.Intake;
import com.asklio.procurement.entity.IntakeStatus;
import com.asklio.procurement.service.IntakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/intake")
public class IntakeController {

    private final IntakeService intakeService;

    @Autowired
    public IntakeController(IntakeService intakeService) {
        this.intakeService = intakeService;
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

    @PostMapping
    public ResponseEntity<String> submit() {
        return ResponseEntity.ok("Hello, World!"); // TODO make intake status `Closed` if it is valid. Else return an error with description
    }
}
