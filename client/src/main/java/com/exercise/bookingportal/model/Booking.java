package com.exercise.bookingportal.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Builder
@Value
public class Booking {
    private String reference;
    private double amount;
    private double amountReceived;
    private String countryFrom;
    private String senderFullName;
    private String senderAddress;
    private String school;
    private String currencyFrom;
    private long studentId;
    private String email;
}
