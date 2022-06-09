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
package io.jefrajames.sagademo.holiday;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.jefrajames.sagademo.holiday.control.HolidayService;
import io.jefrajames.sagademo.holiday.control.saga.HolidayEventConsumer;
import io.jefrajames.sagademo.holiday.control.saga.SagaToCompletableFuture;

@Singleton
public class EventuateMessagingConfig {

  @Inject
  SagaToCompletableFuture sagaToCompletableFuture;

  @Singleton
  public HolidayEventConsumer holidayEventConsumer(HolidayService orderService) {
    return new HolidayEventConsumer(sagaToCompletableFuture);
  }

  @Singleton
  public DomainEventDispatcher domainEventDispatcher(HolidayEventConsumer holidayEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("holidayServiceEvents", holidayEventConsumer.domainEventHandlers());
  }
    
}