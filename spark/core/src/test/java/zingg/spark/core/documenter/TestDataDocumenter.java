package zingg.spark.core.documenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import zingg.common.core.documenter.DataDocumenter;
import zingg.common.core.documenter.TemplateFields;
import zingg.spark.core.TestSparkBase;
import zingg.common.client.Arguments;
import zingg.common.client.ArgumentsUtil;
import zingg.common.client.IArguments;
import zingg.common.client.pipe.FilePipe;
import zingg.common.client.pipe.Pipe;

@ExtendWith(TestSparkBase.class)
public class TestDataDocumenter {

	public static final Log LOG = LogFactory.getLog(TestDataDocumenter.class);

	private IArguments docArguments = new Arguments();
	@BeforeEach
	public void setUp(){
		try {
			String configPath = getClass().getResource("../../../../documenter/config.json").getFile();
			ArgumentsUtil argsUtil = new ArgumentsUtil();
			docArguments = argsUtil.createArgumentsFromJSON(configPath);
		} catch (Throwable e) {
			e.printStackTrace();
			LOG.info("Unexpected exception received " + e.getMessage());
			fail(e.getMessage());
		}
	}

	@Test
	public void testPopulateTemplateData() throws Throwable {
		
		DataDocumenter<SparkSession, Dataset<Row>, Row, Column, DataType> dataDoc = new SparkDataDocumenter(TestSparkBase.zsCTX, docArguments);
		Pipe[] dataPipeArr = docArguments.getData();
		
		for (int i = 0; i < dataPipeArr.length; i++) {
			String file = getClass().getResource("../../../../documenter/test.csv").getFile();
			dataPipeArr[i].setProp(FilePipe.LOCATION, file);
		}

		dataDoc.setData(TestSparkBase.zsCTX.getPipeUtil().read(false, false, dataPipeArr));


		Map<String, Object> root =  dataDoc.populateTemplateData();
		
		assertTrue(root.containsKey(TemplateFields.TITLE), "The field does not exist - " + TemplateFields.TITLE);
		assertEquals(DataDocumenter.TEMPLATE_TITLE, root.get(TemplateFields.TITLE));

		assertTrue(root.containsKey(TemplateFields.MODEL_ID), "The field does not exist - " + TemplateFields.MODEL_ID);
		assertEquals(docArguments.getModelId(), root.get(TemplateFields.MODEL_ID));

		assertTrue(root.containsKey(TemplateFields.DATA_FIELDS_LIST), "The field does not exist. - " + TemplateFields.DATA_FIELDS_LIST);
	}
}
