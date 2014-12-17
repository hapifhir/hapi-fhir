package ca.uhn.fhir.model.dstu;

import java.io.InputStream;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

import javax.servlet.http.HttpServletRequest;

public class FhirDstu1 implements IFhirVersion {

	@Override
	public Object createServerConformanceProvider(RestfulServer theServer) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		InputStream str = FhirDstu1.class.getResourceAsStream("/ca/uhn/fhir/model/dstu/fhirversion.properties");
		if (str == null) {
			str = FhirDstu1.class.getResourceAsStream("ca/uhn/fhir/model/dstu/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu/model.properties");
		}
		return str;
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU1;
	}
}
