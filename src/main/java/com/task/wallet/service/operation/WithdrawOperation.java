package com.task.wallet.service.operation;

import com.task.wallet.exception_handler.ErrorCode;
import com.task.wallet.exception_handler.WalletApplicationException;
import com.task.wallet.model.enums.OperationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Реализация стратегии для вывода средств со счёта
 */
@Component
public class WithdrawOperation implements WalletOperation {

    /**
     * Флаг, определяющий поддерживаемый тип операции
     *
     * @param operationType тип операции
     * @return true, если тип операции поддерживается, иначе false
     */
    @Override
    public boolean supports(OperationType operationType) {
        return OperationType.WITHDRAW == operationType;
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
        if (balance.compareTo(amount) < 0) {
            throw new WalletApplicationException(
                    ErrorCode.BAD_REQUEST,
                    String.format("Недостаточно средств на счёте с ID %s для снятия, доступно %s", walletId, balance)
            );
        }
        return balance.subtract(amount);
    }

}
