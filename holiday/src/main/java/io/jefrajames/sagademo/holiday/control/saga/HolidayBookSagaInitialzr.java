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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.jefrajames.sagademo.holiday.boundary.HolidayBookRequest;
import lombok.extern.java.Log;

/**
 * Initialise a SAGA from a book request
 */
@Singleton
@Log
public class HolidayBookSagaInitialzr {

    @Inject
    SagaManager<HolidayBookSagaData> sagaManager;

    @Inject
    SagaToCompletableFuture sagaToCompletableFuture;

    @Transactional // This annotation avoids connection leak!
    public String bookHoliday(HolidayBookRequest request, String requestedURI) {

        HolidayBookSagaData data = new HolidayBookSagaData(request, requestedURI);

        log.info("DEMO creating SAGA instance ");
        SagaInstance sagaInstance = sagaManager.create(data);
        log.info("DEMO SAGA instance created: " + sagaInstance.getStateName());
        log.info("DEMO SAGA instance id: " + sagaInstance.getId());

        return sagaInstance.getId();
    }

}