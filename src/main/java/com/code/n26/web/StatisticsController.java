package com.code.n26.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.code.n26.model.Statistics;
import com.code.n26.model.Transaction;
import com.code.n26.service.StatisticsService;

@RestController
public class StatisticsController {

	private static final Logger logger = LoggerFactory
			.getLogger(StatisticsController.class);

	@Autowired
	private StatisticsService statisticsService;

	@PostMapping("/transactions")
	public ResponseEntity<Void> addTransaction(
			@RequestBody Transaction transaction) {

		if(transaction.getAmount() !=null && transaction.getTimestamp()!=null)
		{
		logger.info("Request to add transaction to statistics Data");

		boolean isTransactionOlderThanSixtySeconds = statisticsService
				.addTransaction(transaction);

		if (isTransactionOlderThanSixtySeconds) {

			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		} else {
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		}
		logger.info("Request have null contents not proceeding further");
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	
	@GetMapping("/statistics")	
	public ResponseEntity<Statistics> getStatistics() {

		logger.info("Request to pull transaction statistics for lat 60 seconds");

		Statistics statistics = statisticsService
				.getStatisticsForLast60Seconds();

	
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");  
    
    	return new ResponseEntity<>(statistics, headers, HttpStatus.OK);
	}

}
