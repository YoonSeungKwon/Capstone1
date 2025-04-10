package yoon.capstone.application.common.exception.sequence;

import jakarta.validation.GroupSequence;

@GroupSequence({
        ValidationGroup.TitleBlank.class,
        ValidationGroup.LinkBlank.class,
        ValidationGroup.GoalBlank.class,
        ValidationGroup.DateFuture.class
})
public interface ProjectValidationSequence {
}
