package com.example.IoTBack.loadCell.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bin_id")
    private Long id;

    @OneToOne(mappedBy = "bin")
    private Liquid liquid;

    @OneToMany(mappedBy = "bin")
    private List<Cup> cups = new ArrayList<>();
}
