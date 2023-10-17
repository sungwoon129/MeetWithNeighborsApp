package io.weyoui.weyouiappcore.group.command.application;

import io.weyoui.weyouiappcore.common.exception.ErrorCode;
import io.weyoui.weyouiappcore.common.exception.ErrorResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;

import java.util.ArrayList;
import java.util.List;

public class GroupValidator {
    public List<ErrorResponse> validate(GroupRequest groupRequest) {
        List<ErrorResponse> errors = new ArrayList<>();

        if(groupRequest == null) errors.add(ErrorResponse.of(ErrorCode.REQUIRED_PARAMETER_IS_NULL, "groupRequest is Null"));
        else {
            if(groupRequest.getCapacity() <= 0 || groupRequest.getCapacity() > 999) errors.add(ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT, "모임 최대 인원은 1 이상 999 이하의 정수입니다."));
            if(groupRequest.getPlace().getPoint().getX() < -180.000000 || groupRequest.getPlace().getPoint().getX() < 180.000000) errors.add(ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT, "경도 값은 -180.000000 ~ 180.000000 사이의 실수 값이어야 합니다."));
            if(groupRequest.getPlace().getPoint().getY() < -90.000000 || groupRequest.getPlace().getPoint().getY() <90.000000) errors.add(ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT, "위도 값은 -90.000000 ~ 90.000000 사이의 실수 값이어야 합니다."));
            if(groupRequest.getName().length() > 50) errors.add(ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT, "모임의 이름은 공백을 포함해 최대 50자를 넘을 수 없습니다."));

        }

        return errors;
    }
}
