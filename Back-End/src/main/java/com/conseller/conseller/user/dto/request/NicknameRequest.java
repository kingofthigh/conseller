package com.conseller.conseller.user.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class NicknameRequest {

    private String userNickname;

}
