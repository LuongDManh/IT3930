package com.IT3930.apartment.dto;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;

public class BillGenerationRequestDTO {
    private Long apartmentId;
    private YearMonth month;
    private List<ItemUsageDTO> usages;
    private List<Long> billItemIds;
    private LocalDate dueDate;

    public BillGenerationRequestDTO() {
    }

    public BillGenerationRequestDTO(Long apartmentId, YearMonth month, List<ItemUsageDTO> usages) {
        this.apartmentId = apartmentId;
        this.month = month;
        this.usages = usages;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public List<ItemUsageDTO> getUsages() {
        return usages;
    }

    public void setUsages(List<ItemUsageDTO> usages) {
        this.usages = usages;
    }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public List<Long> getBillItemIds() { return billItemIds; }
    public void setBillItemIds(List<Long> billItemIds) { this.billItemIds = billItemIds; }
}
