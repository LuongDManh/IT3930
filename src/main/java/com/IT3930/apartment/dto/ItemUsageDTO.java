package com.IT3930.apartment.dto;

public class ItemUsageDTO {
    private Long billItemId;
    private Double quantity;

    public ItemUsageDTO() {
    }

    public ItemUsageDTO(Long billItemId, Double quantity) {
        this.billItemId = billItemId;
        this.quantity = quantity;
    }

    public Long getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(Long billItemId) {
        this.billItemId = billItemId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
