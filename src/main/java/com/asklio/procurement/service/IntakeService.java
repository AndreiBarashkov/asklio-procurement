package com.asklio.procurement.service;

import com.asklio.procurement.dto.IntakeRequestDTO;
import com.asklio.procurement.dto.IntakeResponseDTO;
import com.asklio.procurement.entity.IntakeStatus;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface IntakeService {
    IntakeResponseDTO createIntake(IntakeRequestDTO intakeDTO);

    @Transactional
    Boolean submitIntake(String id);

    Optional<IntakeResponseDTO> getIntakeById(String id);
    Optional<IntakeResponseDTO> updateIntake(String id, IntakeRequestDTO intakeRequestDTO);
    List<IntakeResponseDTO> getAllIntakes();
    boolean updateStatus(String id, IntakeStatus newStatus);
}
