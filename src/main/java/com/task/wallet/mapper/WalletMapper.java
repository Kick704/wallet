package com.task.wallet.mapper;

import com.task.wallet.model.dto.WalletResponseDto;
import com.task.wallet.model.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразований между сущностью {@link Wallet} и связанных с ним DTO
 */
@Mapper(componentModel = "spring")
public interface WalletMapper {

    /**
     * Маппинг из сущности в DTO
     *
     * @param wallet сущность счёта
     * @return DTO с информацией о счёте
     */
    @Mapping(target = "walletId", source = "wallet.id")
    WalletResponseDto toResponseDto(Wallet wallet);

}
