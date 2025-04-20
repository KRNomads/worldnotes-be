package org.example.auth.domain.exception;

import org.example.common.exception.CustomException;
import org.example.common.exception.ErrorCode;

public class ApiKeyException extends CustomException {

    public ApiKeyException(ErrorCode errorCode, String apiKey) {
        super(errorCode, String.format(errorCode.getMessage(), apiKey));
    }
}
