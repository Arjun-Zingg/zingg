package zingg.common.core.executor.validate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import zingg.common.client.ZFrame;
import zingg.common.client.ZinggClientException;
import zingg.common.core.executor.TrainingDataFinder;

public class TrainingDataFinderValidator<S, D, R, C, T> extends ExecutorValidator<S, D, R, C, T> {

	public static final Log LOG = LogFactory.getLog(TrainingDataFinderValidator.class);
	
	public TrainingDataFinderValidator(TrainingDataFinder<S, D, R, C, T> executor) {
		super(executor);
	}

	@Override
	public void validateResults() throws ZinggClientException {
		// check that unmarked data has at least 10 rows
		ZFrame<D, R, C> df = executor.getContext().getPipeUtil().read(false, false, 
			((TrainingDataFinder<S, D, R, C, T>) executor).getUnmarkedLocation());
		
		long trainingDataCount = df.count();
		assertTrue(trainingDataCount > 10);
		LOG.info("trainingDataCount : "+ trainingDataCount);				
	}

}
