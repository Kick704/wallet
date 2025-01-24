package com.task.wallet.model.dto;

import com.task.wallet.common.validation.ValidEnum;
import com.task.wallet.model.enums.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Данные для выполнения операции над счётом")
public class WalletRequestDto {

    @NotNull(message = "Не введён идентификатор счёта")
    @Schema(description = "Идентификатор счёта")
    private UUID walletId;

    @NotNull(message = "Не выбран тип операции")
    @ValidEnum(clazz = OperationType.class, message = "Недопустимый тип операции")
    @Schema(description = "Тип операции")
    private String operationType;

    @NotNull(message = "Не введена сумма")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
    @Digits(integer = 17, fraction = 2,
            message = "Сумма должна иметь не более 19 цифр и не более 2 знаков в дробной части")
    @Schema(description = "Сумма")
    private BigDecimal amount;

    public WalletRequestDto() {
    }

    public WalletRequestDto(UUID walletId, String operationType, BigDecimal amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
