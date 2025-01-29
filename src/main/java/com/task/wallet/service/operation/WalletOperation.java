package com.task.wallet.service.operation;

import com.task.wallet.model.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Интерфейс стратегии для выполнения операции над счётом
 */
public interface WalletOperation {

    /**
     * Максимально допустимый баланс на счёте
     */
    BigDecimal MAX_BALANCE = new BigDecimal("9999999999999.99");

    /**
     * Флаг, определяющий поддерживаемый тип операции
     *
     * @param operationType тип операции
     * @return true, если тип операции поддерживается, иначе false
     */
    boolean supports(OperationType operationType);

    /**
     * Выполнение операции над счётом
     *
     * @param walletId идентификатор счёта
     * @param balance баланс счёта
     * @param amount сумма для выполнения операции
     * @return баланс счёта после выполнения операции
     */
    BigDecimal apply(UUID walletId, BigDecimal balance, BigDecimal amount);

}
