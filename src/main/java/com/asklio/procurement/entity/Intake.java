package com.asklio.procurement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
public class Intake {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    private String requestorName;
    private String title;
    private String vendorName;
    private String vatId;
    private String commodityGroup; // TODO: check if it needs to be an enum (yes, but not enum. It should be saved in DB according to Readme)
    @OneToMany(mappedBy = "intake", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcurementOrder> procurementOrders;
    private BigDecimal totalCost;
    private String department; // TODO: check (this is department of requestor, so probably String)
    @Enumerated(EnumType.STRING)
    private IntakeStatus status;


    // ==== Builder Pattern ====
    public static class Builder {
        private final Intake intake;

        public Builder() {
            this.intake = new Intake();
        }

        public Builder id(UUID id) {
            intake.id = id;
            return this;
        }

        public Builder requestorName(String requestorName) {
            intake.requestorName = requestorName;
            return this;
        }

        public Builder title(String title) {
            intake.title = title;
            return this;
        }

        public Builder vendorName(String vendorName) {
            intake.vendorName = vendorName;
            return this;
        }

        public Builder vatId(String vatId) {
            intake.vatId = vatId;
            return this;
        }

        public Builder commodityGroup(String commodityGroup) {
            intake.commodityGroup = commodityGroup;
            return this;
        }

        public Builder department(String department) {
            intake.department = department;
            return this;
        }

        public Builder totalCost(BigDecimal totalCost) {
            intake.totalCost = totalCost;
            return this;
        }

        public Builder status(IntakeStatus status) {
            intake.status = status;
            return this;
        }

        public Builder orders(List<ProcurementOrder> procurementOrders) {
            intake.procurementOrders = procurementOrders;
            return this;
        }

        public Intake build() {
            return intake;
        }
    }

    public static Builder builder() {
        return new Builder();
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVatId() {
        return vatId;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

    public String getCommodityGroup() {
        return commodityGroup;
    }

    public void setCommodityGroup(String commodityGroup) {
        this.commodityGroup = commodityGroup;
    }

    public List<ProcurementOrder> getOrders() {
        return procurementOrders;
    }

    public void setOrders(List<ProcurementOrder> procurementOrders) {
        this.procurementOrders = procurementOrders;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public IntakeStatus getStatus() {
        return status;
    }

    public void setStatus(IntakeStatus status) {
        this.status = status;
    }
}
