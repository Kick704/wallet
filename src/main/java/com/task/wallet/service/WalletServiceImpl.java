package com.task.wallet.service;

import com.task.wallet.exception_handler.ErrorCode;
import com.task.wallet.exception_handler.WalletApplicationException;
import com.task.wallet.model.entity.Wallet;
import com.task.wallet.model.enums.OperationType;
import com.task.wallet.repository.WalletRepository;
import com.task.wallet.service.operation.WalletOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Реализация сервиса для работы с сущностью {@link Wallet}
 */
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final WalletOperationService walletOperationService;

    public WalletServiceImpl(WalletRepository walletRepository, WalletOperationService walletOperationService) {
        this.walletRepository = walletRepository;
        this.walletOperationService = walletOperationService;
    }

    /**
     * Получение счёта по его идентификатору
     *
     * @param walletId идентификатор счёта
     * @return счёт
     */
    @Override
    @Transactional(readOnly = true)
    public Wallet getWallet(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletApplicationException(
                        ErrorCode.NOT_FOUND,
                        String.format("Счёт с ID %s не найден в базе", walletId)));
    }

    /**
     * Выполнение операции над счётом
     *
     * @param walletId идентификатор счёта
     * @param operationType тип операции
     * @param amount сумма для изменения счёта
     * @return обновлённый счёт
     */
    @Override
    @Transactional
    public Wallet processTransaction(UUID walletId, OperationType operationType, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletApplicationException(
                        ErrorCode.NOT_FOUND,
                        String.format("Счёт с ID %s не найден в базе для выполнения операции", walletId)));
        BigDecimal newBalance = walletOperationService.execute(walletId, operationType, wallet.getBalance(), amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        return wallet;
    }

}
