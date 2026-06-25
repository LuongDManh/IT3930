package com.IT3930.apartment.dto;

import java.time.YearMonth;
import java.util.List;

public class BillGenerationRequestDTO {
    private Long apartmentId;
    private YearMonth month;
    private List<ItemUsageDTO> usages;

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
}
