package com.task.wallet.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Информация о счёте")
public class WalletResponseDto {

    @Schema(description = "Идентификатор счёта")
    private UUID walletId;

    @Schema(description = "Баланс счёта")
    private BigDecimal balance;

    public WalletResponseDto() {
    }

    public WalletResponseDto(UUID walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
