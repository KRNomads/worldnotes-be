package org.example.note.domain.exception;

import org.example.common.exception.CustomException;
import org.example.common.exception.ErrorCode;
import org.example.note.domain.enums.BlockType;

public class PropertyException extends CustomException {

    public PropertyException(ErrorCode errorCode, BlockType BlockType) {
        super(errorCode, String.format(errorCode.getMessage(), BlockType.name()));
    }

}
