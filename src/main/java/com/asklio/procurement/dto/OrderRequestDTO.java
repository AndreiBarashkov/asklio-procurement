package com.asklio.procurement.dto;

import java.math.BigDecimal;

public record OrderRequestDTO(String description,
                              BigDecimal unitPrice,
                              BigDecimal amount,
                              String unit) {
}
