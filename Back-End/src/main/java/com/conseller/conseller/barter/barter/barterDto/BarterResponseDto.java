package com.conseller.conseller.barter.barter.barterDto;

import com.conseller.conseller.barter.barter.enums.BarterStatus;
import com.conseller.conseller.entity.SubCategory;
import com.conseller.conseller.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

public class BarterResponseDto {
    private Long barterIdx;
    private String barterName;
    private String barterText;
    private LocalDateTime barterCreatedDate;
    private LocalDateTime barterEndDate;
    private BarterStatus barterStatus;
    private SubCategory subCategory;
    private SubCategory preferSubCategory;
    private User barterHost;
    private User barterCompleteGuest;

    @Builder
    public BarterResponseDto(Long barterIdx, String barterName, String barterText, LocalDateTime barterCreatedDate, LocalDateTime barterEndDate, BarterStatus barterStatus, SubCategory subCategory, SubCategory preferSubCategory, User barterHost, User barterCompleteGuest) {
        this.barterIdx = barterIdx;
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterCreatedDate = barterCreatedDate;
        this.barterEndDate = barterEndDate;
        this.barterStatus = barterStatus;
        this.subCategory = subCategory;
        this.preferSubCategory = preferSubCategory;
        this.barterHost = barterHost;
        this.barterCompleteGuest = barterCompleteGuest;
    }
}