package com.code.n26.service;

import com.code.n26.model.Statistics;
import com.code.n26.model.Transaction;

public interface StatisticsService {

	public Statistics getStatisticsForLast60Seconds();
	public boolean addTransaction(Transaction transaction);
}
