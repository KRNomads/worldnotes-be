package org.example.note.domain.exception;

import java.util.UUID;

import org.example.common.exception.CustomException;
import org.example.common.exception.ErrorCode;

public class NoteException extends CustomException {

    public NoteException(ErrorCode errorCode, UUID noteId) {
        super(errorCode, String.format(errorCode.getMessage(), noteId));
    }
}
