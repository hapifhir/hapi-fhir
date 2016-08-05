package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;

@ResourceDef(name = "DiagnosticReport", profile = CustomDiagnosticReportDstu2.PROFILE)
public class CustomDiagnosticReportDstu2 extends DiagnosticReport {

	public static final String PROFILE = "http://custom_DiagnosticReport";

	private static final long serialVersionUID = 1L;

}