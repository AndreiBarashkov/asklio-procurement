package com.asklio.procurement.service;

import com.asklio.procurement.dto.IntakeRequestDTO;
import com.asklio.procurement.dto.IntakeResponseDTO;
import com.asklio.procurement.dto.OrderRequestDTO;
import com.asklio.procurement.dto.OrderResponseDTO;
import com.asklio.procurement.entity.Intake;
import com.asklio.procurement.entity.IntakeStatus;
import com.asklio.procurement.entity.Order;
import com.asklio.procurement.repository.IntakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IntakeServiceImpl implements IntakeService {
    // TODO: implement intake processing here
    // TODO: use repository to save data in postgresql instance
    private IntakeRepository intakeRepository;

    @Autowired
    public IntakeServiceImpl(IntakeRepository intakeRepository) {
        this.intakeRepository = intakeRepository;
    }

    @Override
    public IntakeResponseDTO createIntake(IntakeRequestDTO intakeRequestDTO) {
        Intake intake = Intake.builder()
                .id(UUID.randomUUID().toString())
                .requestorName(intakeRequestDTO.requestorName())
                .title(intakeRequestDTO.title())
                .vendorName(intakeRequestDTO.vendorName())
                .vatId(intakeRequestDTO.vatId())
                .commodityGroup(intakeRequestDTO.commodityGroup())
                .department(intakeRequestDTO.department())
                .totalCost(calculateIntakeTotalPrice(intakeRequestDTO))
                .status(IntakeStatus.Open)
                .build();

        List<Order> orders = intakeRequestDTO.orders().stream().map(orderDTO ->
                Order.builder()
                        .positionDescription(orderDTO.description())
                        .unitPrice(orderDTO.unitPrice())
                        .amount(orderDTO.amount())
                        .unit(orderDTO.unit())
                        .totalPrice(calculateOrderTotalPrice(orderDTO))
                        .intake(intake)
                        .build()
        ).collect(Collectors.toList());

        intake.setOrders(orders);
        Intake saved = intakeRepository.save(intake);

        return toResponse(saved);
    }

    @Override
    public Optional<IntakeResponseDTO> getIntakeById(String id) {
        return intakeRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<IntakeResponseDTO> getAllIntakes() {
        return intakeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateStatus(String id, IntakeStatus newStatus) {
        Optional<Intake> optionalIntake = intakeRepository.findById(id);
        if (optionalIntake.isPresent()) {
            Intake intake = optionalIntake.get();
            intake.setStatus(newStatus);
            intakeRepository.save(intake);
            return true;
        }
        return false;
    }

    private BigDecimal calculateIntakeTotalPrice(IntakeRequestDTO dto) {
        return dto.orders().stream()
                .map(order -> order.amount().multiply(order.unitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateOrderTotalPrice(OrderRequestDTO orderDTO) {
        return orderDTO.amount().multiply(orderDTO.totalPrice());
    }

    // Helper to map Intake → IntakeResponseDTO
    private IntakeResponseDTO toResponse(Intake intake) {
        List<OrderResponseDTO> orders = intake.getOrders().stream().map(order ->
                new OrderResponseDTO(
                        order.getOrder_id(),
                        order.getDescription(),
                        order.getUnitPrice(),
                        order.getAmount(),
                        order.getUnit(),
                        order.getTotalPrice())
        ).toList();

        return new IntakeResponseDTO(
                intake.getId(),
                intake.getRequestorName(),
                intake.getTitle(),
                intake.getVendorName(),
                intake.getVatId(),
                intake.getCommodityGroup(),
                intake.getDepartment(),
                intake.getTotalCost(),
                intake.getStatus(),
                orders
        );
    }
}
