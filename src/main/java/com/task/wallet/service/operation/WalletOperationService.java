package com.task.wallet.service.operation;

import com.task.wallet.exception_handler.ErrorCode;
import com.task.wallet.exception_handler.WalletApplicationException;
import com.task.wallet.model.enums.OperationType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Сервис, определяющий стратегию для выполнения операции над счётом
 */
@Service
public class WalletOperationService {

    /**
     * Список бинов реализованных стратегий
     */
    private final List<WalletOperation> operations;

    public WalletOperationService(List<WalletOperation> operations) {
        this.operations = operations;
    }

    /**
     * Определение и выполнение стратегии по типу операции
     *
     * @param walletId идентификатор счёта
     * @param operationType тип операции
     * @param balance баланс счёта
     * @param amount сумма для операции
     * @return баланс счёта после выполнения операции
     */
    public BigDecimal execute(UUID walletId, OperationType operationType, BigDecimal balance, BigDecimal amount) {
        return operations.stream()
                .filter(op -> op.supports(operationType))
                .findFirst()
                .orElseThrow(() -> new WalletApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,
                        "Ошибка определения типа операции")
                )
                .apply(walletId, balance, amount);
    }

}
