package zingg.common.core.executor;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import zingg.common.client.IArguments;
import zingg.common.client.Arguments;
import zingg.common.client.ArgumentsUtil;
import zingg.common.client.ClientOptions;
import zingg.common.client.ZinggClientException;
import zingg.common.client.pipe.Pipe;
import zingg.common.client.util.DFObjectUtil;
import zingg.common.core.executor.validate.ExecutorValidator;

public class ExecutorTester<S, D, R, C, T>{

	public static final Log LOG = LogFactory.getLog(ExecutorTester.class);
	
	public ZinggBase<S, D, R, C, T> executor;
	public ExecutorValidator<S, D, R, C, T> validator;
	protected IArguments args;
	protected String configFile;
	protected String modelId;
	protected DFObjectUtil<S,D,R,C> dfObjectUtil;
	
	public ExecutorTester(ZinggBase<S, D, R, C, T> executor,ExecutorValidator<S, D, R, C, T> validator, String configFile, String modelId, DFObjectUtil<S,D,R,C> dfObjectUtil) throws ZinggClientException, IOException {
		this.executor = executor;
		this.validator = validator;
		this.configFile = configFile;
		this.modelId = modelId;
		this.dfObjectUtil = dfObjectUtil;
		setupArgs();
	}

	public IArguments setupArgs(String configFile, String phase) throws ZinggClientException, IOException {
		args = new ArgumentsUtil<Arguments>(Arguments.class).createArgumentsFromJSON(getClass().getClassLoader().getResource(configFile).getFile(), phase);
		args = updateLocation(args);
		args.setModelId(modelId);
		return args;
	}

	public IArguments updateLocation(IArguments args){
		for (Pipe p: args.getData()) {
			if (p.getProps().containsKey("location")) {
				String testOneFile = getClass().getClassLoader().getResource(p.get("location")).getFile();
				// correct the location of test data
				p.setProp("location", testOneFile);
			}
		}
		return args;
	}

	public void setupArgs() throws ZinggClientException, IOException{
		this.args = setupArgs(configFile, "");
	}

	public void initAndExecute(S session) throws ZinggClientException {
		executor.init(args,session, new ClientOptions());
		executor.execute();
	}
	
	public void validateResults() throws ZinggClientException {
		validator.validateResults();
	}	
	
}
