package org.example.user.domain.exception;

import java.util.UUID;

import org.example.common.exception.CustomException;
import org.example.common.exception.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode, UUID userId) {
        super(errorCode, String.format(errorCode.getMessage(), userId));
    }
}
