package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class LocalFileValidationSupport implements IValidationSupport {

	final private FhirContext myCtx;

	public LocalFileValidationSupport(FhirContext ctx) {
		this.myCtx = ctx;
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

	@Nullable
	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		try {
			String contents = IOUtils.toString(new InputStreamReader(new FileInputStream(theUrl), "UTF-8"));
			return myCtx.newJsonParser().parseResource(contents);
		} catch (IOException e) {
			return null;
		}
	}
}
