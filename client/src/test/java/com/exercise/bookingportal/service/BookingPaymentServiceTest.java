package com.exercise.bookingportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.exercise.bookingportal.controller.dto.BookingResponse;
import com.exercise.bookingportal.model.Booking;
import com.exercise.bookingportal.model.PaymentWithQualityCheck;
import com.exercise.bookingportal.model.util.BookingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

class BookingPaymentServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookingPaymentService bookingPaymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchBookings() {

        Booking booking = Booking.builder()
                .reference("ref123")
                .amount(5000)
                .amountReceived(5000)
                .currencyFrom("USD")
                .email("valid@example.com")
                .school("School")
                .studentId(123456)
                .build();

        BookingResponse mockResponse = BookingResponse.builder().bookings(Arrays.asList(booking)).build();

        when(restTemplate.getForObject(BookingUtils.BOOKING_PORTAL_API_URL, BookingResponse.class))
                .thenReturn(mockResponse);

        List<Booking> bookings = bookingPaymentService.fetchAllBookings();

        assertEquals(1, bookings.size());
        assertEquals("ref123", bookings.get(0).getReference());
    }

    @Test
    void testProcessBookings() {

        Booking booking1 = Booking.builder()
                .reference("ref1")
                .amount(500)
                .amountReceived(500)
                .currencyFrom("USD")
                .email("valid@example.com")
                .school("School")
                .studentId(123456)
                .build();

        Booking booking2 = Booking.builder()
                .reference("ref2")
                .amount(2000)
                .amountReceived(1500)
                .currencyFrom("USD")
                .email("duplicate@example.com")
                .school("School")
                .studentId(123456)
                .build();

        List<Booking> bookings = Arrays.asList(booking1, booking2);


        List<PaymentWithQualityCheck> processed = bookingPaymentService.processAllBookings(bookings);


        assertEquals(2, processed.size());

        PaymentWithQualityCheck firstProcessed = processed.get(0);
        assertEquals(525, firstProcessed.getAmountWithFees(), 0.01);
        assertTrue(firstProcessed.getQualityCheck().isEmpty());

        PaymentWithQualityCheck secondProcessed = processed.get(1);
        assertEquals("DuplicatedPayment", secondProcessed.getQualityCheck());
        assertTrue(secondProcessed.isUnderPayment());
        assertFalse(secondProcessed.isOverPayment());
    }


}
