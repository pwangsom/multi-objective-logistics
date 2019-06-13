package com.kmutt.sit.batch.tasks;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmutt.sit.jpa.entities.Player;
import com.kmutt.sit.jpa.entities.Team;
import com.kmutt.sit.jpa.respositories.PlayerRepository;
import com.kmutt.sit.jpa.respositories.TeamRepository;

@Service
public class DatabaseReader implements Tasklet {

	private static Logger logger = LoggerFactory.getLogger(DatabaseReader.class);
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
        logger.info("RetrivePlayer: start..");  
        logger.info(""); 
        logger.info("demo: findAll()"); 
        logger.info(""); 
        
        List<Player> players = playerRepository.findAll();
        
        if(!players.isEmpty()) {            
            logger.info("findAll(): found!");
            players.stream().forEach(p -> {
            	logger.info(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
            });
        } else {
            logger.info("findAll(): not found!");
        }

        logger.info(""); 
        logger.info("demo: findById()"); 
        logger.info(""); 
        
        Optional<Player> player1 = playerRepository.findById(1);
        
        if(player1.isPresent()) {
        	Player p = player1.get();
        	logger.info(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
        }
        
        logger.info(""); 
        logger.info("demo: findByFirstNameAndLastName()"); 
        logger.info(""); 
        
        players.clear();
        players = playerRepository.findByFirstNameAndLastName("Sadio", "Mane");
        
        if(!players.isEmpty()) {            
            logger.info("findByFirstNameAndLastName(): found!");
            players.stream().forEach(p -> {
            	logger.info(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
            });
        } else {
            logger.info("findByFirstNameAndLastName(): not found!");
        }
        
        logger.info(""); 
        logger.info("demo: findByTeam()"); 
        logger.info(""); 
        
        players.clear();
        
        Optional<Team> barca = teamRepository.findById(2);
        
        players = playerRepository.findByTeam(barca.get());
        
        if(!players.isEmpty()) {            
            logger.info("findByTeam(): found!");
            players.stream().forEach(p -> {
            	logger.info(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
            });
        } else {
            logger.info("findByTeam(): not found!");
        }

        logger.info("RetrivePlayer: finished..");
		
		return RepeatStatus.FINISHED;
	}
	
}
