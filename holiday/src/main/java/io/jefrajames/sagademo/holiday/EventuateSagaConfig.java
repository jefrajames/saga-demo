package io.jefrajames.sagademo.holiday;

import javax.inject.Singleton;

import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.sagas.common.SagaLockManager;
import io.eventuate.tram.sagas.orchestration.Saga;
import io.eventuate.tram.sagas.orchestration.SagaCommandProducer;
import io.eventuate.tram.sagas.orchestration.SagaInstanceRepository;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;
import io.jefrajames.sagademo.holiday.control.saga.HolidayBookSagaData;

@Singleton
public class EventuateSagaConfig {
  
  @Singleton
  public SagaManager<HolidayBookSagaData> bookHolidaySagaManager(Saga<HolidayBookSagaData> saga,
                                                                 SagaInstanceRepository sagaInstanceRepository,
                                                                 CommandProducer commandProducer,
                                                                 MessageConsumer messageConsumer,
                                                                 SagaLockManager sagaLockManager,
                                                                 SagaCommandProducer sagaCommandProducer) {

    return new SagaManagerImpl<>(saga,
            sagaInstanceRepository,
            commandProducer,
            messageConsumer,
            sagaLockManager,
            sagaCommandProducer);
  }  

}
