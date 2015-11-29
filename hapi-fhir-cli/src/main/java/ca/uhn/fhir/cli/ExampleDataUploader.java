package ca.uhn.fhir.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.fusesource.jansi.Ansi;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import com.phloc.commons.io.file.FileUtils;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryRequest;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.GZipContentInterceptor;
import ca.uhn.fhir.util.ResourceReferenceInfo;

public class ExampleDataUploader extends BaseCommand {

	private static final String SPEC_DEFAULT_VERSION = "dstu2";

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

//		opt = new Option("c", "cache", true, "Store a copy of the downloaded example pack on the local disk using a file of the given name. Use this file instead of fetching it from the internet if the file already exists.");
//		opt.setRequired(false);
//		options.addOption(opt);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws Exception {
		String specVersion = theCommandLine.getOptionValue("f", SPEC_DEFAULT_VERSION);
		String targetServer = theCommandLine.getOptionValue("t");
		if (isBlank(targetServer)) {
			throw new ParseException("No target server (-t) specified");
		} else if (targetServer.startsWith("http") == false && targetServer.startsWith("file") == false) {
			throw new ParseException("Invalid target server specified, must begin with 'http' or 'file'");
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

		FhirContext ctx;
		String specUrl;

		if (SPEC_DEFAULT_VERSION.equals(specVersion)) {
			ctx = FhirContext.forDstu2();
			specUrl = "http://hl7.org/fhir/" + specVersion + "/examples-json.zip";
		} else if ("dstu2.1".equals(specVersion)) {
			ctx = FhirContext.forDstu2_1();
			specUrl = "http://hl7-fhir.github.io/examples-json.zip";
		} else {
			throw new ParseException("Unknown spec version: " + specVersion);
		}

		ourLog.info("HTTP fetching: {}", specUrl);

		HttpGet get = new HttpGet(specUrl);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse result = client.execute(get);

		if (result.getStatusLine().getStatusCode() != 200) {
			throw new CommandFailureException("Got HTTP " + result.getStatusLine().getStatusCode() + " response code loading " + specUrl);
		}
		Bundle bundle = new Bundle();
		{
			byte[] inputBytes = readStreamFromInternet(result);
			
			IOUtils.closeQuietly(result.getEntity().getContent());

			ourLog.info("Successfully Loaded example pack ({} bytes)", inputBytes.length);

			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(inputBytes));
			byte[] buffer = new byte[2048];

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

				if (ctx.getResourceDefinition(parsed).getName().equals("Bundle")) {
					BaseRuntimeChildDefinition entryChildDef = ctx.getResourceDefinition(parsed).getChildByName("entry");
					BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");

					for (IBase nextEntry1 : entryChildDef.getAccessor().getValues(parsed)) {
						List<IBase> resources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry1);
						if (resources == null) {
							continue;
						}
						for (IBase nextResource : resources) {
							if (!ctx.getResourceDefinition(parsed).getName().equals("Bundle") && ctx.getResourceDefinition(parsed).getName().equals("SearchParameter")) {
								bundle.addEntry().setRequest(new EntryRequest().setMethod(HTTPVerbEnum.POST)).setResource((IResource) nextResource);
							}
						}
					}
				} else {
					if (ctx.getResourceDefinition(parsed).getName().equals("SearchParameter")) {
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
				if ("DataElement".equals(next.getResource().getResourceName()) || "OperationOutcome".equals(next.getResource().getResourceName()) || "OperationDefinition".equals(next.getResource().getResourceName())) {
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
			Map<String, String> renames = new HashMap<String, String>();
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
					next.getResource().setId("");
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
					nextRef.getResourceReference().setResource(null);
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
			System.gc();
		}

		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info("Final bundle: {} entries", bundle.getEntry().size());
		ourLog.info("Final bundle: {}", FileUtils.getFileSizeDisplay(encoded.length(), 1));

		if (targetServer.startsWith("file://")) {
			String path = targetServer.substring("file://".length());
			ourLog.info("Writing bundle to: {}", path);
			File file = new File(path);
			if (file.exists()) {
				throw new Exception("File already exists: " + file.getAbsolutePath());
			}
			FileWriter w = new FileWriter(file, false);
			w.append(encoded);
			w.close();
		} else {
			ourLog.info("Uploading bundle to server: " + targetServer);

			IGenericClient fhirClient = newClient(ctx, targetServer);
			fhirClient.registerInterceptor(new GZipContentInterceptor());

			long start = System.currentTimeMillis();
			;
			fhirClient.transaction().withBundle(bundle).execute();
			long delay = System.currentTimeMillis() - start;

			ourLog.info("Finished uploading bundle to server (took {} ms)", delay);
		}
	}

	private byte[] readStreamFromInternet(CloseableHttpResponse result) throws IOException {
		byte[] inputBytes;
		{
		long maxLength = result.getEntity().getContentLength();
		int nextLog = -1;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = result.getEntity().getContent().read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		  if (buffer.size() > nextLog) {
			  System.err.print("\r" + Ansi.ansi().eraseLine());
			  System.err.print(FileUtils.getFileSizeDisplay(buffer.size(), 1));
			  if (maxLength > 0) {
				  System.err.print(" [");
				  int stars = (int)(50.0f * ((float)buffer.size() / (float)maxLength));
				  for (int i = 0; i < stars; i++) {
					  System.err.print("*");
				  }
				  for (int i = stars; i < 50; i++) {
					  System.err.print(" ");
				  }
				  System.err.print("]");
			  }
			  System.err.flush();
			  nextLog += 100000;
		  }
		}
		buffer.flush();
		inputBytes = buffer.toByteArray();
		}
		System.err.println();
		System.err.flush();
		return inputBytes;
	}

}
