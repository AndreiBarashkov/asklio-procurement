package com.asklio.procurement.repository;

import com.asklio.procurement.entity.Intake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntakeRepository extends JpaRepository<Intake, String> {
    // Add custom queries
}
