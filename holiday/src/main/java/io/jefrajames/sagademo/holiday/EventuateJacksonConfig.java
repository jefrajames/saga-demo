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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.runtime.StartupEvent;

/**
 * Enables Eventuate Tram to serialize/deserialize java.tim classes.
 */

@ApplicationScoped
public class EventuateJacksonConfig {

    public void addJavaTimeModule(@Observes StartupEvent ev) {
        ObjectMapper om = io.eventuate.common.json.mapper.JSonMapper.objectMapper;
        om.registerModule(new JavaTimeModule());
    }

}
