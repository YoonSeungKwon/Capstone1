package yoon.capstone.application.common.exception.sequence;

import jakarta.validation.GroupSequence;

@GroupSequence({
        ValidationGroup.EmailBlank.class,
        ValidationGroup.EmailFormat.class,
        ValidationGroup.PasswordBlank.class
})
public interface LoginValidationSequence {
}
