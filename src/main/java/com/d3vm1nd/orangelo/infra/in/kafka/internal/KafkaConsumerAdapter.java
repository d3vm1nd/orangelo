package com.d3vm1nd.orangelo.infra.in.kafka.internal;

import com.d3vm1nd.orangelo.infra.in.kafka.dto.InternetKafka;
import com.d3vm1nd.orangelo.infra.out.mysql.api.DownloadSpeedInternetRepository;
import com.d3vm1nd.orangelo.infra.out.mysql.dto.DownloadSpeedInternet;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerAdapter {

  private final DownloadSpeedInternetRepository downloadSpeedInternetRepository;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "TOPIC.DOWNLOAD")
  public void processMessage(String content) {
    try {
      InternetKafka internetKafka = objectMapper.readValue(content, InternetKafka.class);
      DownloadSpeedInternet downloadSpeedInternetJpa = DownloadSpeedInternet.builder()
          .bytes(internetKafka.getBytes()).duration(internetKafka.getDuration().toMillis())
          .endDateTime(LocalDateTime.ofInstant(internetKafka.getEndInstant(), ZoneOffset.UTC))
          .host(internetKafka.getHost()).speed(internetKafka.getSpeed())
          .startDateTime(LocalDateTime.ofInstant(internetKafka.getStartInstant(), ZoneOffset.UTC))
          .build();
      downloadSpeedInternetRepository.save(downloadSpeedInternetJpa);

      log.info("Message saved :" + downloadSpeedInternetJpa);
    } catch (Exception e) {
      log.error("ERROR", e);
    }

  }

}
