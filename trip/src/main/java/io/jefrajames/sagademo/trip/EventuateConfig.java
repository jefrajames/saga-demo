package io.jefrajames.sagademo.trip;

import javax.inject.Singleton;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.jefrajames.sagademo.trip.control.TripCommandHandler;

/**
 * This class declares the command handlers for a SAGA.
 * 
 */
@Singleton
public class EventuateConfig {


  @Singleton
  public CommandDispatcher tripCommandDispatcher(TripCommandHandler target,
      SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {

    return sagaCommandDispatcherFactory.make("tripCommandDispatcher", target.commandHandlerDefinitions());
  }

}
