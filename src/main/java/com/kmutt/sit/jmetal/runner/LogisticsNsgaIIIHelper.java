package com.kmutt.sit.jmetal.runner;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kmutt.sit.jpa.entities.DhlRoute;
import com.kmutt.sit.jpa.entities.DhlShipment;
import com.kmutt.sit.jpa.respositories.LogisticsJobProblemRepository;
import com.kmutt.sit.jpa.respositories.LogisticsJobRepository;
import com.kmutt.sit.jpa.respositories.LogisticsJobResultRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Service
public class LogisticsNsgaIIIHelper {
	
	@Setter
	private String jobId;
	@Setter
	private String shipmentDate;
	@Setter
	private String vehicleType;    
    @Setter
    private List<DhlShipment> shipmentList;
    @Setter
    private List<DhlRoute> routeList;
    @Setter
    private Map<String, Integer> scoreMapping;
    @Setter
    private Integer currentRun;
    
    @Autowired
    private LogisticsJobRepository logisticsJobRepository;
    
    @Autowired
    private LogisticsJobProblemRepository logisticsJobProblemRepository;
    
    @Autowired
    private LogisticsJobResultRepository logisticsJobResultRepository;
	
    @Value("${area.notfound.score}")
    private Integer notfoundScore;
    
    @Value("${area.utilization.min}")
    private Double minUtilization;
    
    @Value("${nsga.reference.file}")
    private String referenceFile;

    @Value("${nsga.max.run}")
    private Integer maxRun;
    
    @Value("${nsga.max.iteration}")
    private Integer maxIteration;
}
