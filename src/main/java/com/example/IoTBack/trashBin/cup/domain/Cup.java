package com.example.IoTBack.trashBin.cup.domain;

import com.example.IoTBack.trashBin.bin.domain.Bin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Cup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cup_id")
    private Long id;

    // Liquid.addedWeight + 컵 무게
    private double weight;

    // 액체 감지 여부 (컵 투입 시 true)
    private Boolean hasLiquid;

    // 기타 센서값 추가...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id")
    private Bin bin;
}
