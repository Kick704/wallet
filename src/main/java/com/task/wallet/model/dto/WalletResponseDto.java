package com.task.wallet.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Информация о счёте")
public class WalletResponseDto {

    @Schema(description = "Идентификатор счёта", format = "uuid")
    private String walletId;

    @Schema(description = "Баланс счёта")
    private BigDecimal balance;

    public WalletResponseDto() {
    }

    public WalletResponseDto(String walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
