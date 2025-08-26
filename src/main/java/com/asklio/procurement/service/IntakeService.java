package com.asklio.procurement.service;

import com.asklio.procurement.dto.IntakeRequestDTO;
import com.asklio.procurement.dto.IntakeResponseDTO;
import com.asklio.procurement.entity.IntakeStatus;

import java.util.List;
import java.util.Optional;

public interface IntakeService {
    IntakeResponseDTO createIntake(IntakeRequestDTO intakeDTO);
    Optional<IntakeResponseDTO> getIntakeById(String id);
    List<IntakeResponseDTO> getAllIntakes();
    boolean updateStatus(String id, IntakeStatus newStatus);
}
