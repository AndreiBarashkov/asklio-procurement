package com.asklio.procurement.repository;

import com.asklio.procurement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // Add custom queries
}
