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

package io.jefrajames.sagademo.holiday.control.saga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import io.jefrajames.sagademo.holiday.boundary.HolidayBookRequest;
import io.jefrajames.sagademo.holiday.entity.Holiday;
import io.jefrajames.sagademo.holiday.entity.HolidayCategory;
import io.jefrajames.sagademo.holiday.entity.HolidayStatus;
import io.jefrajames.sagademo.trip.api.messaging.commands.BookTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.CancelTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.ConfirmTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.replies.TransportType;
import lombok.Data;

/**
 * This class stores all data needed to run the SAGA.
 * 
 */
@Data
public class HolidayBookSagaData {

  // Input data
  private Long customerId;
  private String departure;
  private String destination;
  private LocalDate departureDate;
  private LocalDate returnDate;
  private HolidayCategory category;
  private Integer peopleCount;
  private String requestedURI;

  // OutputData provided by processing
  private Long holidayId;
  private HolidayStatus holidayStatus;
  private BigDecimal pricing;
  private TransportType transportType;
  private Long tripId;
  private String businessError;
  private LocalTime returnTime;
  private LocalTime departureTime;
  

  public HolidayBookSagaData() {
  }

  public HolidayBookSagaData(HolidayBookRequest bookRequest, String requestedURI) {

    this.customerId = bookRequest.getCustomerId();
    this.departure = bookRequest.getDeparture();
    this.destination = bookRequest.getDestination();
    this.departureDate = bookRequest.getDepartureDate();
    this.returnDate = bookRequest.getReturnDate();
    this.category = bookRequest.getCategory();
    this.peopleCount = bookRequest.getPeopleCount();
    this.holidayStatus = HolidayStatus.PENDING;
    this.requestedURI = requestedURI;

  }

  public BookTripCommand toBookTripCommand() {

    BookTripCommand command = new BookTripCommand();

    command.setCustomerId(this.customerId);
    command.setDeparture(this.departure);
    command.setDepartureDate(this.departureDate);
    command.setReturnDate(this.returnDate);
    command.setDestination(this.destination);
    command.setSeatCount(this.peopleCount);
    command.setFirstClass(this.category == HolidayCategory.LUXURY);

    return command;
  }

  public CancelTripCommand toCancelTripCommand() {
    return new CancelTripCommand(this.tripId);
  }

  public ConfirmTripCommand toConfirmTripCommand() {
    return new ConfirmTripCommand(this.tripId);
  }

  public Holiday toEntity() {

    Holiday holiday = new Holiday();

    // Copy of input data
    holiday.customerId = this.customerId;
    holiday.destination = this.destination;
    holiday.departure = this.departure;
    holiday.category = this.category;
    holiday.departureDate = this.departureDate;
    holiday.returnDate = this.returnDate;
    holiday.category = this.category;
    holiday.peopleCount = this.peopleCount;

    // Other data
    holiday.status = this.holidayStatus;
    holiday.bookedAt = LocalDateTime.now();

    return holiday;
  }

}