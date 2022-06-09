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

package io.jefrajames.sagademo.trip.boundary;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TripBookRequest {

    @JsonProperty("customer_id")
    @PositiveOrZero
    private Long customerId;

    @NotEmpty
    private String departure;

    @NotEmpty
    private String destination;

    @JsonProperty("departure_date")
    @FutureOrPresent(message="should be past or present")
    private LocalDate departureDate;

    @JsonProperty("return_date")
    @FutureOrPresent(message="should be past or present")
    private LocalDate returnDate;

    @JsonProperty("seat_count")
    @Positive(message="should be postive")
    private Integer seatCount;

    @JsonProperty("first_class")
    @NotNull(message="should not be null")
    private Boolean firstClass;
}