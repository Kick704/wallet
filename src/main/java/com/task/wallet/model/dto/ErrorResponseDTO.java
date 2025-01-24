package com.task.wallet.model.dto;

import com.task.wallet.exception_handler.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

import java.time.ZonedDateTime;

/**
 * DTO с информацией об ошибке
 */
@Schema(description = "Информация об ошибке")
public class ErrorResponseDTO {

    /**
     * Код ошибки
     */
    @Schema(description = "Код ошибки")
    private String errorCode;

    /**
     * Сообщение об ошибке
     */
    @Schema(description = "Сообщение об ошибке")
    private String message;

    /**
     * Время возникновения ошибки
     */
    @Schema(description = "Время возникновения ошибки")
    private final ZonedDateTime timestamp = ZonedDateTime.now();

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(@NonNull ErrorCode code, String message) {
        this.errorCode = String.format("%d: %s", code.getCode(), code.name());
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ErrorResponseDTO{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}

