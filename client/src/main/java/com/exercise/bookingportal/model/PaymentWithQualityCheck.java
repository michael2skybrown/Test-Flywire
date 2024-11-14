package com.exercise.bookingportal.model;


import lombok.Data;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentWithQualityCheck {
    private String reference;
    private double amount;
    private double amountWithFees;
    private double amountReceived;
    private String qualityCheck;
    private boolean overPayment;
    private boolean underPayment;
}
