package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.StaticHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.provider.HfqlRestProvider;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import jakarta.annotation.Nonnull;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OverlayTestApp {

	private static AnnotationConfigApplicationContext ourAppCtx;

	/**
	 * Set to true if you want to have Patient operations that have
	 * search parameters.
	 */
	private static final boolean USE_CUSTOM_PATIENT_PROVIDER = true;

	public static void main(String[] args) throws Exception {
		IHfqlExecutor hfqlExecutor = mock(IHfqlExecutor.class);
		List<String> columnNames = List.of("family", "given");
		List<HfqlDataTypeEnum> columnTypes = List.of(HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.JSON);
		List<List<Object>> rows = List.of(
			List.of("Simpson", "[\"Homer\", \"Jay\"]"),
			List.of("Simpson", "[\"Bart\", \"Barto\"]")
		);
		when(hfqlExecutor.executeInitialSearch(any(), any(), any())).thenAnswer(t-> {
			Thread.sleep(1000);
			return new StaticHfqlExecutionResult("the-search-id", columnNames, columnTypes, rows);
		});

		{
			int myPort = 8888;
			Server server = new Server(myPort);

			WebAppContext overlayHandler = new WebAppContext();
			overlayHandler.setContextPath("/testpage/base");
			overlayHandler.setDescriptor("hapi-fhir-testpage-overlay/src/test/resources/web.xml");
			overlayHandler.setBaseResourceAsString("hapi-fhir-testpage-overlay/src/main/webapp");
			overlayHandler.setParentLoaderPriority(true);

			FhirContext ctx = FhirContext.forR4Cached();
			RestfulServer restfulServer = new RestfulServer(ctx);
			restfulServer.registerProvider(new ProviderWithRequiredAndOptional());
			restfulServer.registerProvider(USE_CUSTOM_PATIENT_PROVIDER ? new PatientTestResourceProvider(ctx) : new HashMapResourceProvider<>(ctx, Patient.class));
			restfulServer.registerProvider(new HfqlRestProvider(hfqlExecutor));

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");
			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restfulServer);
			proxyHandler.addServlet(servletHolder, "/fhir/*");

			server.setHandler(new Handler.Sequence(overlayHandler, proxyHandler));

			server.start();
		}
	}

	public static class PatientTestResourceProvider extends HashMapResourceProvider<Patient> {

		/**
		 * Constructor
		 *
		 * @param theFhirContext The FHIR context
		 */
		public PatientTestResourceProvider(FhirContext theFhirContext) {
			super(theFhirContext, Patient.class);
		}

		@Description(shortDefinition = "This is a provider endpoint with parameters for searching on patients to display")
		@Search
		public IBundleProvider findPatients(@RequiredParam(name = Patient.SP_ACTIVE) TokenAndListParam theType,
											@Description(shortDefinition = "A portion of the given name of the patient")
											@OptionalParam(name = "given")
											StringAndListParam theGiven,
											@Description(shortDefinition = "A portion of the family name of the patient")
											@OptionalParam(name = "family")
											StringAndListParam theFamily,
											RequestDetails theRequestDetails
		) throws Exception {
			return searchAll(theRequestDetails);
		}

	}

	public static class ProviderWithRequiredAndOptional implements IResourceProvider {

		@Description(shortDefinition = "This is a query by date!")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatient(@RequiredParam(name = DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) TokenParam thePatientId, @OptionalParam(name = DiagnosticReport.SP_CODE) TokenOrListParam theNames,
																						 @OptionalParam(name = DiagnosticReport.SP_DATE) DateRangeParam theDateRange, @IncludeParam(allow = {"DiagnosticReport.result"}) Set<Include> theIncludes) throws Exception {
			return getDiagnosticReports();
		}

		@Description(shortDefinition = "This is a query by issued.. blah blah foo bar blah blah")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatientIssued(@RequiredParam(name = DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) TokenParam thePatientId, @OptionalParam(name = DiagnosticReport.SP_CODE) TokenOrListParam theNames,
																								 @OptionalParam(name = DiagnosticReport.SP_ISSUED) DateRangeParam theDateRange, @IncludeParam(allow = {"DiagnosticReport.result"}) Set<Include> theIncludes) throws Exception {
			return getDiagnosticReports();
		}

		@Description(shortDefinition = "This is a query by issued.. blah blah foo bar blah blah")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatientIssued() throws Exception {
			return getDiagnosticReports();
		}

		@Nonnull
		private List<DiagnosticReport> getDiagnosticReports() {
			ArrayList<DiagnosticReport> retVal = new ArrayList<>();

			DiagnosticReport dr = new DiagnosticReport();
			dr.addResult().setReference("Observation/123");
			dr.setId("DiagnosticReport/1");
			retVal.add(dr);

			dr = new DiagnosticReport();
			dr.addResult().setReference("Observation/123");
			dr.setId("DiagnosticReport/2");
			retVal.add(dr);

			return retVal;
		}

		@Override
		public Class<DiagnosticReport> getResourceType() {
			return DiagnosticReport.class;
		}

	}

}
