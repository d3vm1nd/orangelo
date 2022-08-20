package com.d3vm1nd.orangelo.infra.out.mysql.dto;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "download_speed_internet_jpa")
public class DownloadSpeedInternet {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String host;

  private LocalDateTime startDateTime;

  private Integer bytes;

  private LocalDateTime endDateTime;

  private Double speed;

  private long duration;

}
