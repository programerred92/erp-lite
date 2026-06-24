package com.magicbox.erp_lite.persistence.jpa.repositories;

import com.magicbox.erp_lite.persistence.jpa.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
