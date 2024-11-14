package com.exercise.bookingportal.service;


import com.exercise.bookingportal.controller.dto.BookingResponse;
import com.exercise.bookingportal.model.Booking;
import com.exercise.bookingportal.model.PaymentWithQualityCheck;
import com.exercise.bookingportal.model.util.BookingUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingPaymentService {
    private final RestTemplate restTemplate;

    public BookingPaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Booking> fetchAllBookings() {
        BookingResponse response = restTemplate.getForObject(BookingUtils.BOOKING_PORTAL_API_URL, BookingResponse.class);
        return response.getBookings();
    }

    public List<PaymentWithQualityCheck> processAllBookings(List<Booking> bookings) {
        Set<String> processedStudentReference = new HashSet<>();

        return bookings.stream()
                .map(booking -> verifyBooking(booking, processedStudentReference))
                .collect(Collectors.toList());
    }

    private PaymentWithQualityCheck verifyBooking(Booking booking, Set<String> processedStudentReference) {
        double amountWithFees = calculateAmountWithFees(booking.getAmount());
        String qualityCheck = determineQualityCheck(booking, amountWithFees, processedStudentReference);
        boolean overPayment = booking.getAmountReceived() > amountWithFees;
        boolean underPayment = booking.getAmountReceived() < amountWithFees;

        return PaymentWithQualityCheck.builder()
                .reference(booking.getReference())
                .amount(booking.getAmount())
                .amountWithFees(amountWithFees)
                .amountReceived(booking.getAmountReceived())
                .qualityCheck(qualityCheck)
                .overPayment(overPayment)
                .underPayment(underPayment)
                .build();
    }

    private double calculateAmountWithFees(double amount) {
        if (amount <= 1000) {
            return amount * 1.05;
        } else if (amount <= 10000) {
            return amount * 1.03;
        } else {
            return amount * 1.02;
        }
    }

    private String determineQualityCheck(Booking booking, double amountWithFees, Set<String> processedStudentReference) {
        StringBuilder qualityCheck = new StringBuilder();

        if (!isValidEmail(booking.getEmail())) {
            qualityCheck.append("InvalidEmail,");
        }

        if (isDuplicatePayment(booking, processedStudentReference)) {
            qualityCheck.append("DuplicatedPayment,");
        }
        if (amountWithFees > 1000000) {
            qualityCheck.append("AmountThreshold,");
        }
        return qualityCheck.length() > 0 ? qualityCheck.substring(0, qualityCheck.length() - 1) : "";
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isDuplicatePayment(Booking booking, Set<String> processedStudentReference) {
        if (processedStudentReference.contains(booking.getReference())) {
            return true;
        } else {
            processedStudentReference.add(booking.getReference());
            return false;
        }
    }
}
