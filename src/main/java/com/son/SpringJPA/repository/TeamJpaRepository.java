package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    private final EntityManager em;

    public Team save(Team team) { // Create
        em.persist(team);
        return team;
    }

    public Optional<Team> findById(Long id) { // Read
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public List<Team> findAll() { // Read
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }

    public void delete(Team team) { // Delete
        em.remove(team);
    }

}
