package io.jefrajames.sagademo.holiday.control.saga;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import io.jefrajames.sagademo.holiday.control.HolidayService;
import io.jefrajames.sagademo.holiday.entity.Holiday;
import io.jefrajames.sagademo.trip.api.messaging.commands.BookTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.CancelTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.commands.ConfirmTripCommand;
import io.jefrajames.sagademo.trip.api.messaging.replies.BookTripFailed;
import io.jefrajames.sagademo.trip.api.messaging.replies.TripBooked;
import lombok.extern.java.Log;

/**
 * This class is in charge of defining and running the SAGA.
 * 
 */
@Singleton
@Log
public class HolidayBookSaga implements SimpleSaga<HolidayBookSagaData> {

  @Inject
  HolidayService holidayService;

  @Inject
  DomainEventPublisher domainEventPublisher;

  private SagaDefinition<HolidayBookSagaData> sagaDefinition = 
    step()
      .invokeLocal(this::create)
      .withCompensation(this::reject)
    .step()
      .invokeLocal(this::checkCustomer)
    .step()
      .invokeParticipant(this::bookTrip)
      .onReply(TripBooked.class, this::handleTripBooked)
      .onReply(BookTripFailed.class, this::handleBookTripFailed)
      .withCompensation(this::cancelTrip)
    .step()
      .invokeLocal(this::checkPricing)
    .step()
      .invokeParticipant(this::confirmTrip)
    .step()
      .invokeLocal(this::approve)
    .build();

  @Override
  public SagaDefinition<HolidayBookSagaData> getSagaDefinition() {
    return this.sagaDefinition;
  }

  private void create(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling create");
    holidayService.createHoliday(data);
  }

  private void reject(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling reject");
    holidayService.reject(data);
  }

  private void checkCustomer(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling checkCustomer");
    holidayService.checkCustomer(data);
  }

  private CommandWithDestination bookTrip(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling bookTrip");
    BookTripCommand command = data.toBookTripCommand();
    CommandWithDestination cwd = holidayService.bookTrip(command);
    return cwd;
  }

  private void handleTripBooked(HolidayBookSagaData data, TripBooked tripBooked) {
    log.info("DEMO running SAGA calling handleTripBooked");

    data.setPricing(tripBooked.getPricing());
    data.setTransportType(tripBooked.getTransportType());
    data.setDepartureTime(tripBooked.getDepartureTime());
    data.setReturnTime(tripBooked.getReturnTime());
    data.setTripId(tripBooked.getTripId());

    holidayService.updateHolidayWithTripBooked(data);
  }

  private void handleBookTripFailed(HolidayBookSagaData data, BookTripFailed bookTripFailed) {

    log.info("DEMO running SAGA calling handleBookTripFailed");

    data.setBusinessError(bookTripFailed.getBusinessError());
    data.setTripId(bookTripFailed.getTripId());

    holidayService.updateHolidayWithBookTripFailed(data);

  }

  private CommandWithDestination cancelTrip(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling cancelTrip");
    CancelTripCommand command = data.toCancelTripCommand();
    CommandWithDestination cwd = holidayService.cancelTrip(command);
    return cwd;
  }

  private void approve(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling approve");
    holidayService.approve(data.getHolidayId());
  }

  private CommandWithDestination confirmTrip(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling confirmTrip");
    ConfirmTripCommand command = data.toConfirmTripCommand();
    CommandWithDestination cwd = holidayService.confirmTrip(command);
    return cwd;
  }

  private void checkPricing(HolidayBookSagaData data) {
    log.info("DEMO running SAGA calling checkPricing");
    holidayService.checkPricing(data);
  }

  @Override
  public void onStarting(String sagaId, HolidayBookSagaData data) {
    log.info("DEMO calling onStarting, sagaId= " + sagaId);
  }

  @Override
  public void onSagaCompletedSuccessfully(String sagaId, HolidayBookSagaData data) {
    log.info("DEMO calling onSagaCompletedSuccessfully, sagaId= " + sagaId);

    HolidayBookSagaFinishedEvent evt = new HolidayBookSagaFinishedEvent(sagaId, data.getHolidayId(), data.getRequestedURI());
    domainEventPublisher.publish(Holiday.class, data.getHolidayId(), Collections.singletonList(evt));
    log.info("DEMO eventPublished= " + evt);
  }

  @Override
  public void onSagaRolledBack(String sagaId, HolidayBookSagaData data) {
    log.info("DEMO calling onSagaRolledBack, sagaId= " + sagaId);

    HolidayBookSagaFinishedEvent evt = new HolidayBookSagaFinishedEvent(sagaId, data.getHolidayId(), data.getRequestedURI());
    domainEventPublisher.publish(Holiday.class, data.getHolidayId(), Collections.singletonList(evt));
    log.info("DEMO eventPublished= " + evt);
  }

}
