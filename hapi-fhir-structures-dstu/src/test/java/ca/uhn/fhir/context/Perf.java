package ca.uhn.fhir.context;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;

public class Perf {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Perf.class);
	
	public static void main(String[] args) throws Exception {
		
		FhirContext ctx = FhirContext.forDstu1();
		
		String str = IOUtils.toString(Perf.class.getResourceAsStream("/contained-diagnosticreport-singlecontainedelement.xml"));
		DiagnosticReport rept = ctx.newXmlParser().parseResource(DiagnosticReport.class, str);
		
		long start = System.currentTimeMillis();
				
		for (int i = 0; i < 100000; i++) {
			if (i % 10 == 0) {
				ourLog.info("Rep: " + i);
			}
			ctx.newTerser().getAllPopulatedChildElementsOfType(rept, BaseResourceReferenceDt.class);
		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Took: {} ms", delay);
	}
	
}
