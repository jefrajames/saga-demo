// Copyright 2022 jefrajames
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.jefrajames.sagademo.trip.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.jefrajames.sagademo.trip.api.messaging.commands.BookTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.replies.TransportType;
import io.jefrajames.sagademo.trip.api.messaging.replies.TripBooked;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor
public class Trip extends PanacheEntity {

    @Column(name = "customer_id")
    @JsonProperty("customer_id")
    public Long customerId;

    @JsonProperty("booked_at")
    @Column(name="booked_at")
    public LocalDateTime bookedAt;

    @Column(name="departure")
    public String departure;

    @Column(name="destination")
    public String destination;

    @JsonProperty("seat_count")
    @Column(name="seat_count")
    public Integer seatCount;

    @JsonProperty("departure_date")
    @Column(name="departure_date")
    public LocalDate departureDate;

    @JsonProperty("departure_time")
    @Column(name="departure_time")
    public LocalTime departureTime;

    @JsonProperty("return_date")
    @Column(name="return_date")
    public LocalDate returnDate;

    @JsonProperty("return_time")
    @Column(name="return_time")
    public LocalTime returnTime;

    @JsonProperty("first_class")
    @Column(name="first_class")
    public Boolean firstClass;

    @Column(name="pricing", precision=10, scale = 2)
    public BigDecimal pricing;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    public TripStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name="transport_type")
    @JsonProperty("transport_type")
    public TransportType transportType;

    @Column(name="business_error")
    @JsonProperty("business_error")
    public String businessError;

    public Trip(BookTripCommand cmd) {
        this.customerId = cmd.getCustomerId();
        this.departure = cmd.getDeparture();
        this.destination = cmd.getDestination();
        this.departureDate = cmd.getDepartureDate();
        this.returnDate = cmd.getReturnDate();
        this.seatCount = cmd.getSeatCount();
        this.firstClass = cmd.getFirstClass();
        this.bookedAt = LocalDateTime.now();
    }

    public TripBooked toReply() {
        TripBooked tripBooked = new TripBooked();

        tripBooked.setPricing(this.pricing);
        tripBooked.setTransportType(this.transportType);
        tripBooked.setDepartureTime(this.departureTime);
        tripBooked.setReturnTime(this.returnTime);
        tripBooked.setTripId(this.id);

        return tripBooked;
    }

}
