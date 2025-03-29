package ca.uhn.fhir;

import ca.uhn.fhir.batch2.jobs.imprt.BulkImportAppCtx;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.ActivateJobResult;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.dao.GZipUtil;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.server.BaseJettyServerExtension;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NcqaBulkImportTest extends BaseJpaR4Test {

	@RegisterExtension
	private HttpServletExtension myServer = new HttpServletExtension()
		.withServlet(new FileServlet());


	@Test
	public void testImport() throws InterruptedException, IOException {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		BulkImportJobParameters params = new BulkImportJobParameters();
		params.addNdJsonUrl(myServer.getBaseUrl() + "?filename=" + "/Users/james/tmp/ncqa/All_Bulk_Import_Files/LSC/Claim.ndjson.gz");
		params.setMaxBatchResourceCount(10);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(params);

		Batch2JobStartResponse outcome = myJobCoordinator.startInstance(new SystemRequestDetails(), request);

		JobInstance instance = myBatch2JobHelper.awaitJobCompletion(outcome.getInstanceId(), 600);
		assertNotNull(instance);
		assertEquals(StatusEnum.COMPLETED, instance.getStatus());

		Thread.sleep(1000000);
	}


	private static class FileServlet extends HttpServlet {

		@Serial
		private static final long serialVersionUID = 1707356074169877290L;

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String filename = req.getParameter("filename");
			ourLog.info("Servlet fetching file: {}", filename);
			byte[] bytes = FileUtils.readFileToByteArray(new File(filename));
			String contents = GZipUtil.decompress(bytes);

			resp.setContentType("text/plain");
			resp.getWriter().write(contents);
			resp.getWriter().close();
		}
	}
}
