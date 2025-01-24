package com.task.wallet.facade;

import com.task.wallet.mapper.WalletMapper;
import com.task.wallet.model.dto.WalletRequestDto;
import com.task.wallet.model.dto.WalletResponseDto;
import com.task.wallet.model.entity.Wallet;
import com.task.wallet.model.enums.OperationType;
import com.task.wallet.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Реализация фасад-сервиса для работы с DTO на основе {@link Wallet}
 */
@Service
public class WalletFacadeServiceImpl implements WalletFacadeService {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    public WalletFacadeServiceImpl(WalletService walletService, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
    }

    /**
     * Получение информации о счёте по его идентификатору
     *
     * @param walletId идентификатор счёта в виде строки
     * @return DTO с информацией о счёте
     */
    @Override
    public WalletResponseDto getWallet(String walletId) {
        Wallet wallet = walletService.getWallet(UUID.fromString(walletId));
        return walletMapper.toResponseDto(wallet);
    }

    /**
     * Выполнение транзакции над счётом
     *
     * @param requestDto DTO с информацией о транзакции
     * @return DTO с информацией об обновлённом счёте
     */
    @Override
    public WalletResponseDto processTransaction(WalletRequestDto requestDto) {
        Wallet wallet = walletService.processTransaction(
                requestDto.getWalletId(),
                OperationType.valueOf(requestDto.getOperationType()),
                requestDto.getAmount()
        );
        return walletMapper.toResponseDto(wallet);
    }

}
