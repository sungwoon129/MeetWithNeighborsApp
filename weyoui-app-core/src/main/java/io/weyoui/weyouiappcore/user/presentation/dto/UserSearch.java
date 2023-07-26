package io.weyoui.weyouiappcore.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Builder
@NoArgsConstructor
public class UserSearch {
    private String email;
    private String nickname;
    private String address;

    public UserSearch(String email, String nickname, String address) {
        this.email = email;
        this.nickname = nickname;
        this.address = address;
    }

}
