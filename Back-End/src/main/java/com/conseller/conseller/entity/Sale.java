package com.conseller.conseller.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "saleIdx")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long saleIdx;

    @Column(name = "sale_price", nullable = false)
    private Integer salePrice;

    @CreatedDate
    private LocalDateTime saleCreatedDate;

    @Column(name = "sale_end_date")
    private LocalDateTime saleEndedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;



}