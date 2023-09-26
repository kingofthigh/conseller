package com.conseller.conseller.barter.request.barterRequestDto;



import com.conseller.conseller.barter.BarterGuestItem.BarterGuestItemDto.BarterGuestItemDto;
import com.conseller.conseller.entity.Barter;
import com.conseller.conseller.entity.BarterGuestItem;
import com.conseller.conseller.entity.BarterRequest;
import com.conseller.conseller.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BarterRequestRegistDto {
    private Long barterIdx;
    private String userId;
    private List<Long> barterGuestItemList;

    public BarterRequest toEntity(Barter barter, User user) {
        return BarterRequest.builder()
                .barter(barter)
                .user(user)
                .build();
    }
}