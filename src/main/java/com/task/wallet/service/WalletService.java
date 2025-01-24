package com.task.wallet.service;

import com.task.wallet.model.entity.Wallet;
import com.task.wallet.model.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для работы с сущностью {@link Wallet}
 */
public interface WalletService {

    /**
     * Получение счёта по его идентификатору
     *
     * @param walletId идентификатор счёта
     * @return счёт
     */
    Wallet getWallet(UUID walletId);

    /**
     * Выполнение операции над счётом
     *
     * @param walletId идентификатор счёта
     * @param operationType тип операции
     * @param amount сумма для изменения счёта
     * @return обновлённый счёт
     */
    Wallet processTransaction(UUID walletId, OperationType operationType, BigDecimal amount);

}
