package com.task.wallet.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Сущность счёта в базе данных
 */
@Entity
@Table(name = "wallets")
public class Wallet extends BaseEntity {

    /**
     * Баланс счёта
     */
    @Column(name = "balance")
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
