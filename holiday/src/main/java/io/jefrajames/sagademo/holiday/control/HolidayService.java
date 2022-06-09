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

package io.jefrajames.sagademo.holiday.control;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.jefrajames.sagademo.holiday.control.saga.HolidayBookSagaData;
import io.jefrajames.sagademo.holiday.entity.Holiday;
import io.jefrajames.sagademo.holiday.entity.HolidayStatus;
import io.jefrajames.sagademo.trip.api.messaging.commands.BookTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.CancelTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.ConfirmTripCommand;
import lombok.extern.java.Log;

/**
 * This class implements business logic called by the SAGA
 * 
 */
@ApplicationScoped
@Log
public class HolidayService {

    private static final BigDecimal MAX_PRICING = new BigDecimal(500.00);
    private static final long CUSTOMER_ID = 42;

    public void checkCustomer(HolidayBookSagaData data) {
        
        if (data.getCustomerId() != CUSTOMER_ID) {
            data.setBusinessError("Unknwown customer");
            throw new ValidationException("{\"Holiday error\": \"Unknown Customer\"}");
        }

    }

    public void checkPricing(HolidayBookSagaData data) {

        if (data.getPricing()!=null && data.getPricing().compareTo(MAX_PRICING) == 1) {
            data.setBusinessError("Max pricing exceeded");
            throw new ValidationException("{\"Holiday error\": \"Max Pricing Exceeded\"}");
        }

    }


    @Transactional
    public void updateHolidayWithTripBooked(HolidayBookSagaData data) {
        Holiday holiday = Holiday.findById(data.getHolidayId());
        
        if ( holiday==null ) {
            log.warning("Holiday with id not found " + data.getHolidayId());
            return;
        }

        holiday.departureTime=data.getDepartureTime();
        holiday.returnTime = data.getReturnTime();
        holiday.pricing = data.getPricing();
        holiday.transportType = data.getTransportType();
        holiday.tripId = data.getTripId();
    }

    @Transactional
    public void updateHolidayWithBookTripFailed(HolidayBookSagaData data) {
    
        Holiday holiday = Holiday.findById(data.getHolidayId());
        
        if ( holiday==null ) {
            log.warning("Holiday with id not found " + data.getHolidayId());
            return;
        }

        holiday.businessError=data.getBusinessError();
        holiday.tripId=data.getTripId();       
    }

    @Transactional
    public void createHoliday(HolidayBookSagaData data) {
        Holiday holiday = data.toEntity();
        Holiday.persist(holiday);
        data.setHolidayId(holiday.id);
    }

    @Transactional
    public void approve(Long holidayId) {

        Holiday holiday = Holiday.findById(holidayId);
        if (holiday == null) {
            log.warning("Unable to approve: Id not found " + holidayId);
            return;
        }

        holiday.status = HolidayStatus.ACCEPTED;
        
        holiday.bookResponseTime = ChronoUnit.MILLIS.between(holiday.bookedAt,
                LocalDateTime.now());
    }

    @Transactional
    public void reject(HolidayBookSagaData data) {

        Holiday holiday = Holiday.findById(data.getHolidayId());
        if (holiday == null) {
            log.warning("Unable to reject: Id not found " + data.getHolidayId());
            return;
        }

        holiday.status = HolidayStatus.REJECTED; 
        holiday.businessError = data.getBusinessError();

        holiday.bookResponseTime = ChronoUnit.MILLIS.between(holiday.bookedAt,
                LocalDateTime.now());
    }


    @Transactional
    public CommandWithDestination bookTrip(BookTripCommand command) {

        CommandWithDestination cwd = send(command)
                .to("tripService")
                .build();

        return cwd;
    }

    @Transactional
    public CommandWithDestination confirmTrip(ConfirmTripCommand command) {

        CommandWithDestination cwd = send(command)
                .to("tripService")
                .build();

        return cwd;
    }

    @Transactional
    public CommandWithDestination cancelTrip(CancelTripCommand command) {

        CommandWithDestination cwd = send(command)
                .to("tripService")
                .build();

        return cwd;
    }

}