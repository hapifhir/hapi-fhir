package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageResourceParsingSvc {

	private final FhirContext myFhirContext;

	public PackageResourceParsingSvc(FhirContext theContext) {
		myFhirContext = theContext;
	}

	public List<IBaseResource> parseResourcesOfType(String type, NpmPackage pkg) {
		if (!pkg.getFolders().containsKey("package")) {
			return Collections.emptyList();
		}
		ArrayList<IBaseResource> resources = new ArrayList<>();
		List<String> filesForType = pkg.getFolders().get("package").getTypes().get(type);
		if (filesForType != null) {
			for (String file : filesForType) {
				try {
					byte[] content = pkg.getFolders().get("package").fetchFile(file);
					resources.add(myFhirContext.newJsonParser().parseResource(new String(content)));
				} catch (IOException e) {
					throw new InternalErrorException(Msg.code(1289) + "Cannot install resource of type " + type + ": Could not fetch file " + file, e);
				}
			}
		}
		return resources;
	}
}
