package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyDisplayPopulationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ca.uhn.fhir.jpa.provider.r4.TerminologyUploaderProviderR4Test.createLoincZip;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.*;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class TerminologyInlineMappingR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyInlineMappingR4Test.class);
	public static final String CS_URI_LABCORP = "http://terminology.labcorp.com/CodeSystem/lcls-test-code";

	@Test
	void testUploadLoinc() throws Exception {
		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);

		ResponseTerminologyDisplayPopulationInterceptor displayInterceptor =
			new ResponseTerminologyDisplayPopulationInterceptor(myValidationSupport);
		myServer.registerInterceptor(displayInterceptor);

		//		byte[] packageBytes = createLoincZip();
//
//		Parameters respParam = myClient
//			.operation()
//			.onType(CodeSystem.class)
//			.named("upload-external-code-system")
//			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.LOINC_URI))
//			.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("file.zip").setData(packageBytes))
//			.execute();
//
//		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
//		ourLog.info(resp);

		CodeSystem loinc = new CodeSystem();
		loinc.setId("loinc");
		loinc.setUrl("http://loinc.org");
		loinc.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		loinc.setVersion("2.68");
		loinc
			.addConcept()
			.setCode("000005")
			.setDisplay("Loinc Bacterial Display Name");
		myClient.update().resource(loinc).execute();

		CodeSystem labCorp = new CodeSystem();
		labCorp.setId("labcorp");
		labCorp.setUrl(CS_URI_LABCORP);
		labCorp.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myClient.update().resource(labCorp).execute();

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URI_LABCORP);

		CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept();
		concept.setCode("000005");
		concept.setDisplay("BACTERIAL(Bill Code Only)");
		concept.addProperty()
				.setCode("LOINC_CD")
				.setValue(new Coding().setSystem("http://loinc.org").setCode("000005"));
		concept.addProperty()
				.setCode("TEST_STATUS")
			.setValue(new CodeType("A"));
		concept.addProperty()
				.setCode("PROCEDURE_CLASS")
			.setValue(new CodeType("GN"));

		myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(CS_URI_LABCORP))
			.andParameter(TerminologyUploaderProvider.PARAM_CODESYSTEM, codeSystem)
			.prettyPrint()
			.execute();

		Parameters response = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_LOOKUP)
			.withParameter(Parameters.class, "system", new UriType(CS_URI_LABCORP))
			.andParameter("code", new CodeType("000005"))
			.andParameter("property", new CodeType("LOINC_CD"))
			.useHttpGet()
			.execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
	}


}
