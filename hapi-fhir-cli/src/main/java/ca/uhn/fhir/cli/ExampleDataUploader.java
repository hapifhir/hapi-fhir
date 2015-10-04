package ca.uhn.fhir.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryRequest;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.util.ResourceReferenceInfo;

public class ExampleDataUploader extends BaseCommand {

	private static final String SPEC_DEFAULT_VERSION = "2015Sep";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExampleDataUploader.class);

	@Override
	public String getCommandDescription() {
		return "Downloads the resource example pack from the HL7.org FHIR specification website, and uploads all of the example resources to a given server.";
	}

	@Override
	public String getCommandName() {
		return "upload-examples";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		Option opt;

		opt = new Option("f", "fhirversion", true, "Spec version to upload (default is '" + SPEC_DEFAULT_VERSION + "')");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("t", "target", true, "Base URL for the target server (e.g. \"http://example.com/fhir\")");
		opt.setRequired(true);
		options.addOption(opt);

		opt = new Option("l", "limit", true, "Sets a limit to the number of resources the uploader will try to upload");
		opt.setRequired(false);
		options.addOption(opt);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws Exception {
		String specVersion = theCommandLine.getOptionValue("f", SPEC_DEFAULT_VERSION);
		String targetServer = theCommandLine.getOptionValue("t");
		if (isBlank(targetServer)) {
			throw new ParseException("No target server (-t) specified");
		} else if (targetServer.startsWith("http") == false) {
			throw new ParseException("Invalid target server specified, must begin with 'http'");
		}
		Integer limit = null;
		String limitString = theCommandLine.getOptionValue('l');
		if (isNotBlank(limitString)) {
			try {
			limit = Integer.parseInt(limitString);
			} catch (NumberFormatException e) {
				throw new ParseException("Invalid number for limit (-l) option, must be a number: " + limitString);
			}
		}

		FhirContext ctx = FhirContext.forDstu2();
		String specUrl = "http://hl7.org/fhir/" + specVersion + "/examples-json.zip";

		ourLog.info("HTTP fetching: {}", specUrl);

		HttpGet get = new HttpGet(specUrl);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse result = client.execute(get);

		if (result.getStatusLine().getStatusCode() != 200) {
			throw new CommandFailureException("Got HTTP " + result.getStatusLine().getStatusCode() + " response code loading " + specUrl);
		}

		byte[] bytes = IOUtils.toByteArray(result.getEntity().getContent());
		IOUtils.closeQuietly(result.getEntity().getContent());

		ourLog.info("Successfully Loaded example pack ({} bytes)", bytes.length);

		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
		byte[] buffer = new byte[2048];

		Bundle bundle = new Bundle();

		int count = 0;
		while (true) {
			count++;
			if (limit != null && count > limit) {
				break;
			}
			
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

			if (ourLog.isTraceEnabled()) {
				ourLog.trace("Next example: " + exampleString);
			}

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
					bundle.addEntry().setRequest(new EntryRequest().setMethod(HTTPVerbEnum.POST)).setResource(nextEntry1.getResource());
				}
			} else {
				if (parsed instanceof SearchParameter) {
					continue;
				}
				bundle.addEntry().setRequest(new EntryRequest().setMethod(HTTPVerbEnum.POST)).setResource((IResource) parsed);
			}

		}

		Map<String, Integer> ids = new HashMap<String, Integer>();
		Set<String> fullIds = new HashSet<String>();
		for (int i = 0; i < bundle.getEntry().size(); i++) {
			Entry next = bundle.getEntry().get(i);
			
			// DataElement have giant IDs that seem invalid, need to investigate this..
			if ("DataElement".equals(next.getResource().getResourceName()) || "OperationOutcome".equals(next.getResource().getResourceName())
					 || "OperationDefinition".equals(next.getResource().getResourceName())) {
				ourLog.info("Skipping " + next.getResource().getResourceName() + " example");
				bundle.getEntry().remove(i);
				i--;
			} else {
				IdDt resourceId = next.getResource().getId();
				if (!fullIds.add(resourceId.toUnqualifiedVersionless().getValue())) {
					ourLog.info("Discarding duplicate resource: " + resourceId.getValue());
					bundle.getEntry().remove(i);
					i--;
					continue;
				}
				
				String idPart = resourceId.getIdPart();
				if (idPart != null) {
					if (!ids.containsKey(idPart)) {
						ids.put(idPart, 1);
					} else {
						ids.put(idPart, ids.get(idPart) + 1);
					}
				} else {
					ourLog.info("Discarding resource with not explicit ID");
					bundle.getEntry().remove(i);
					i--;
				}
			}
		}

		Set<String> qualIds = new HashSet<String>();
		Map<String, String> renames = new HashMap<String,String>(); 
		for (int i = 0; i < bundle.getEntry().size(); i++) {
			Entry next = bundle.getEntry().get(i);
			if (next.getResource().getId().getIdPart() != null) {
				String idPart = next.getResource().getId().getIdPart();
				String originalId = next.getResource().getResourceName() + '/' + idPart;
				if (ids.get(idPart) > 1 || next.getResource().getId().isIdPartValidLong()) {
					idPart = next.getResource().getResourceName() + idPart;
				}
				String nextId = next.getResource().getResourceName() + '/' + idPart;
				if (!qualIds.add(nextId)) {
					ourLog.info("Discarding duplicate resource with ID: " + nextId);
					bundle.getEntry().remove(i);
					i--;
				}
				next.getRequest().setMethod(HTTPVerbEnum.PUT);
				next.getRequest().setUrl(nextId);
				renames.put(originalId, nextId);
			}
		}

		int goodRefs = 0;
		for (Entry next : bundle.getEntry()) {
			List<ResourceReferenceInfo> refs = ctx.newTerser().getAllResourceReferences(next.getResource());
			for (ResourceReferenceInfo nextRef : refs) {
				// if (nextRef.getResourceReference().getReferenceElement().isAbsolute()) {
				// ourLog.info("Discarding absolute reference: {}",
				// nextRef.getResourceReference().getReferenceElement().getValue());
				// nextRef.getResourceReference().getReferenceElement().setValue(null);
				// }
				nextRef.getResourceReference().getReferenceElement().setValue(nextRef.getResourceReference().getReferenceElement().toUnqualifiedVersionless().getValue());
				String value = nextRef.getResourceReference().getReferenceElement().toUnqualifiedVersionless().getValue();
				if (!qualIds.contains(value) && !nextRef.getResourceReference().getReferenceElement().isLocal()) {
					if (renames.containsKey(value)) {
						nextRef.getResourceReference().setReference(renames.get(value));
						goodRefs++;
					} else {
					ourLog.info("Discarding unknown reference: {}", value);
					nextRef.getResourceReference().getReferenceElement().setValue(null);
					}
				} else {
					goodRefs++;
				}
			}
		}

		// for (Entry next : bundle.getEntry()) {
		// if (next.getResource().getId().hasIdPart() &&
		// Character.isLetter(next.getResource().getId().getIdPart().charAt(0))) {
		// next.getTransaction().setUrl(next.getResource().getResourceName() + '/' +
		// next.getResource().getId().getIdPart());
		// next.getTransaction().setMethod(HTTPVerbEnum.PUT);
		// }
		// }

		ourLog.info("{} good references", goodRefs);

		String encoded = ctx.newJsonParser().encodeResourceToString(bundle);
		ourLog.info("Final bundle: {} entries", bundle.getEntry().size());
		ourLog.info("Final bundle: {} chars", encoded.length());

		ourLog.info("Uploading bundle to server: " + targetServer);

		IGenericClient fhirClient = newClient(ctx, targetServer);

		long start = System.currentTimeMillis();
		;
		fhirClient.transaction().withBundle(bundle).execute();
		long delay = System.currentTimeMillis() - start;

		ourLog.info("Finished uploading bundle to server (took {} ms)", delay);
	}

}
