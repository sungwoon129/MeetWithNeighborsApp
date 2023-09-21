package io.weyoui.weyouiappcore.user.query.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.weyoui.weyouiappcore.user.command.domain.UserState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Schema(description = "회원 목록 검색 요청 DTO")
@Setter
@Getter
@NoArgsConstructor
public class UserSearchRequest {
    private String email;
    private String nickname;
    private String address;
    private String[] states = new String[]{UserState.ACTIVE.getCode(),UserState.SLEEP.getCode()};

    @Builder
    public UserSearchRequest(String email, String nickname, String address, String[] states) {
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.states = states;
    }

}
