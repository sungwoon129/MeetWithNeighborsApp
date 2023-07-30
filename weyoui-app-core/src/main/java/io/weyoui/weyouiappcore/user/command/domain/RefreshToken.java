package io.weyoui.weyouiappcore.user.command.domain;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String refreshToken;
    private String userid;
}
