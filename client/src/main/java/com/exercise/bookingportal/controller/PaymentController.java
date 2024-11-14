package com.exercise.bookingportal.controller;

import com.exercise.bookingportal.model.Booking;
import com.exercise.bookingportal.model.PaymentWithQualityCheck;
import com.exercise.bookingportal.service.BookingPaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class PaymentController {
    private final BookingPaymentService bookingPaymentService;

    public PaymentController(BookingPaymentService bookingPaymentService) {
        this.bookingPaymentService = bookingPaymentService;
    }


    @GetMapping("/payments_with_quality_check")
    public List<PaymentWithQualityCheck> getPaymentsWithQualityCheck() {
        List<Booking> bookings = bookingPaymentService.fetchAllBookings();
        return bookingPaymentService.processAllBookings(bookings);
    }
}
