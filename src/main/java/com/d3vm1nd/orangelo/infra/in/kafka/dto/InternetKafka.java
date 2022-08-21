package com.d3vm1nd.orangelo.infra.in.kafka.dto;

import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternetKafka {

  private String host;
  private Instant startInstant;
  private int bytes;
  private Instant endInstant;
  private String speedType;
  private double speed;
  private Duration duration;

}
