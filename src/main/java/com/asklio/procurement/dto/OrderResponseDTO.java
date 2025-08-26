package com.asklio.procurement.dto;

import java.math.BigDecimal;

public record OrderResponseDTO(String id,
                               String description,
                               BigDecimal unitPrice,
                               BigDecimal amount,
                               String unit,
                               BigDecimal totalPrice) {
}
