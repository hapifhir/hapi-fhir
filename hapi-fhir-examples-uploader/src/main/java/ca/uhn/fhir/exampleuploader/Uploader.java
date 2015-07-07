package ca.uhn.fhir.exampleuploader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryTransaction;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.util.ResourceReferenceInfo;

public class Uploader {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Uploader.class);

	public static void main(String[] args) throws Exception {
		ourLog.info("Starting...");

		FhirContext ctx = FhirContext.forDstu2();

		HttpGet get = new HttpGet("http://hl7.org/fhir/2015May/examples-json.zip");
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse result = client.execute(get);
		byte[] bytes = IOUtils.toByteArray(result.getEntity().getContent());
		IOUtils.closeQuietly(result.getEntity().getContent());

		ourLog.info("Loaded examples ({} bytes)", bytes.length);

		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
		byte[] buffer = new byte[2048];

		Bundle bundle = new Bundle();

		while (true) {
			ZipEntry nextEntry = zis.getNextEntry();
			if (nextEntry == null) {
				break;
			}

			int len = 0;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((len = zis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			byte[] exampleBytes = bos.toByteArray();
			String exampleString = new String(exampleBytes, "UTF-8");

			IBaseResource parsed;
			try {
				parsed = ctx.newJsonParser().parseResource(exampleString);
			} catch (DataFormatException e) {
				ourLog.info("FAILED to parse example {}", nextEntry.getName(), e);
				continue;
			}
			ourLog.info("Found example {} - {} - {} chars", nextEntry.getName(), parsed.getClass().getSimpleName(), exampleString.length());

			if (parsed instanceof Bundle) {
				Bundle b = (Bundle) parsed;
				for (Entry nextEntry1 : b.getEntry()) {
					if (nextEntry1.getResource() == null) {
						continue;
					}
					if (nextEntry1.getResource() instanceof Bundle) {
						continue;
					}
					if (nextEntry1.getResource() instanceof SearchParameter) {
						continue;
					}
					bundle.addEntry().setTransaction(new EntryTransaction().setMethod(HTTPVerbEnum.POST)).setResource(nextEntry1.getResource());
				}
			} else {
				if (parsed instanceof SearchParameter) {
					continue;
				}
				bundle.addEntry().setTransaction(new EntryTransaction().setMethod(HTTPVerbEnum.POST)).setResource((IResource) parsed);
			}

		}

		Set<String> ids = new HashSet<String>();
		for (int i = 0; i < bundle.getEntry().size(); i++) {
			Entry next = bundle.getEntry().get(i);
			if (next.getResource().getId().getIdPart() != null) {
				String nextId = next.getResource().getResourceName() + '/' + next.getResource().getId().getIdPart();
				if (!ids.add(nextId)) {
					ourLog.info("Discarding duplicate resource with ID: " + nextId);
					bundle.getEntry().remove(i);
					i--;
				}
			}
		}

		int goodRefs = 0;
		for (Entry next : bundle.getEntry()) {
			List<ResourceReferenceInfo> refs = ctx.newTerser().getAllResourceReferences(next.getResource());
			for (ResourceReferenceInfo nextRef : refs) {
//				if (nextRef.getResourceReference().getReferenceElement().isAbsolute()) {
//					ourLog.info("Discarding absolute reference: {}", nextRef.getResourceReference().getReferenceElement().getValue());
//					nextRef.getResourceReference().getReferenceElement().setValue(null);
//				}
				nextRef.getResourceReference().getReferenceElement().setValue(nextRef.getResourceReference().getReferenceElement().toUnqualifiedVersionless().getValue());
				String value = nextRef.getResourceReference().getReferenceElement().toUnqualifiedVersionless().getValue();
				if (!ids.contains(value) && !nextRef.getResourceReference().getReferenceElement().isLocal()) {
					ourLog.info("Discarding unknown reference: {}", value);
					nextRef.getResourceReference().getReferenceElement().setValue(null);
				} else {
					goodRefs++;
				}
			}
		}
		
//		for (Entry next : bundle.getEntry()) {
//			if (next.getResource().getId().hasIdPart() && Character.isLetter(next.getResource().getId().getIdPart().charAt(0))) {
//				next.getTransaction().setUrl(next.getResource().getResourceName() + '/' + next.getResource().getId().getIdPart());
//				next.getTransaction().setMethod(HTTPVerbEnum.PUT);
//			}
//		}		

		ourLog.info("{} good references", goodRefs);

		String encoded = ctx.newJsonParser().encodeResourceToString(bundle);
		ourLog.info("Final bundle: {} entries", bundle.getEntry().size());
		ourLog.info("Final bundle: {} chars", encoded.length());

		IGenericClient fhirClient = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
		fhirClient.transaction().withBundle(bundle).execute();

	}

}
