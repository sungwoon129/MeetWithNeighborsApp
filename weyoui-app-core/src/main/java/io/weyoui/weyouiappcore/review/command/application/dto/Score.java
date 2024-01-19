package io.weyoui.weyouiappcore.review.command.application.dto;

public enum Score {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final Integer value;

    Score(Integer value) {
        this.value = value;
    }

    public static float getFloatValue(Score score) {
        return score.value;
    }

    public static int getIntValue(Score score) {
        return score.value;
    }
}
