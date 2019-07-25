package ca.uhn.fhir.narrative;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.core.StringContains;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationRequest.MedicationRequestStatus;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;

public class DefaultThymeleafNarrativeGeneratorDstu3Test {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DefaultThymeleafNarrativeGeneratorDstu3Test.class);
	private DefaultThymeleafNarrativeGenerator myGen;

	@Before
	public void before() {
		myGen = new DefaultThymeleafNarrativeGenerator();
		myGen.setUseHapiServerConformanceNarrative(true);
		myGen.setIgnoreFailures(false);
		myGen.setIgnoreMissingTemplates(false);

		ourCtx.setNarrativeGenerator(myGen);
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testGeneratePatient() throws DataFormatException {
		Patient value = new Patient();

		value.addIdentifier().setSystem("urn:names").setValue("123456");
		value.addName().setFamily("blow").addGiven("joe").addGiven((String) null).addGiven("john");
		//@formatter:off
		value.addAddress()
			.addLine("123 Fake Street").addLine("Unit 1")
			.setCity("Toronto").setState("ON").setCountry("Canada");
		//@formatter:on

		value.setBirthDate(new Date());

		Narrative narrative = new Narrative();
		myGen.generateNarrative(ourCtx, value, narrative);
		String output = narrative.getDiv().getValueAsString();
		ourLog.info(output);
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\">joe john <b>BLOW </b></div>"));

	}


	@Test
	public void testGenerateDiagnosticReport() throws DataFormatException {
		DiagnosticReport value = new DiagnosticReport();
		value.getCode().setText("Some Diagnostic Report");

		value.addResult().setReference("Observation/1");
		value.addResult().setReference("Observation/2");
		value.addResult().setReference("Observation/3");

		Narrative narrative = new Narrative();
		myGen.generateNarrative(ourCtx, value, narrative);
		String output = narrative.getDiv().getValueAsString();

		ourLog.info(output);
		assertThat(output, StringContains.containsString(value.getCode().getTextElement().getValue()));
	}

	@Test
	public void testGenerateOperationOutcome() {
		//@formatter:off
		String parse = "<OperationOutcome xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <issue>\n" + 
				"      <severity value=\"error\"/>\n" + 
				"      <diagnostics value=\"ca.uhn.fhir.rest.server.exceptions.InternalErrorException: Failed to call access method&#xa;&#xa;ca.uhn.fhir.rest.server.exceptions.InternalErrorException: Failed to call access method&#xa;&#x9;at ca.uhn.fhir.rest.method.BaseMethodBinding.invokeServerMethod(BaseMethodBinding.java:199)&#xa;&#x9;at ca.uhn.fhir.rest.method.HistoryMethodBinding.invokeServer(HistoryMethodBinding.java:162)&#xa;&#x9;at ca.uhn.fhir.rest.method.BaseResourceReturningMethodBinding.invokeServer(BaseResourceReturningMethodBinding.java:228)&#xa;&#x9;at ca.uhn.fhir.rest.method.HistoryMethodBinding.invokeServer(HistoryMethodBinding.java:1)&#xa;&#x9;at ca.uhn.fhir.rest.server.RestfulServer.handleRequest(RestfulServer.java:534)&#xa;&#x9;at ca.uhn.fhir.rest.server.RestfulServer.doGet(RestfulServer.java:141)&#xa;&#x9;at javax.servlet.http.HttpServlet.service(HttpServlet.java:687)&#xa;&#x9;at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)&#xa;&#x9;at org.apache.catalina.core.StandardWrapper.service(StandardWrapper.java:1682)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:344)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:214)&#xa;&#x9;at org.ebaysf.web.cors.CORSFilter.handleNonCORS(CORSFilter.java:437)&#xa;&#x9;at org.ebaysf.web.cors.CORSFilter.doFilter(CORSFilter.java:172)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:256)&#xa;&#x9;at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:214)&#xa;&#x9;at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:316)&#xa;&#x9;at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:160)&#xa;&#x9;at org.apache.catalina.core.StandardPipeline.doInvoke(StandardPipeline.java:734)&#xa;&#x9;at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:673)&#xa;&#x9;at com.sun.enterprise.web.WebPipeline.invoke(WebPipeline.java:99)&#xa;&#x9;at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:174)&#xa;&#x9;at org.apache.catalina.connector.CoyoteAdapter.doService(CoyoteAdapter.java:357)&#xa;&#x9;at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:260)&#xa;&#x9;at com.sun.enterprise.v3.services.impl.ContainerMapper.service(ContainerMapper.java:188)&#xa;&#x9;at org.glassfish.grizzly.http.server.HttpHandler.runService(HttpHandler.java:191)&#xa;&#x9;at org.glassfish.grizzly.http.server.HttpHandler.doHandle(HttpHandler.java:168)&#xa;&#x9;at org.glassfish.grizzly.http.server.HttpServerFilter.handleRead(HttpServerFilter.java:189)&#xa;&#x9;at org.glassfish.grizzly.filterchain.ExecutorResolver$9.execute(ExecutorResolver.java:119)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.executeFilter(DefaultFilterChain.java:288)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.executeChainPart(DefaultFilterChain.java:206)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.execute(DefaultFilterChain.java:136)&#xa;&#x9;at org.glassfish.grizzly.filterchain.DefaultFilterChain.process(DefaultFilterChain.java:114)&#xa;&#x9;at org.glassfish.grizzly.ProcessorExecutor.execute(ProcessorExecutor.java:77)&#xa;&#x9;at org.glassfish.grizzly.nio.transport.TCPNIOTransport.fireIOEvent(TCPNIOTransport.java:838)&#xa;&#x9;at org.glassfish.grizzly.strategies.AbstractIOStrategy.fireIOEvent(AbstractIOStrategy.java:113)&#xa;&#x9;at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy.run0(WorkerThreadIOStrategy.java:115)&#xa;&#x9;at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy.access$100(WorkerThreadIOStrategy.java:55)&#xa;&#x9;at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy$WorkerThreadRunnable.run(WorkerThreadIOStrategy.java:135)&#xa;&#x9;at org.glassfish.grizzly.threadpool.AbstractThreadPool$Worker.doWork(AbstractThreadPool.java:564)&#xa;&#x9;at org.glassfish.grizzly.threadpool.AbstractThreadPool$Worker.run(AbstractThreadPool.java:544)&#xa;&#x9;at java.lang.Thread.run(Thread.java:722)&#xa;Caused by: java.lang.reflect.InvocationTargetException&#xa;&#x9;at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)&#xa;&#x9;at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)&#xa;&#x9;at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)&#xa;&#x9;at java.lang.reflect.Method.invoke(Method.java:601)&#xa;&#x9;at ca.uhn.fhir.rest.method.BaseMethodBinding.invokeServerMethod(BaseMethodBinding.java:194)&#xa;&#x9;... 40 more&#xa;Caused by: java.lang.NumberFormatException: For input string: &quot;3cb9a027-9e02-488d-8d01-952553d6be4e&quot;&#xa;&#x9;at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)&#xa;&#x9;at java.lang.Long.parseLong(Long.java:441)&#xa;&#x9;at java.lang.Long.parseLong(Long.java:483)&#xa;&#x9;at ca.uhn.fhir.model.primitive.IdDt.getIdPartAsLong(IdDt.java:194)&#xa;&#x9;at ca.uhn.fhir.jpa.provider.JpaResourceProvider.getHistoryForResourceInstance(JpaResourceProvider.java:81)&#xa;&#x9;... 45 more&#xa;\"/>\n" + 
				"   </issue>\n" + 
				"   <issue>\n" + 
				"      <severity value=\"warning\"/>\n" + 
				"      <diagnostics value=\"YThis is a warning\"/>\n" + 
				"   </issue>\n" + 
				"</OperationOutcome>";
		//@formatter:on

		OperationOutcome oo = ourCtx.newXmlParser().parseResource(OperationOutcome.class, parse);

		// String output = gen.generateTitle(oo);
		// ourLog.info(output);
		// assertEquals("Operation Outcome (2 issues)", output);

		Narrative narrative = new Narrative();
		myGen.generateNarrative(ourCtx, oo, narrative);
		String output = narrative.getDiv().getValueAsString();

		ourLog.info(output);

		assertThat(output, containsString("<td><pre>YThis is a warning</pre></td>"));
	}


	@Test
	public void testGenerateDiagnosticReportWithObservations() throws DataFormatException {
		DiagnosticReport value = new DiagnosticReport();

		value.getIssuedElement().setValueAsString("2011-02-22T11:13:00");
		value.setStatus(DiagnosticReportStatus.FINAL);

		value.getCode().setText("Some & Diagnostic Report");
		{
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("1938HB").setDisplay("Hemoglobin");
			obs.setValue(new Quantity(null, 2.223, null, null, "mg/L"));
			obs.addReferenceRange().setLow((SimpleQuantity) new SimpleQuantity().setValue(2.20)).setHigh((SimpleQuantity) new SimpleQuantity().setValue(2.99));
			obs.setStatus(ObservationStatus.FINAL);
			obs.setComment("This is a result comment");

			Reference result = value.addResult();
			result.setResource(obs);
		}
		{
			Observation obs = new Observation();
			obs.setValue(new StringType("HELLO!"));
			value.addResult().setResource(obs);
		}
		{
			Observation obs = new Observation();
			obs.setCode(new CodeableConcept().addCoding(new Coding("AA", "BB", null)));
			value.addResult().setResource(obs);
		}

		Narrative narrative = new Narrative();
		myGen.generateNarrative(ourCtx, value, narrative);
		String output = narrative.getDiv().getValueAsString();

		ourLog.info(output);
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\"> Some &amp; Diagnostic Report </div>"));

	}

	@Test
	@Ignore
	public void testGenerateMedicationPrescription() {
		MedicationRequest mp = new MedicationRequest();
		mp.setId("12345");
		Medication med = new Medication();
		med.getCode().setText("ciproflaxin");
		Reference medRef = new Reference(med);
		mp.setMedication(medRef);
		mp.setStatus(MedicationRequestStatus.ACTIVE);
		mp.setAuthoredOnElement(new DateTimeType("2014-09-01"));

		Narrative narrative = new Narrative();
		myGen.generateNarrative(ourCtx, mp, narrative);

		assertTrue("Expected medication name of ciprofloaxin within narrative: " + narrative.getDiv().toString(), narrative.getDiv().toString().indexOf("ciprofloaxin") > -1);
		assertTrue("Expected string status of ACTIVE within narrative: " + narrative.getDiv().toString(), narrative.getDiv().toString().indexOf("ACTIVE") > -1);

	}

	@Test
	public void testGenerateMedication() {
		Medication med = new Medication();
		med.getCode().setText("ciproflaxin");

		Narrative narrative = new Narrative();
		myGen.generateNarrative(ourCtx, med, narrative);

		String string = narrative.getDiv().getValueAsString();
		assertThat(string, containsString("ciproflaxin"));

	}

}
