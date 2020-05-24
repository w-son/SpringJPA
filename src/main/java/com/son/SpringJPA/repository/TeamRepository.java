package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
