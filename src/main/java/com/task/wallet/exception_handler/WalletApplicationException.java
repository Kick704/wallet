package com.task.wallet.exception_handler;

/**
 * Общее исключение для управления ошибками в приложении с привязкой кода {@link ErrorCode}
 */
public class WalletApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    public WalletApplicationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
