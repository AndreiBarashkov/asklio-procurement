package com.asklio.procurement.dto;

import com.asklio.procurement.entity.IntakeStatus;

import java.math.BigDecimal;
import java.util.List;

public record IntakeResponseDTO(String id,
                         String requestorName,
                         String title,
                         String vendorName,
                         String vatId,
                         String commodityGroup,
                         String department,
                         BigDecimal totalCost,
                         IntakeStatus status,
                         List<OrderResponseDTO> orders
) { }
