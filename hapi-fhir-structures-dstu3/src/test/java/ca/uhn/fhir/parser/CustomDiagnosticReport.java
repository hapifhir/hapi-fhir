package ca.uhn.fhir.parser;

import org.hl7.fhir.dstu3.model.DiagnosticReport;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "DiagnosticReport", profile = CustomDiagnosticReport.PROFILE)
public class CustomDiagnosticReport extends DiagnosticReport {

	public static final String PROFILE = "http://custom_DiagnosticReport";

	private static final long serialVersionUID = 1L;

}