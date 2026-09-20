package org.hl7.fhir.r6.hapi.ctx;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.model.core.Coding;
import org.hl7.fhir.model.core.IdType;
import org.hl7.fhir.model.core.Reference;
import org.hl7.fhir.model.core.Resource;
import org.hl7.fhir.model.core.StructureDefinition;
import org.hl7.fhir.r6.hapi.fhirpath.FhirPathR6;
import org.hl7.fhir.r6.hapi.rest.server.NormativeBundleFactory;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class FhirR6 implements IFhirVersion {

	private String myId;

	@Override
	public IFhirPath createFhirPathExecutor(FhirContext theFhirContext) {
		return new FhirPathR6(theFhirContext);
	}

	@Override
	public IBaseResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
		StructureDefinition retVal = new StructureDefinition();

		RuntimeResourceDefinition def = theRuntimeResourceDefinition;

		myId = def.getId();
		if (StringUtils.isBlank(myId)) {
			myId = theRuntimeResourceDefinition.getName().toLowerCase();
		}

		retVal.setId(new IdDt(myId));
		return retVal;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<List> getContainedType() {
		return List.class;
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		String path = "org/hl7/fhir/model/core/fhirversion.properties";
		return ClasspathUtil.loadResourceAsStream(path);
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ((Resource) theResource).getMeta().getLastUpdatedElement();
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/org/hl7/fhir/r6/model/schema";
	}

	@Override
	public Class<? extends IBaseReference> getResourceReferenceType() {
		return Reference.class;
	}

	@Override
	public Object getServerVersion() {
		return ReflectionUtil.newInstanceOfFhirServerType("org.hl7.fhir.r6.hapi.ctx.FhirServerR6");
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.R6;
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new NormativeBundleFactory(theContext);
	}

	@Override
	public IBaseCoding newCodingDt() {
		return new Coding();
	}

	@Override
	public IIdType newIdType() {
		return new IdType();
	}
}
