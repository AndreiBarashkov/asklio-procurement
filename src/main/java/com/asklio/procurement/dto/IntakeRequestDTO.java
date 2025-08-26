package com.asklio.procurement.dto;

import java.util.List;

public record IntakeRequestDTO(String requestorName,
                               String title,
                               String vendorName,
                               String vatId,
                               String commodityGroup,
                               String department,
                               List<OrderRequestDTO> orders) { }
