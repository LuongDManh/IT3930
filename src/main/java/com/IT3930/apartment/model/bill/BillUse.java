package com.IT3930.apartment.model.bill;

import jakarta.persistence.*;
import java.time.YearMonth;

@Entity
@Table(name = "bill_use")
public class BillUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_item_id", nullable = false)
    private BillItem billItem;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private YearMonth month;

    public BillUse() {
    }

    public BillUse(Bill bill, BillItem billItem, Double quantity, YearMonth month) {
        this.bill = bill;
        this.billItem = billItem;
        this.quantity = quantity;
        this.month = month;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public BillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }
}
