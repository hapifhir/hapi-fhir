package ca.uhn.fhir.narrative;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.util.Date;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MedicationPrescriptionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;

public class DefaultThymeleafNarrativeGeneratorTest {
	private FhirContext myCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DefaultThymeleafNarrativeGeneratorTest.class);
	private DefaultThymeleafNarrativeGenerator gen;

	@Before
	public void before() {
		gen = new DefaultThymeleafNarrativeGenerator();
		gen.setUseHapiServerConformanceNarrative(true);
		gen.setIgnoreFailures(false);
		gen.setIgnoreMissingTemplates(false);

		myCtx = FhirContext.forDstu1();
		myCtx.setNarrativeGenerator(gen);
	}

	@Test
	public void testGeneratePatient() throws DataFormatException {
		Patient value = new Patient();

		value.addIdentifier().setSystem("urn:names").setValue("123456");
		value.addName().addFamily("blow").addGiven("joe").addGiven(null).addGiven("john");
		value.getAddressFirstRep().addLine("123 Fake Street").addLine("Unit 1");
		value.getAddressFirstRep().setCity("Toronto").setState("ON").setCountry("Canada");

		value.setBirthDate(new Date(), TemporalPrecisionEnum.DAY);

		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(myCtx, value, narrative);
		String output = narrative.getDiv().getValueAsString();
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\"> joe john <b>BLOW </b></div>"));

		// Removed because label is gone in DSTU2
