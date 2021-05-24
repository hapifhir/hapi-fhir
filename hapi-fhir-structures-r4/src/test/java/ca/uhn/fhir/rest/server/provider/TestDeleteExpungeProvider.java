package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.BaseR4ServerTest;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDeleteExpungeProvider extends BaseR4ServerTest {
	private static final Logger ourLog = LoggerFactory.getLogger(TestDeleteExpungeProvider.class);
	private MyDeleteExpungeJobSubmitter mySvc = new MyDeleteExpungeJobSubmitter();
	private Parameters myReturnParameters;

	@BeforeEach
	public void reset() {
		myReturnParameters = new Parameters();
		myReturnParameters.addParameter("success", true);
		mySvc.reset();
	}

	@Test
	public void testDeleteExpunge() throws Exception {
		// setup
		Parameters input = new Parameters();
		String url1 = "Patient?active=false";
		String url2 = "Patient?active=false";
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url1);
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url2);

		DeleteExpungeProvider provider = new DeleteExpungeProvider(mySvc);
		startServer(provider);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		assertTrue(response.getParameterBool("success"));
		assertThat(mySvc.calledWith, hasSize(2));
		assertEquals(url1, mySvc.calledWith.get(0));
		assertEquals(url2, mySvc.calledWith.get(1));
	}

	private class MyDeleteExpungeJobSubmitter implements IDeleteExpungeJobSubmitter {
		public List<String> calledWith;

		@Override
		public IBaseParameters submitJob(List<IPrimitiveType<String>> theUrlsToExpungeDelete) {
			theUrlsToExpungeDelete.forEach(t -> calledWith.add(t.getValue()));
			return myReturnParameters;
		}

		public void reset() {
			calledWith = new ArrayList<>();
		}
	}
}
