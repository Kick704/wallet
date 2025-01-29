package com.task.wallet.controller.v1;

import com.task.wallet.facade.WalletFacadeService;
import com.task.wallet.model.dto.WalletRequestDto;
import com.task.wallet.model.dto.WalletResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для управления счетами
 */
@RestController
@Validated
@RequestMapping("${api-base-path}")
@Tag(name = "Счета", description = "Управление счетами")
public class WalletController {

    private final WalletFacadeService walletFacadeService;

    public WalletController(WalletFacadeService walletFacadeService) {
        this.walletFacadeService = walletFacadeService;
    }

    /**
     * Обработчик GET запроса для получения информации о счёте по его идентификатору
     *
     * @param walletId идентификатор счёта
     * @return информация о счёте
     */
    @GetMapping("wallets/{walletId}")
    @Operation(summary = "Получение информации о счёте",
            description = "Позволяет получить информацию о счёте по его идентификатору")
    public WalletResponseDto getWalletInfo(@PathVariable
                                           @UUID(message = "Некорректный идентификатор счёта")
                                           String walletId) {
        return walletFacadeService.getWallet(walletId);
    }

    /**
     * Обработчик POST запроса для изменения баланса счёта
     *
     * @param walletRequestDto объект-запрос
     * @return изменённый счёт
     */
    @PostMapping("/wallet")
    @Operation(summary = "Обновление счёта",
            description = "Позволяет выполнить операцию на счётом (пополнение, снятие средств)")
    public WalletResponseDto updateBalance(@Valid @RequestBody WalletRequestDto walletRequestDto) {
        return walletFacadeService.processTransaction(walletRequestDto);
    }


}
