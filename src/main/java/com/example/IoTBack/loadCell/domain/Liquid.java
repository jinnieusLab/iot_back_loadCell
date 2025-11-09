package com.example.IoTBack.loadCell.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Liquid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liquid_id")
    private Long id;

    private double weight;

    // 센서 데이터가 수집된 시각
    private LocalDateTime measuredAt;

    // 이전 측정값과 비교해 새로 추가된 물의 양
    private double addedWeight;

    // 5kg 초과 여부
    private Boolean overload;

    @OneToOne
    @JoinColumn(name = "bin_id")
    private Bin bin;
}
