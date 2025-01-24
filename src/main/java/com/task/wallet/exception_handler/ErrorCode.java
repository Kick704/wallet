package com.task.wallet.exception_handler;

import org.springframework.http.HttpStatus;

/**
 * Перечисление основных ошибок приложения
 */
public enum ErrorCode {

    BAD_REQUEST(1000, "Некорректный запрос", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1001, "Ресурс не найден", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR(1002, "Ошибка валидации", HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_SERVER_ERROR (1003, "Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);

    /**
     * Внутренний код ошибки
     */
    private final int code;

    /**
     * Описание ошибки по умолчанию
     */
    private final String description;

    /**
     * HTTP статус ошибки сервера
     */
    private final HttpStatus status;

    ErrorCode(int code, String description, HttpStatus status) {
        this.code = code;
        this.description = description;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
