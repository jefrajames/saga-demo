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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

/**
 * This class stores the association between a SAGA instance and
 * a CompetableFuture JAX-RS Response.
 * 
 * It is written by HolidayResource and read by HolidayEventConsumer.
 * 
 */
@ApplicationScoped
public class SagaToCompletableFuture {

    private Map<String, CompletableFuture<Response>> map = new ConcurrentHashMap<>();

    public void put(String sagaId, CompletableFuture<Response> cfr) {
        map.put(sagaId, cfr);
    }

    public CompletableFuture<Response> getAndRemove(String sagaId) {

        CompletableFuture<Response> completableResponse = map.get(sagaId);
           
        if (completableResponse!=null)
            map.remove(sagaId);
        
        return completableResponse;
    }

}

