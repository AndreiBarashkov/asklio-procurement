package com.asklio.procurement.repository;

import com.asklio.procurement.entity.Intake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IntakeRepository extends JpaRepository<Intake, UUID> {
    // Add custom queries
}
