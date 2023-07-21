package io.weyoui.weyouiappcore.user.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearch {
    private String email;
    private String nickname;
    private String address;
    private String groupName;
    private int page;
    private int limit;


}
