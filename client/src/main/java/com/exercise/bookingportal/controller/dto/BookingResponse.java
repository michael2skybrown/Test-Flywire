package com.exercise.bookingportal.controller.dto;

import com.exercise.bookingportal.model.Booking;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class BookingResponse {
    private List<Booking> bookings;
}
