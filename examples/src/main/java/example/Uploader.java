package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.List;

public class Uploader {
	private static final Logger ourLog = LoggerFactory.getLogger(Uploader.class);
	public static void main(String[] theArgs) throws FileNotFoundException {

		FhirContext ctx = FhirContext.forDstu2();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/baseDstu2/");

		Collection<File> files = FileUtils.listFiles(new File("/home/james/tmp/download"), new String[]{"json"}, false);
		for (File nextFile : files) {
			Bundle bundle = (Bundle) ctx.newJsonParser().parseResource(new FileReader(nextFile));
			for (Bundle.Entry nextEntry : bundle.getEntry()) {
				IBaseResource nextResource = nextEntry.getResource();
				IIdType oldId = nextResource.getIdElement();
				String newId = oldId.getResourceType() + "/A" + oldId.getIdPart();
				ourLog.info("Changing resource ID from {} to {}", oldId.toUnqualifiedVersionless(), newId);
				nextResource.setId(newId);

				List<ResourceReferenceInfo> refs = ctx.newTerser().getAllResourceReferences(nextResource);
				for (ResourceReferenceInfo nextRefInfo : refs) {
					IIdType nextRef = nextRefInfo.getResourceReference().getReferenceElement();
					String newRef = nextRef.getResourceType() + "/A" + nextRef.getIdPart();
					ourLog.info("Changing ref from {} to {}", nextRef.getValue(), newRef);
					nextRefInfo.getResourceReference().setReference(newRef);
				}

				nextEntry.getRequest().setMethod(HTTPVerbEnum.PUT);
				nextEntry.getRequest().setUrl(newId);
				nextEntry.setFullUrl(newId);

			}

			bundle.setType(BundleTypeEnum.TRANSACTION);
			ourLog.info("Uploading transaction for {}", nextFile.getName());
			client.transaction().withBundle(bundle).execute();
		}


	}

}
