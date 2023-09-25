package com.conseller.conseller.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationItemData {
    private Long notificationIdx;
    private String notificationTitle;
    private String notificationContent;
    private LocalDateTime notificationCreatedDate;
    private String notificationStatus;
    private Boolean seller;
}
