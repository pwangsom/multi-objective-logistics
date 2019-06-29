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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kmutt.sit.jpa.entities.DhlShipment;
import com.kmutt.sit.jpa.entities.Player;
import com.kmutt.sit.jpa.entities.Team;
import com.kmutt.sit.jpa.respositories.DhlShipmentRepository;
import com.kmutt.sit.jpa.respositories.PlayerRepository;
import com.kmutt.sit.jpa.respositories.TeamRepository;

@Service
public class DatabaseReader implements Tasklet {

	private static Logger logger = LoggerFactory.getLogger(DatabaseReader.class);
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private DhlShipmentRepository shipmentRepository;
	
    @Value("${shipment.month}")
    private String shipmentMonth;
    
    @Value("${shipment.date}")
    private String shipmentDate;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
        logger.info("DatabaseReader: start..");  
        logger.info(""); 
        
        List<Player> players = playerRepository.findAll();
        
        if(!players.isEmpty()) {            
            logger.debug("findAll(): found!");
            players.stream().forEach(p -> {
            	logger.debug(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
            });
        } else {
            logger.debug("findAll(): not found!");
        }

        logger.debug(""); 
        
        Optional<Player> player1 = playerRepository.findById(1);
        
        if(player1.isPresent()) {
        	Player p = player1.get();
        	logger.debug(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
        }
        
        logger.debug(""); 
        
        players.clear();
        players = playerRepository.findByFirstNameAndLastName("Sadio", "Mane");
        
        if(!players.isEmpty()) {            
            logger.debug("findByFirstNameAndLastName(): found!");
            players.stream().forEach(p -> {
            	logger.debug(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
            });
        } else {
            logger.debug("findByFirstNameAndLastName(): not found!");
        }
        
        logger.debug(""); 
        
        players.clear();
        
        Optional<Team> barca = teamRepository.findById(2);
        
        players = playerRepository.findByTeam(barca.get());
        
        if(!players.isEmpty()) {            
            logger.debug("findByTeam(): found!");
            players.stream().forEach(p -> {
            	logger.debug(p.getFirstName() + " " + p.getLastName() + ": " + p.getTeam().getTeamName());
            });
        } else {
            logger.debug("findByTeam(): not found!");
        }
        
        logger.debug(""); 
        
        Optional<DhlShipment> shipmentOpt = shipmentRepository.findById(32);
        
        if(shipmentOpt.isPresent()) {            
            logger.debug("findByShipmentKey(): found!");
            DhlShipment shipment = shipmentOpt.get();
            logger.debug(shipment.getPudRte() + " " + shipment.getCourierId() + " " + shipment.getAwbBooking());
        } else {
            logger.debug("findByShipmentKey(): not found!");
        }        

        logger.debug(""); 
        
        List<DhlShipment> shipments = shipmentRepository.findByActDt(shipmentMonth + shipmentDate);
        
        if(!shipments.isEmpty()) {            
            logger.debug("findByActDt(): found!");
            logger.debug("Shipment size: " + shipments.size());
        } else {
            logger.debug("findByActDt(): not found!");
        }

        logger.debug(""); 
        logger.info("DatabaseReader: finished..");
		
		return RepeatStatus.FINISHED;
	}
	
}
