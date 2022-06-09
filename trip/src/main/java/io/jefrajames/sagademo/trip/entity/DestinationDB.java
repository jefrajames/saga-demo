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

package io.jefrajames.sagademo.trip.entity;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import io.jefrajames.sagademo.trip.api.messaging.replies.TransportType;

@ApplicationScoped
public class DestinationDB {

    Map<String, Destination> DESTINATIONS  = new HashMap<>() {{
        put("Barcelona", new Destination("Barcelona", new BigDecimal(107.50), TransportType.PLANE, LocalTime.of(20,30), LocalTime.of(22,30)));
        put("Budapest", new Destination("Budapest", new BigDecimal(201.90), TransportType.TRAIN, LocalTime.of(21,30), LocalTime.of(3,30)));
        put("Dublin", new Destination("Dublin", new BigDecimal(310.55), TransportType.PLANE, LocalTime.of(7,30), LocalTime.of(11,30)));
        put("London", new Destination("London", new BigDecimal(402.25), TransportType.BOAT, LocalTime.of(5,15), LocalTime.of(2,20)));
    }};

    public Destination lookup(String destination) {
        return DESTINATIONS.get(destination);
    }

}