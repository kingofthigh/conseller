package com.conseller.conseller.entity;

import com.conseller.conseller.barter.barter.barterDto.BarterCreateDto;
import com.conseller.conseller.barter.barter.enums.BarterStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "barterIdx")
public class Barter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barterIdx;

    @Column(name = "barter_name", nullable = false)
    private String barterName;

    @Column(name = "barter_text", nullable = false)
    private String barterText;

    @CreatedDate
    private LocalDateTime barterCreatedDate;

    @Column(name = "barter_end_date", nullable = false)
    private LocalDateTime barterEndDate;


    @LastModifiedDate
    private LocalDateTime barterModifiedDate;

    @Column(name = "barter_completed_date")
    private LocalDateTime barterCompletedDate;

    @Enumerated(EnumType.STRING) // Enum의 문자열 값을 데이터베이스에 저장
    private BarterStatus barterStatus = BarterStatus.EXCHANGEABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_idx")
    private User barterHost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complete_guest_idx")
    private User barterCompleteGuest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_idx")
    private SubCategory subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_catergory_idx")
    private SubCategory preferSubCategory;

    @Builder
    public Barter(String barterName, String barterText, LocalDateTime barterEndDate, User barterHost, SubCategory subCategory, SubCategory preferSubCategory) {
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterCreatedDate = now();
        this.barterEndDate = barterEndDate;
        this.barterHost = barterHost;
        this.subCategory = subCategory;
        this.preferSubCategory = preferSubCategory;
    }
}

