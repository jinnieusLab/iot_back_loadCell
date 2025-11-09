package com.example.IoTBack.trashBin.liquid.domain;

import com.example.IoTBack.trashBin.bin.domain.Bin;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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

    // 센서 데이터가 수집(수정)된 시각
    @LastModifiedDate
    private LocalDateTime measuredAt;

    // 이전 측정값과 비교해 새로 추가된 물의 양
    private double addedWeight;

    // 5kg 초과 여부 혹은 80% 이상 찼는지 여부
    private Boolean overload = false;

    @OneToOne
    @JoinColumn(name = "bin_id")
    private Bin bin;

    public void setBin(Bin bin) {
        this.bin = bin;
    }
}
