package org.example.note.domain.exception;

import java.util.UUID;

import org.example.common.exception.CustomException;
import org.example.common.exception.ErrorCode;

public class ProjectException extends CustomException {

    public ProjectException(ErrorCode errorCode, UUID projectId) {
        super(errorCode, String.format(errorCode.getMessage(), projectId));
    }
}
