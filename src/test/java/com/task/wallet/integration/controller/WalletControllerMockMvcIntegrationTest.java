package com.task.wallet.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.wallet.controller.v1.WalletController;
import com.task.wallet.integration.container.TestContainerConfiguration;
import com.task.wallet.model.dto.WalletRequestDto;
import com.task.wallet.model.entity.Wallet;
import com.task.wallet.model.enums.OperationType;
import com.task.wallet.repository.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты для контроллера {@link WalletController}
 * <p>Для тестирования используется контейнер PostgreSQL, сконфигурированный в {@link TestContainerConfiguration},
 * что позволяет создать тестовую среду для выполнения операций над счётом</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainerConfiguration.class)
@AutoConfigureMockMvc
public class WalletControllerMockMvcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepository walletRepository;

    private Wallet wallet;

    /**
     * Создание тестового счёта в БД перед каждым тестом
     */
    @BeforeEach
    public void setup() {
        wallet = new Wallet();
        wallet.setBalance(new BigDecimal("0.01"));
        walletRepository.save(wallet);
    }

    /**
     * Удаление тестового кошелька после каждого теста
     */
    @AfterEach
    public void deleteTestWallet() {
        walletRepository.delete(wallet);
    }

    /**
     * Тест на успешное получение существующего кошелька по его идентификатору
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenId_whenGetExistingWallet_thenStatus200andWalletReturned() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{walletId}", wallet.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(wallet.getBalance()));
    }

    /**
     * Тест на попытку получения несуществующего кошелька с ожиданием ошибки 404
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenId_whenGetNonExistingWallet_thenStatus404andExceptionThrown() throws Exception {
        UUID randomWalletId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/wallets/{walletId}", randomWalletId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(String.format("Счёт с ID %s не найден в базе", randomWalletId)));
    }

    /**
     * Тест на попытку вывода средств с несуществующего кошелька с ожиданием ошибки 404
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenRequest_whenWithdrawFromNonExistentWallet_thenStatus404andExceptionThrown() throws Exception {
        UUID randomWalletId = UUID.randomUUID();
        WalletRequestDto walletRequestDto = new WalletRequestDto(
                randomWalletId,
                OperationType.WITHDRAW.name(),
                new BigDecimal("100")
        );

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(String.format("Счёт с ID %s не найден в базе для выполнения операции", randomWalletId)));
    }

    /**
     * Тест на попытку вывода средств при их недостаточности на счёте, ожидается ошибка 400
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenRequest_whenInsufficientFundsForWithdraw_thenStatus400andExceptionThrown() throws Exception {
        WalletRequestDto walletRequestDto = new WalletRequestDto(
                wallet.getId(),
                OperationType.WITHDRAW.name(),
                new BigDecimal("1000")
        );

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        String.format("Недостаточно средств на счёте с ID %s для снятия, доступно %s",
                                wallet.getId(),
                                wallet.getBalance())));
    }

    /**
     * Тест на выполнение конкурентных операций депозита и снятия средств с одного кошелька
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenRequests_whenConcurrentDepositWithWithdraw_thenStatus200andWalletReturned() throws Exception {
        WalletRequestDto walletRequestDtoForDeposit = new WalletRequestDto(
                wallet.getId(),
                OperationType.DEPOSIT.name(),
                new BigDecimal("15")
        );
        WalletRequestDto walletRequestDtoForWithdraw = new WalletRequestDto(
                wallet.getId(),
                OperationType.WITHDRAW.name(),
                new BigDecimal("10")
        );

        ExecutorService executor = Executors.newFixedThreadPool(100);
        IntStream.range(0, 1000).forEach(i -> executor.submit(() -> {
            try {
                mockMvc.perform(post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(walletRequestDtoForDeposit)))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
        IntStream.range(0, 1000).forEach(i -> executor.submit(() -> {
            try {
                mockMvc.perform(post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(walletRequestDtoForWithdraw)))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));

        executor.shutdown();
        boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);
        if (!terminated) {
            throw new RuntimeException("Executor service: ошибка завершения задач.");
        }

        mockMvc.perform(get("/api/v1/wallets/" + wallet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(5000.01));
    }

    /**
     * Тест на отправку запроса с некорректным идентификатором, ожидается ошибка 422
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenId_whenIncorrectIdProvided_thenStatus422andExceptionThrown() throws Exception {
        String incorrectId = "id-id";

        mockMvc.perform(get("/api/v1/wallets/{walletId}", incorrectId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Некорректный идентификатор счёта"));
    }

    /**
     * Тест на отправку запроса с некорректным типом операции, ожидается ошибка 422
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenRequest_whenIncorrectOperationTypeProvided_thenStatus422andExceptionThrown() throws Exception {
        WalletRequestDto walletRequestDto = new WalletRequestDto(
                wallet.getId(),
                "CLAIM",
                new BigDecimal("3")
        );

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletRequestDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Недопустимый тип операции"));
    }

    /**
     * Тест на отправку запроса с нулевой суммой, ожидается ошибка 422
     *
     * @throws Exception если выполнение запроса или проверка результата не удалась
     */
    @Test
    void givenRequest_whenZeroAmountProvided_thenStatus422andExceptionThrown() throws Exception {
        WalletRequestDto walletRequestDto = new WalletRequestDto(
                wallet.getId(),
                OperationType.DEPOSIT.name(),
                BigDecimal.ZERO
        );

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletRequestDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Сумма должна быть больше нуля"));
    }

}
