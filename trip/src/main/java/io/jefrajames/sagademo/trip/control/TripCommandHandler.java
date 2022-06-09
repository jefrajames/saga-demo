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

package io.jefrajames.sagademo.trip.control;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.jefrajames.sagademo.trip.api.messaging.commands.BookTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.CancelTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.ConfirmTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.replies.BookTripFailed;
import io.jefrajames.sagademo.trip.api.messaging.replies.TripCanceled;
import io.jefrajames.sagademo.trip.api.messaging.replies.TripConfirmed;
import io.jefrajames.sagademo.trip.entity.Trip;
import lombok.extern.java.Log;

@Singleton
@Log
public class TripCommandHandler {

    @Inject
    TripService tripService;

    // Called by EventuateConfig
    public CommandHandlers commandHandlerDefinitions() {

        return SagaCommandHandlersBuilder
                .fromChannel("tripService")
                .onMessage(BookTripCommand.class, this::bookTrip)
                .onMessage(CancelTripCommand.class, this::cancelTrip)
                .onMessage(ConfirmTripCommand.class, this::confirmTrip)
                .build();
    }

    @Transactional
    public Message bookTrip(CommandMessage<BookTripCommand> cm) {

        BookTripCommand cmd = cm.getCommand();
        log.info("DEMO calling bookTrip");

        Trip trip = new Trip(cmd);

        tripService.book(trip);

        if (trip.businessError != null)
            return withFailure(new BookTripFailed(trip.id, trip.businessError));
        else
            return withSuccess(trip.toReply());
    }

    @Transactional
    public Message cancelTrip(CommandMessage<CancelTripCommand> cm) {

        CancelTripCommand cmd = cm.getCommand();
        log.info("DEMO calling cancelTrip" + cmd);

        tripService.cancel(cmd.getTripId());

        return withSuccess(new TripCanceled());
    }

    @Transactional
    public Message confirmTrip(CommandMessage<ConfirmTripCommand> cm) {

        ConfirmTripCommand cmd = cm.getCommand();
        log.info("DEMO calling confirmTrip" + cmd);

        tripService.confirm(cmd.getTripId());

        return withSuccess(new TripConfirmed());
    }

}
