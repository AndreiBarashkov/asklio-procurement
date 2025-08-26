package com.asklio.procurement.service;

import com.asklio.procurement.dto.IntakeRequestDTO;
import com.asklio.procurement.dto.IntakeResponseDTO;
import com.asklio.procurement.dto.OrderRequestDTO;
import com.asklio.procurement.dto.OrderResponseDTO;
import com.asklio.procurement.entity.Intake;
import com.asklio.procurement.entity.IntakeStatus;
import com.asklio.procurement.entity.ProcurementOrder;
import com.asklio.procurement.repository.IntakeRepository;
import com.asklio.procurement.repository.OrderRepository;
import jakarta.transaction.Transactional;
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
    private OrderRepository orderRepository;

    @Autowired
    public IntakeServiceImpl(IntakeRepository intakeRepository, OrderRepository orderRepository) {
        this.intakeRepository = intakeRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public IntakeResponseDTO createIntake(IntakeRequestDTO intakeRequestDTO) {
        Intake intake = Intake.builder()
                .id(UUID.randomUUID())
                .requestorName(intakeRequestDTO.requestorName())
                .title(intakeRequestDTO.title())
                .vendorName(intakeRequestDTO.vendorName())
                .vatId(intakeRequestDTO.vatId())
                .commodityGroup(intakeRequestDTO.commodityGroup())
                .department(intakeRequestDTO.department())
                .totalCost(calculateIntakeTotalPrice(intakeRequestDTO))
                .status(IntakeStatus.Open)
                .build();

        Intake saved = intakeRepository.save(intake);

        List<ProcurementOrder> procurementOrders = intakeRequestDTO.orders().stream().map(orderDTO ->
                ProcurementOrder.builder()
                        .id(UUID.randomUUID())
                        .positionDescription(orderDTO.description())
                        .unitPrice(orderDTO.unitPrice())
                        .amount(orderDTO.amount())
                        .unit(orderDTO.unit())
                        .totalPrice(orderDTO.amount().multiply(orderDTO.unitPrice()))
                        .intake(saved)
                        .build()
        ).collect(Collectors.toList());

        saved.setOrders(procurementOrders);
        intakeRepository.save(saved);

        return toResponse(saved);
    }

    @Override
    @Transactional
    public Optional<IntakeResponseDTO> updateIntake(String id, IntakeRequestDTO intakeRequestDTO) {
        Optional<Intake> optionalIntake = intakeRepository.findById(UUID.fromString(id));

        if (optionalIntake.isEmpty()) {
            return Optional.empty();
        }

        Intake intake = optionalIntake.get();

        // Update intake fields
        intake.setRequestorName(intakeRequestDTO.requestorName());
        intake.setTitle(intakeRequestDTO.title());
        intake.setVendorName(intakeRequestDTO.vendorName());
        intake.setVatId(intakeRequestDTO.vatId());
        intake.setCommodityGroup(intakeRequestDTO.commodityGroup());
        intake.setDepartment(intakeRequestDTO.department());

        // Remove existing orders
        intake.getOrders().clear(); // TODO update instead of recreating?

        // Add new orders
        List<ProcurementOrder> newOrders = intakeRequestDTO.orders().stream().map(orderDTO ->
                ProcurementOrder.builder()
                        .id(UUID.randomUUID())
                        .positionDescription(orderDTO.description())
                        .unitPrice(orderDTO.unitPrice())
                        .amount(orderDTO.amount())
                        .unit(orderDTO.unit())
                        .totalPrice(orderDTO.amount().multiply(orderDTO.unitPrice()))
                        .intake(intake)
                        .build()
        ).toList();

        intake.getOrders().addAll(newOrders);

        // Recalculate total cost
        BigDecimal totalCost = newOrders.stream()
                .map(ProcurementOrder::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        intake.setTotalCost(totalCost);

        Intake updated = intakeRepository.save(intake);
        return Optional.of(toResponse(updated));
    }

    @Override
    public Optional<IntakeResponseDTO> getIntakeById(String id) {
        return intakeRepository.findById(UUID.fromString(id)).map(this::toResponse);
    }

    @Override
    public List<IntakeResponseDTO> getAllIntakes() {
        return intakeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateStatus(String id, IntakeStatus newStatus) {
        Optional<Intake> optionalIntake = intakeRepository.findById(UUID.fromString(id));
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

    // Helper to map Intake → IntakeResponseDTO
    private IntakeResponseDTO toResponse(Intake intake) {
        List<OrderResponseDTO> orders = intake.getOrders().stream().map(procurementOrder ->
                new OrderResponseDTO(
                        procurementOrder.getId().toString(),
                        procurementOrder.getDescription(),
                        procurementOrder.getUnitPrice(),
                        procurementOrder.getAmount(),
                        procurementOrder.getUnit(),
                        procurementOrder.getTotalPrice())
        ).toList();

        return new IntakeResponseDTO(
                intake.getId().toString(),
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
