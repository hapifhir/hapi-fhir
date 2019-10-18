package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.binstore.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.hl7.fhir.r4.model.Binary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class BinaryStorageInterceptorR4Test extends BaseResourceProviderR4Test {

	public static final byte[] SOME_BYTES = {1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
	public static final byte[] SOME_BYTES_2 = {5, 5, 5, 6};
	private static final Logger ourLog = LoggerFactory.getLogger(BinaryStorageInterceptorR4Test.class);

	@Autowired
	private MemoryBinaryStorageSvcImpl myStorageSvc;
	@Autowired
	private IBinaryStorageSvc myBinaryStorageSvc;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		myStorageSvc.setMinimumBinarySize(10);
		myDaoConfig.setExpungeEnabled(true);
		myInterceptorRegistry.registerInterceptor(myBinaryStorageInterceptor);
	}

	@Override
	@After
	public void after() throws Exception {
		super.after();
		myStorageSvc.setMinimumBinarySize(0);
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
	}

	@Test
	public void testStoreAndRetrieveBinary_Externalized() {

		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(JpaConstants.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("data")));


	}


}
