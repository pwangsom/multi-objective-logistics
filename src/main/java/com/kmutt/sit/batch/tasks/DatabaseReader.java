package com.kmutt.sit.batch.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.kmutt.sit.jpa.entities.Player;
import com.kmutt.sit.jpa.respositories.PlayerRepository;

public class DatabaseReader implements Tasklet {

	private static Logger logger = LoggerFactory.getLogger(DatabaseReader.class);
	
	private PlayerRepository playerRepository;
	
	public DatabaseReader(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
        logger.info("RetrivePlayer: start..");  
        
        List<Player> players = playerRepository.findAll();
        
        if(!players.isEmpty()) {            
            logger.info("RetrivePlayer: found!");
            players.stream().forEach(p -> {
            	logger.info(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
            });
        } else {
            logger.info("RetrivePlayer: not found!");
        }

        logger.info("RetrivePlayer: finished..");
		
		return RepeatStatus.FINISHED;
	}
	
}
