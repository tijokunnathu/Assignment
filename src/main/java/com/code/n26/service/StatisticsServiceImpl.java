package com.code.n26.service;

import java.time.Duration;
import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.code.n26.model.Statistics;
import com.code.n26.model.Transaction;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();

	@Override
	public Statistics getStatisticsForLast60Seconds() {

		Statistics statistics = new Statistics();

		if (!transactions.isEmpty()) {

			DoubleSummaryStatistics stats = transactions
					.stream()
					.filter(x -> !isTransactionOlderThanSixtySeconds(x,
							Instant.now()))
					.collect(
							Collectors
									.summarizingDouble(Transaction::getAmount));

			if (stats.getCount() > 0) {

				statistics.setMax(stats.getMax());
				statistics.setMin(stats.getMin());
				statistics.setAvg(stats.getAverage());
				statistics.setSum(stats.getSum());
				statistics.setCount(stats.getCount());
			}
		}

		return statistics;

	}

	/*this method will check if the transaction is
	 * older than 60 seconds if not it will add to the array.
	 * Method will return a boolean which further checked in controller to send proper http status*/
	@Override
	public boolean addTransaction(Transaction transaction) {

		boolean isTransactionOlderThanSixtySeconds = isTransactionOlderThanSixtySeconds(
				transaction, Instant.now());

		if (!isTransactionOlderThanSixtySeconds) {
			transactions.add(transaction);
		}

		return isTransactionOlderThanSixtySeconds;

	}

	private boolean isTransactionOlderThanSixtySeconds(Transaction transaction,
			Instant now) {

		Instant epochTime = Instant.ofEpochMilli(transaction.getTimestamp());
		Duration duration = Duration.between(now, epochTime).abs();

		if (duration.getSeconds() > 60)
			return true;

		return false;

	}

}