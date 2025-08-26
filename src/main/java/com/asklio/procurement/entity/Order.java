package com.asklio.procurement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String order_id;
    private String description;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String unit;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "id")
    private Intake intake;

    // ==== Builder ====
    public static class Builder {
        private final Order order;

        public Builder() {
            this.order = new Order();
        }

        public Builder positionDescription(String description) {
            order.description = description;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            order.unitPrice = unitPrice;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            order.amount = amount;
            return this;
        }

        public Builder unit(String unit) {
            order.unit = unit;
            return this;
        }

        public Builder totalPrice(BigDecimal totalPrice) {
            order.totalPrice = totalPrice;
            return this;
        }

        public Builder intake(Intake intake) {
            order.intake = intake;
            return this;
        }

        public Order build() {
            return order;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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
