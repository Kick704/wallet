package com.task.wallet.service;

import com.task.wallet.exception_handler.ErrorCode;
import com.task.wallet.exception_handler.WalletApplicationException;
import com.task.wallet.model.entity.Wallet;
import com.task.wallet.model.enums.OperationType;
import com.task.wallet.repository.WalletRepository;
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

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal newBalance = switch (operationType) {
            case DEPOSIT -> currentBalance.add(amount);
            case WITHDRAW -> {
                if (currentBalance.compareTo(amount) < 0) {
                    throw new WalletApplicationException(
                            ErrorCode.BAD_REQUEST,
                            String.format("Недостаточно средств на счёте с ID %s для снятия, доступно %s", walletId,
                                    currentBalance)
                    );
                }
                yield currentBalance.subtract(amount);
            }
        };
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        return wallet;
    }

}
