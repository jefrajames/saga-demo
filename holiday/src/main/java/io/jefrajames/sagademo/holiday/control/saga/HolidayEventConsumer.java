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

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.core.Response;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.jefrajames.sagademo.holiday.entity.Holiday;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

/**
 * This class is in charge to complete the JAX-RS response when
 * the SAGA is finished on reception of HolidayBookSagaFinishedEvent.
 * 
 * HolidayBookSagaFinishedEvent is produced by HolidayBookSaga.
 * 
 */
@Log
@AllArgsConstructor
// Not a CDI bean: Instanciated by Eventuate configuration
public class HolidayEventConsumer {

  private SagaToCompletableFuture sagaToCompletableFuture;

  private URI resourceUri(long holidayId, String requestedURI) {
    return URI.create(String.format("%s/%d", requestedURI, holidayId));
  }

  public DomainEventHandlers domainEventHandlers() {

    log.info("DEMO configuring DomainEventHandlers on " + Holiday.class.getName());

    return DomainEventHandlersBuilder
        .forAggregateType(Holiday.class.getName())
        .onEvent(HolidayBookSagaFinishedEvent.class, this::sagaFinished)
        .build();
  }

  private void sagaFinished(DomainEventEnvelope<HolidayBookSagaFinishedEvent> de) {
    
    log.info("DEMO received event " + de);
    log.info("DEMO de.getAggregateType()=" + de.getAggregateType());
    log.info("DEMO de.getAggregateId()=" + de.getAggregateId());

    HolidayBookSagaFinishedEvent event = de.getEvent();

    CompletableFuture<Response> completableResponse = sagaToCompletableFuture.getAndRemove(event.getSagaId());
    log.info("DEMO completableReponse=" + completableResponse);

    Holiday holiday = Holiday.findById(event.getHolidayId());

    URI uri = resourceUri(event.getHolidayId(), event.getRequestedURI());

    Response rep = Response.created(uri).entity(holiday).build();

    completableResponse.complete(rep);
  }

}