//		value.getIdentifierFirstRep().setLabel("FOO MRN 123");
//		title = gen.generateTitle(value);
//		assertEquals("joe john BLOW (FOO MRN 123)", title);
//		ourLog.info(title);

	}

	@Test
	public void testGeneratePatientWithoutData() throws DataFormatException {
		Patient value = new Patient();

		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(myCtx, value, narrative);
		String output = narrative.getDiv().getValueAsString();
		assertThat(output, StringContains.containsString("<div>"));

		// Removed because label is gone in DSTU2
//		value.getIdentifierFirstRep().setLabel("FOO MRN 123");
//		title = gen.generateTitle(value);
//		assertEquals("joe john BLOW (FOO MRN 123)", title);
//		ourLog.info(title);

	}


	@Test
	public void testGenerateEncounter() throws DataFormatException {
		Encounter enc = new Encounter();

		enc.addIdentifier("urn:visits", "1234567");
		enc.setClassElement(EncounterClassEnum.AMBULATORY);
		enc.setPeriod(new PeriodDt().setStart(new DateTimeDt("2001-01-02T11:11:00")));
		enc.setType(EncounterTypeEnum.ANNUAL_DIABETES_MELLITUS_SCREENING);

	}

	@Test
	public void testGenerateOrganization() throws DataFormatException {
		Organization enc = new Organization();

		enc.addIdentifier("urn:visits", "1234567");
		enc.setName("Some Test Org");
		enc.addAddress().addLine("123 Fake St").setCity("Toronto").setState("ON").setCountry("Canada").setZip("12345");

	}

	@Test
	public void testGenerateServerConformance() throws DataFormatException {
		Conformance value = myCtx.newXmlParser().parseResource(Conformance.class, new InputStreamReader(getClass().getResourceAsStream("/server-conformance-statement.xml")));

		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(myCtx, value, narrative);
		String output =narrative.getDiv().getValueAsString();

		ourLog.info(output);
	}

	@Test
	public void testGenerateDiagnosticReport() throws DataFormatException {
		DiagnosticReport value = new DiagnosticReport();
		value.getName().setText("Some Diagnostic Report");

		value.addResult().setReference("Observation/1");
		value.addResult().setReference("Observation/2");
		value.addResult().setReference("Observation/3");

		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(myCtx, value, narrative);
		String output = narrative.getDiv().getValueAsString();

		ourLog.info(output);
		assertThat(output, StringContains.containsString(value.getName().getText().getValue()));
	}

	@Test
	public void testGenerateOperationOutcome() {
		//@formatter:off
		String parse = "<OperationOutcome xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <issue>\n" + 
				"      <severity value=\"error\"/>\n" + 
				"      <details value=\"ca.uhn.fhir.rest.server.exceptions.InternalErrorException: Failed to call access method&#xa;&#xa;ca.uhn.fhir.rest.server.exceptions.InternalErrorException: Failed to call access method&#xa;&#x9;at ca.uhn.fhir.rest.method.BaseMethodBinding.invokeServerMethod(BaseMethodBinding.java:199)&#xa;&#x9;at ca.uhn.fhir.rest.method.HistoryMethodBinding.invokeServer(HistoryMethodBinding.java:162)&#xa;&#x9;at ca.uhn.fhir.rest.method.BaseResourceReturningMethodBinding.invokeServer(BaseResourceReturningMethodBinding.java:228)&#xa;&#x9;at ca.uhn.fhir.rest.method.HistoryMethodBinding.invokeServer(HistoryMethodBinding.java:1)&#xa;&#x9;at ca.uhn.fhir.rest.server.RestfulServer.handleRequest(RestfulServer.java:534)&#xa;&#x9;at ca.uhn.fhir.rest.server.RestfulServer.doGet(RestfulServer.java:141)&#xa;&#x9;at javax.servlet.http.HttpServlet.service(HttpServlet.java:687)&#xa;&#x9;at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)&#xa;&#x9;at org.apache.catalina.core.StandardWrapper.service(StandardWrapper.java:1682)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:344)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:214)&#xa;&#x9;at org.ebaysf.web.cors.CORSFilter.handleNonCORS(CORSFilter.java:437)&#xa;&#x9;at org.ebaysf.web.cors.CORSFilter.doFilter(CORSFilter.java:172)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:256)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:214)&#xa;&#x9;at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:316)&#xa;&#x9;at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:160)&#xa;&#x9;at org.apache.catalina.core.StandardPipeline.doInvoke(StandardPipeline.java:734)&#xa;&#x9;at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:673)&#xa;&#x9;at com.sun.enterprise.web.WebPipeline.invoke(WebPipeline.java:99)&#xa;&#x9;at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:174)&#xa;&#x9;at org.apache.catalina.connector.CoyoteAdapter.doService(CoyoteAdapter.java:357)&#xa;&#x9;at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:260)&#xa;&#x9;at com.sun.enterprise.v3.services.impl.ContainerMapper.service(ContainerMapper.java:188)&#xa;&#x9;at org.glassfish.grizzly.http.server.HttpHandler.runService(HttpHandler.java:191)&#xa;&#x9;at org.glassfish.grizzly.http.server.HttpHandler.doHandle(HttpHandler.java:168)&#xa;&#x9;at org.glassfish.grizzly.http.server.HttpServerFilter.handleRead(HttpServerFilter.java:189)&#xa;&#x9;at org.glassfish.grizzly.filterchain.ExecutorResolver$9.execute(ExecutorResolver.java:119)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.executeFilter(DefaultFilterChain.java:288)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.executeChainPart(DefaultFilterChain.java:206)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.execute(DefaultFilterChain.java:136)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.process(DefaultFilterChain.java:114)&#xa;&#x9;at org.glassfish.grizzly.ProcessorExecutor.execute(ProcessorExecutor.java:77)&#xa;&#x9;at org.glassfish.grizzly.nio.transport.TCPNIOTransport.fireIOEvent(TCPNIOTransport.java:838)&#xa;&#x9;at org.glassfish.grizzly.strategies.AbstractIOStrategy.fireIOEvent(AbstractIOStrategy.java:113)&#xa;&#x9;at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy.run0(WorkerThreadIOStrategy.java:115)&#xa;&#x9;at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy.access$100(WorkerThreadIOStrategy.java:55)&#xa;&#x9;at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy$WorkerThreadRunnable.run(WorkerThreadIOStrategy.java:135)&#xa;&#x9;at org.glassfish.grizzly.threadpool.AbstractThreadPool$Worker.doWork(AbstractThreadPool.java:564)&#xa;&#x9;at org.glassfish.grizzly.threadpool.AbstractThreadPool$Worker.run(AbstractThreadPool.java:544)&#xa;&#x9;at java.lang.Thread.run(Thread.java:722)&#xa;Caused by: java.lang.reflect.InvocationTargetException&#xa;&#x9;at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)&#xa;&#x9;at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)&#xa;&#x9;at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)&#xa;&#x9;at java.lang.reflect.Method.invoke(Method.java:601)&#xa;&#x9;at ca.uhn.fhir.rest.method.BaseMethodBinding.invokeServerMethod(BaseMethodBinding.java:194)&#xa;&#x9;... 40 more&#xa;Caused by: java.lang.NumberFormatException: For input string: &quot;3cb9a027-9e02-488d-8d01-952553d6be4e&quot;&#xa;&#x9;at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)&#xa;&#x9;at java.lang.Long.parseLong(Long.java:441)&#xa;&#x9;at java.lang.Long.parseLong(Long.java:483)&#xa;&#x9;at ca.uhn.fhir.model.primitive.IdDt.getIdPartAsLong(IdDt.java:194)&#xa;&#x9;at ca.uhn.fhir.jpa.provider.JpaResourceProvider.getHistoryForResourceInstance(JpaResourceProvider.java:81)&#xa;&#x9;... 45 more&#xa;\"/>\n" + 
				"   </issue>\n" + 
				"   <issue>\n" + 
				"      <severity value=\"warning\"/>\n" + 
				"      <details value=\"YThis is a warning\"/>\n" + 
				"   </issue>\n" + 
				"</OperationOutcome>";
		//@formatter:on

		OperationOutcome oo = myCtx.newXmlParser().parseResource(OperationOutcome.class, parse);

		// String output = gen.generateTitle(oo);
		// ourLog.info(output);
		// assertEquals("Operation Outcome (2 issues)", output);

		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(myCtx, oo, narrative);
		String nar = narrative.getDiv().getValueAsString();
		ourLog.info(nar);

		// oo = new OperationOutcome();
		// oo.addIssue().setSeverity(IssueSeverityEnum.FATAL).setDetails("AA");
		// output = gen.generateTitle(oo);
		// ourLog.info(output);
		// assertEquals("Operation Outcome (fatal)", output);

	}

	@Test
	public void testGenerateDiagnosticReportWithObservations() throws DataFormatException {
		DiagnosticReport value = new DiagnosticReport();

		value.getIssued().setValueAsString("2011-02-22T11:13:00");
		value.setStatus(DiagnosticReportStatusEnum.FINAL);

		value.getName().setText("Some & Diagnostic Report");
		{
			Observation obs = new Observation();
			obs.getName().addCoding().setCode("1938HB").setDisplay("Hemoglobin");
			obs.setValue(new QuantityDt(null, 2.223, "mg/L"));
			obs.addReferenceRange().setLow(new QuantityDt(2.20)).setHigh(new QuantityDt(2.99));
			obs.setStatus(ObservationStatusEnum.FINAL);
			obs.setComments("This is a result comment");

			ResourceReferenceDt result = value.addResult();
			result.setResource(obs);
		}
		{
			Observation obs = new Observation();
			obs.setValue(new StringDt("HELLO!"));
			value.addResult().setResource(obs);
		}
		{
			Observation obs = new Observation();
			obs.setName(new CodeableConceptDt("AA", "BB"));
			value.addResult().setResource(obs);
		}
		
		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(myCtx, value, narrative);
		String output = narrative.getDiv().getValueAsString();

		ourLog.info(output);
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\"> Some &amp; Diagnostic Report </div>"));

		// Now try it with the parser

		output = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(value);
		ourLog.info(output);
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\"> Some &amp; Diagnostic Report </div>"));

	}

	@Test
	public void testGenerateMedicationPrescription() {
		MedicationPrescription mp = new MedicationPrescription();
		mp.setId("12345");
		Medication med = new Medication();
		med.setName("ciprofloaxin");
		ResourceReferenceDt medRef = new ResourceReferenceDt(med);
		mp.setMedication(medRef);
		mp.setStatus(MedicationPrescriptionStatusEnum.ACTIVE);
		mp.setDateWritten(new DateTimeDt("2014-09-01"));

		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(myCtx, mp, narrative);
		assertTrue("Expected medication name of ciprofloaxin within narrative: " + narrative.getDiv().toString(), narrative.getDiv().toString().indexOf("ciprofloaxin") > -1);
		assertTrue("Expected string status of ACTIVE within narrative: " + narrative.getDiv().toString(), narrative.getDiv().toString().indexOf("ACTIVE") > -1);

	}

}
