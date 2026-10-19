package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

public class BulkImportFileServletTest {

	private BulkImportFileServlet mySvc = new BulkImportFileServlet();

	static final String ourInput = "{\"resourceType\":\"Patient\", \"id\": \"A\", \"active\": true}\n" +
		"{\"resourceType\":\"Patient\", \"id\": \"B\", \"active\": false}";

	@RegisterExtension
	private HttpServletExtension myServletExtension = new HttpServletExtension()
		.withServlet(mySvc)
		.withContextPath("/context")
		.withServletPath("/base/path/*");

	@BeforeEach
	public void beforeEach() {
		mySvc.clearFiles();
	}

	@Test
	public void testDownloadFile() {

		String index = mySvc.registerFileByContents(ourInput);

		String path = "/download?index=" + index;

		executeBulkImportAndCheckReturnedContentType(path);

	}


	private void executeBulkImportAndCheckReturnedContentType(String thePath) {
		HttpTestResponse response = myServletExtension.request(thePath).get()
			.assertStatus(200)
			.assertBodyEquals(ourInput);

		String responseHeaderContentType = response.getHeader(Constants.HEADER_CONTENT_TYPE);
		assertThat(responseHeaderContentType).isEqualTo(BulkImportFileServlet.DEFAULT_HEADER_CONTENT_TYPE);
	}


	@Test
	public void testInvalidRequests() {
		myServletExtension.request("/blah").get()
			.assertStatus(404)
			.assertBodyEquals("Failed to handle response. See server logs for details.");

		myServletExtension.request("/foo").get()
			.assertStatus(404)
			.assertBodyEquals("Failed to handle response. See server logs for details.");

		myServletExtension.request("/download").get()
			.assertStatus(404)
			.assertBodyEquals("Failed to handle response. See server logs for details.");

		myServletExtension.request("/download?").get()
			.assertStatus(404)
			.assertBodyEquals("Failed to handle response. See server logs for details.");

		myServletExtension.request("/download?index=").get()
			.assertStatus(404)
			.assertBodyEquals("Failed to handle response. See server logs for details.");

		myServletExtension.request("/download?index=A").get()
			.assertStatus(404)
			.assertBodyEquals("Failed to handle response. See server logs for details.");

		myServletExtension.request("/download?index=22").get()
			.assertStatus(404)
			.assertBodyEquals("Failed to handle response. See server logs for details.");

	}

}
