package io.weyoui.weyouiappcore.review.command.application.domain;

import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.review.command.application.dto.Score;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {

    @EmbeddedId
    private ReviewId id;

    @Embedded
    private Reviewer reviewer;

    private String contents;

    private Score score;


}
