package com.kmutt.sit.jpa.respositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmutt.sit.jpa.entities.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {

}
