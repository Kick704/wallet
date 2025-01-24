package com.task.wallet.exception_handler;

import com.task.wallet.model.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений(ошибок) приложения с логированием и формированием ответа {@link ErrorResponseDTO}
 */
@RestControllerAdvice
public class ExceptionTranslator {

    /**
     * Логгер обработчика
     */
    private static final Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);

    /**
     * Обработчик кастомного исключения приложения
     *
     * @param ex обрабатываемое исключение {@link WalletApplicationException}
     * @return информация об ошибке
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleSwimmingPoolManagementException(
            @NonNull WalletApplicationException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getMessage();
        log.error("Common error: {}", message);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponseDTO(errorCode, message));
    }

    /**
     * Обработчик ошибок валидаций.
     * <p>{@link MethodArgumentNotValidException} - ошибки валидации тела запроса
     * <p>{@link ConstraintViolationException} - ошибки валидации параметров запроса
     *
     * @param ex общее обрабатываемое исключение {@link Exception}
     * @return информация об ошибке
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(Exception ex) {
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        String message;
        if (ex instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError();
            message = fieldError != null ? fieldError.getDefaultMessage() : errorCode.getDescription();
        } else {
            message = ((ConstraintViolationException) ex).getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
        }
        log.error("Validation error in {}: {}", ex.getClass().getSimpleName(), message);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponseDTO(errorCode, message));
    }

    /**
     * Обработчик остальных ошибок с логированием всей информации о них
     *
     * @param ex обрабатываемое исключение {@link Exception}
     * @return информация об ошибке
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleOtherException(@NonNull Exception ex) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();
        log.error("Unhandled error: {}", message, ex);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponseDTO(errorCode, message));
    }

}
