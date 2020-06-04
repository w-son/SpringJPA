package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
