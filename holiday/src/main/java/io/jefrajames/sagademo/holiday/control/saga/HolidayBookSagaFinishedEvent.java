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

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class indicated a SAGA termination.
 * 
 * It is published by HolidayBookSaga and 
 * consumed by HolidayEventConsumer.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayBookSagaFinishedEvent implements DomainEvent {
    private String sagaId;
    private Long holidayId;
    private String requestedURI;
}