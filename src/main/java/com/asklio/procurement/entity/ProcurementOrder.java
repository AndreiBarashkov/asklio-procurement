package com.asklio.procurement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "procurement_order")
public class ProcurementOrder {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    private String description;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String unit;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "intake_id", nullable = false)
    private Intake intake;

    // ==== Builder ====
    public static class Builder {
        private final ProcurementOrder procurementOrder;

        public Builder() {
            this.procurementOrder = new ProcurementOrder();
        }

        public ProcurementOrder.Builder id(UUID id) {
            procurementOrder.id = id;
            return this;
        }

        public Builder positionDescription(String description) {
            procurementOrder.description = description;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            procurementOrder.unitPrice = unitPrice;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            procurementOrder.amount = amount;
            return this;
        }

        public Builder unit(String unit) {
            procurementOrder.unit = unit;
            return this;
        }

        public Builder totalPrice(BigDecimal totalPrice) {
            procurementOrder.totalPrice = totalPrice;
            return this;
        }

        public Builder intake(Intake intake) {
            procurementOrder.intake = intake;
            return this;
        }

        public ProcurementOrder build() {
            return procurementOrder;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public void setOrder_id(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Intake getIntake() {
        return intake;
    }

    public void setIntake(Intake intake) {
        this.intake = intake;
    }
}
