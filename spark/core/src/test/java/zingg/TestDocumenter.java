package zingg;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;

import zingg.common.client.Arguments;
import zingg.common.client.ArgumentsUtil;
import zingg.spark.core.executor.ZinggSparkTester;

public class TestDocumenter {

    @BeforeEach
    public void setUp(){

        try {
            ArgumentsUtil argsUtil = new ArgumentsUtil();
			args = argsUtil.createArgumentsFromJSON(getClass().getResource("/testDocumenter/config.json").getFile());
           	//fail("Exception was expected for missing config file");
		} catch (Throwable e) {
            e.printStackTrace();
			LOG.info("Unexpected exception received " + e.getMessage());
            fail(e.getMessage());
		}
    }

    /*
    @Test
    public void testOutput() throws Throwable{
        Documenter doc = new Documenter();
        doc.init(args, "");
        doc.setArgs(args);
        doc.execute();
    }
    */
}
