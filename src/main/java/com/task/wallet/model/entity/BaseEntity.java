package com.task.wallet.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Базовый класс с общими полями и методами для создаваемых и обновляемых сущностей
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Идентификатор сущности
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    /**
     * Дата и время создания сущности
     * <p>Устанавливается при создании сущности, неизменно
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected ZonedDateTime createdAt;

    /**
     * Дата и время обновления сущности
     * <p>Изменяется при обновлении сущности
     */
    @LastModifiedDate
    @Column(name = "modified_at")
    protected ZonedDateTime modifiedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

}
