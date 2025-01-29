package com.task.wallet.service.operation;

import com.task.wallet.exception_handler.ErrorCode;
import com.task.wallet.exception_handler.WalletApplicationException;
import com.task.wallet.model.enums.OperationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Реализация стратегии для пополнения баланса счёта
 */
@Component
public class DepositOperation implements WalletOperation {

    /**
     * Флаг, определяющий поддерживаемый тип операции
     *
     * @param operationType тип операции
     * @return true, если тип операции поддерживается, иначе false
     */
    @Override
    public boolean supports(OperationType operationType) {
        return OperationType.DEPOSIT == operationType;
    }

    /**
     * Выполнение операции над счётом
     *
     * @param walletId идентификатор счёта
     * @param balance баланс счёта
     * @param amount сумма для выполнения операции
     * @return баланс счёта после выполнения операции
     */
    @Override
    public BigDecimal apply(UUID walletId, BigDecimal balance, BigDecimal amount) {
        BigDecimal newBalance = balance.add(amount);
        if (newBalance.compareTo(WalletOperation.MAX_BALANCE) > 0) {
            throw new WalletApplicationException(
                    ErrorCode.BAD_REQUEST,
                    String.format("Ошибка при пополнении счёта с ID %s, итоговый баланс не может превышать %s",
                            walletId, WalletOperation.MAX_BALANCE)
            );
        }
        return balance.add(amount);
    }

}
