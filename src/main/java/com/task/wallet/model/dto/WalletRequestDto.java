package com.task.wallet.model.dto;

import com.task.wallet.common.validation.ValidEnum;
import com.task.wallet.model.enums.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

@Schema(description = "Данные для выполнения операции над счётом")
public class WalletRequestDto {

    @UUID(message = "Некорректный идентификатор счёта")
    @NotNull(message = "Не введён идентификатор счёта")
    @Schema(description = "Идентификатор счёта", format = "uuid")
    private String walletId;

    @NotNull(message = "Не выбран тип операции")
    @ValidEnum(clazz = OperationType.class, message = "Недопустимый тип операции")
    @Schema(description = "Тип операции", oneOf = {OperationType.class})
    private String operationType;

    @NotNull(message = "Не введена сумма")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
    @Digits(integer = 13, fraction = 2,
            message = "Сумма должна иметь не более 13 цифр в целой части и не более 2 в дробной части")
    @Schema(description = "Сумма")
    private BigDecimal amount;

    public WalletRequestDto() {
    }

    public WalletRequestDto(String walletId, String operationType, BigDecimal amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
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
