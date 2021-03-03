package com.zup.mercadolivre.purchase.repository;

import com.zup.mercadolivre.purchase.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByIdentifier(String identifier);
}
