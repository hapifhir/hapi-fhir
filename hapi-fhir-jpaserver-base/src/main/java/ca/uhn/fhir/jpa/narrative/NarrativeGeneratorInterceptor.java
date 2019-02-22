package ca.uhn.fhir.jpa.narrative;

import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.narrative2.NarrativeTemplateManifest;

import java.io.IOException;

@Interceptor
public class NarrativeGeneratorInterceptor {

	private String myManifestFile;

	/**
	 * Sets the filename/classpath name of the manifect properties
	 * file that defines the available narrative templates
	 *
	 * @param theManifestFile The filename or classpath name
	 */
	public void setManifestFile(String theManifestFile) {
		myManifestFile = theManifestFile;
	}

	@Hook(Pointcut.REGISTERED)
	public void start() throws IOException {
		propertyFileParser = new NarrativeTemplateManifest(myManifestFile);
	}

}
