package org.example.note.domain.exception;

import org.example.common.exception.CustomException;
import org.example.common.exception.ErrorCode;

public class BlockExeption extends CustomException {

    public BlockExeption(ErrorCode errorCode, Long BlockId) {
        super(errorCode, String.format(errorCode.getMessage(), BlockId));
    }
}
