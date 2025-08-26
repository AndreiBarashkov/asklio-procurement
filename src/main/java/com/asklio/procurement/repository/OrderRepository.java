package com.asklio.procurement.repository;

import com.asklio.procurement.entity.ProcurementOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<ProcurementOrder, UUID> {
    // Add custom queries
}
