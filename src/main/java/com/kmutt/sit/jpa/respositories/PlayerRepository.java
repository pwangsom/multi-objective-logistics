package com.kmutt.sit.jpa.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kmutt.sit.jpa.entities.Player;
import com.kmutt.sit.jpa.entities.Team;

public interface PlayerRepository extends JpaRepository<Player, Integer>{
	
	@Query("SELECT p FROM Player p WHERE firstName = ?1 AND lastName = ?2")
    List<Player> findByFirstNameAndLastName(String firstName, String lastName);
	
	@Query("SELECT p FROM Player p WHERE firstName = ?1 AND lastName = ?2")
    List<Player> getByName(String firstName, String lastName);
	
	@Query("SELECT p FROM Player p WHERE team = ?1")
    List<Player> findByTeam(Team team);
	
}
