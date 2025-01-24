package com.task.wallet.facade;

import com.task.wallet.model.dto.WalletRequestDto;
import com.task.wallet.model.dto.WalletResponseDto;
import com.task.wallet.model.entity.Wallet;

/**
 * Фасад-сервис для работы с DTO на основе {@link Wallet}
 */
public interface WalletFacadeService {

    /**
     * Получение информации о счёте по его идентификатору
     *
     * @param walletId идентификатор счёта в виде строки
     * @return DTO с информацией о счёте
     */
    WalletResponseDto getWallet(String walletId);

    /**
     * Выполнение операции над счётом
     *
     * @param requestDto DTO с информацией о транзакции
     * @return DTO с информацией об обновлённом счёте
     */
    WalletResponseDto processTransaction(WalletRequestDto requestDto);

}
