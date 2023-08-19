package io.weyoui.weyouiappcore.user.command.application;


import io.weyoui.weyouiappcore.user.command.application.dto.UserUpdateRequest;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UpdateUserInfoService {

    private final UserViewService userViewService;
    private final UserRepository userRepository;

    public UpdateUserInfoService(UserViewService userViewService, UserRepository userRepository) {
        this.userViewService = userViewService;
        this.userRepository = userRepository;
    }


    public void changeNickname(UserId userId, UserUpdateRequest userUpdateRequest) {
        User user = userViewService.findById(userId);

        user.setNickname(userUpdateRequest.getNickname());

        userRepository.save(user);
    }

    public void changeAddress(UserId userId, UserUpdateRequest userUpdateRequest) {
        User user = userViewService.findById(userId);

        user.setAddress(userUpdateRequest.getAddress());

        userRepository.save(user);
    }

    public void identify(UserId userId, UserUpdateRequest userUpdateRequest) {
        User user = userViewService.findById(userId);

        user.identify(userUpdateRequest.getIsIdentified(), userUpdateRequest.getIdentificationDate());

        userRepository.save(user);
    }
}
