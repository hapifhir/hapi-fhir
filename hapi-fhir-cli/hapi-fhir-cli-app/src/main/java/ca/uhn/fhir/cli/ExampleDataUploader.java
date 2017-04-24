package ca.uhn.fhir.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.fusesource.jansi.Ansi;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryRequest;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class ExampleDataUploader extends BaseCommand {

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

		addFhirVersionOption(options);

		opt = new Option("t", "target", true, "Base URL for the target server (e.g. \"http://example.com/fhir\")");
		opt.setRequired(true);
		options.addOption(opt);

		opt = new Option("l", "limit", true, "Sets a limit to the number of resources the uploader will try to upload");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("d", "data", true, "Local *.zip containing file to use to upload");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("c", "cache", false,
				"Cache the downloaded examples-json.zip file in the ~/.hapi-fhir-cli/cache directory. Use this file for 12 hours if it exists, instead of fetching it from the internet.");
		opt.setRequired(false);
		options.addOption(opt);

		// opt = new Option("c", "cache", true, "Store a copy of the downloaded example pack on the local disk using a file of the given name. Use this file instead of fetching it from the internet if
		// the file already exists.");
		// opt.setRequired(false);
		// options.addOption(opt);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws Exception {
		FhirContext ctx = getSpecVersionContext(theCommandLine);

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

		String specUrl;

		switch (ctx.getVersion().getVersion()) {
		case DSTU2:
			specUrl = "http://hl7.org/fhir/dstu2/examples-json.zip";
			break;
		case DSTU3:
			specUrl = "http://build.fhir.org/examples-json.zip";
			break;
		default:
			throw new ParseException("Invalid spec version for this command: " + ctx.getVersion().getVersion());
		}

		String filepath = theCommandLine.getOptionValue('d');

		boolean cacheFile = theCommandLine.hasOption('c');

		String userHomeDir = System.getProperty("user.home");

		File applicationDir = new File(userHomeDir + File.separator + "." + "hapi-fhir-cli");
		FileUtils.forceMkdir(applicationDir);

		if (isNotBlank(filepath)) {
			ourLog.info("Loading from local path: {}", filepath);

			if (filepath.startsWith("~" + File.separator)) {
				filepath = userHomeDir + filepath.substring(1);
			}

			File suppliedFile = new File(FilenameUtils.normalize(filepath));

			if (suppliedFile.isDirectory()) {
				Collection<File> inputFiles;
				inputFiles = FileUtils.listFiles(suppliedFile, new String[] { "zip" }, false);

				for (File inputFile : inputFiles) {
					IBaseBundle bundle = getBundleFromFile(limit, inputFile, ctx);
					processBundle(ctx, bundle);
					sendBundleToTarget(targetServer, ctx, bundle);
				}
			} else {
				IBaseBundle bundle = getBundleFromFile(limit, suppliedFile, ctx);
				processBundle(ctx, bundle);
				sendBundleToTarget(targetServer, ctx, bundle);
			}

		} else {

			File cacheDir = new File(applicationDir, "cache");
			FileUtils.forceMkdir(cacheDir);

			File inputFile = new File(cacheDir, "examples-json-" + ctx.getVersion().getVersion() + ".zip");

			Date cacheExpiryDate = DateUtils.addHours(new Date(), -12);

			if (!inputFile.exists() | (cacheFile && FileUtils.isFileOlder(inputFile, cacheExpiryDate))) {

				File exampleFileDownloading = new File(cacheDir, "examples-json-" + ctx.getVersion().getVersion() + ".zip.partial");

				HttpGet get = new HttpGet(specUrl);
				CloseableHttpClient client = HttpClientBuilder.create().build();
				CloseableHttpResponse result = client.execute(get);

				if (result.getStatusLine().getStatusCode() != 200) {
					throw new CommandFailureException("Got HTTP " + result.getStatusLine().getStatusCode() + " response code loading " + specUrl);
				}

				ourLog.info("Downloading from remote url: {}", specUrl);
				downloadFileFromInternet(result, exampleFileDownloading);

				FileUtils.deleteQuietly(inputFile);
				FileUtils.moveFile(exampleFileDownloading, inputFile);

				if (!cacheFile) {
					inputFile.deleteOnExit();
				}

				ourLog.info("Successfully Loaded example pack ({})", FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(inputFile)));
				IOUtils.closeQuietly(result.getEntity().getContent());
			}

			IBaseBundle bundle = getBundleFromFile(limit, inputFile, ctx);
			processBundle(ctx, bundle);

			sendBundleToTarget(targetServer, ctx, bundle);

		}

	}

	private IBaseBundle getBundleFromFile(Integer theLimit, File theSuppliedFile, FhirContext theCtx) throws ParseException, UnsupportedEncodingException, IOException {
		switch (theCtx.getVersion().getVersion()) {
		case DSTU2:
			return getBundleFromFileDstu2(theLimit, theSuppliedFile, theCtx);
		case DSTU3:
			return getBundleFromFileDstu3(theLimit, theSuppliedFile, theCtx);
		default:
			throw new ParseException("Invalid spec version for this command: " + theCtx.getVersion().getVersion());
		}
	}

	private void sendBundleToTarget(String targetServer, FhirContext ctx, IBaseBundle bundle) throws Exception, IOException {
		List<IBaseResource> resources = BundleUtil.toListOfResources(ctx, bundle);

		for (Iterator<IBaseResource> iter = resources.iterator(); iter.hasNext();) {
			IBaseResource next = iter.next();
			String nextType = ctx.getResourceDefinition(next).getName();
			if (nextType.endsWith("Definition")) {
				iter.remove();
			} else if (nextType.contains("ValueSet")) {
				iter.remove();
			} else if (nextType.equals("CodeSystem")) {
				iter.remove();
			}
		}

		List<IBaseResource> subResourceList = new ArrayList<IBaseResource>();
		while (resources.size() > 0) {

			IBaseResource nextAddedResource = resources.remove(0);
			subResourceList.add(nextAddedResource);

			Set<String> checkedTargets = new HashSet<String>();

			for (int i = 0; i < subResourceList.size(); i++) {
				IBaseResource nextCandidateSource = subResourceList.get(i);
				for (ResourceReferenceInfo nextRef : ctx.newTerser().getAllResourceReferences(nextCandidateSource)) {
					String nextRefResourceType = nextRef.getResourceReference().getReferenceElement().getResourceType();
					if (isBlank(nextRefResourceType)) {
						nextRef.getResourceReference().setResource(null);
						nextRef.getResourceReference().setReference(null);
						continue;
					}
					String nextRefIdPart = nextRef.getResourceReference().getReferenceElement().getIdPart();
					if (nextRefIdPart.startsWith("EX")) {
						nextRefIdPart = nextRefIdPart.substring(2);
					}
					String nextTarget = nextRefResourceType + "/EX" + nextRefIdPart;
					nextRef.getResourceReference().setResource(null);
					nextRef.getResourceReference().setReference(nextTarget);
					if (checkedTargets.add(nextTarget) == false) {
						continue;
					}

					boolean found = false;
					for (int j = 0; j < resources.size(); j++) {
						String candidateTarget = resources.get(j).getIdElement().getValue();
						if (isNotBlank(nextTarget) && nextTarget.equals(candidateTarget)) {
							ourLog.info("Reflexively adding resource {} to bundle as it is a reference target", nextTarget);
							subResourceList.add(resources.remove(j));
							found = true;
							break;
						}
					}
				}
			}

			if (subResourceList.size() < 10 && resources.size() > 0) {
				subResourceList.add(resources.remove(0));
				continue;
			}

			ourLog.info("About to upload {} examples in a transaction, {} remaining", subResourceList.size(), resources.size());

			IVersionSpecificBundleFactory bundleFactory = ctx.newBundleFactory();
			bundleFactory.initializeBundleFromResourceList(null, subResourceList, null, null, 0, BundleTypeEnum.TRANSACTION);
			IBaseResource subBundle = bundleFactory.getResourceBundle();

			String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(subBundle);
			ourLog.info("Final bundle: {}", FileUtils.byteCountToDisplaySize(encoded.length()));

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
				try {
					fhirClient.transaction().withBundle(encoded).execute();
				} catch (BaseServerResponseException e) {
					ourLog.error("Failed to upload bundle:HTTP " + e.getStatusCode() + ": " + e.getMessage());
					ourLog.error("Failing bundle: {}", encoded);
				}
				long delay = System.currentTimeMillis() - start;

				ourLog.info("Finished uploading bundle to server (took {} ms)", delay);
			}

			subResourceList.clear();
		}

	}

	private void processBundle(FhirContext ctx, IBaseBundle bundle) {
		switch (ctx.getVersion().getVersion()) {
		case DSTU2:
			processBundleDstu2(ctx, (Bundle) bundle);
			break;
		case DSTU3:
			processBundleDstu3(ctx, (org.hl7.fhir.dstu3.model.Bundle) bundle);
			break;
		default:
			throw new IllegalStateException();
		}
	}

	private void processBundleDstu2(FhirContext ctx, Bundle bundle) {

		Map<String, Integer> ids = new HashMap<String, Integer>();
		Set<String> fullIds = new HashSet<String>();

		for (Iterator<Entry> iterator = bundle.getEntry().iterator(); iterator.hasNext();) {
			Entry next = iterator.next();

			// DataElement have giant IDs that seem invalid, need to investigate this..
			if ("Subscription".equals(next.getResource().getResourceName()) || "DataElement".equals(next.getResource().getResourceName())
					|| "OperationOutcome".equals(next.getResource().getResourceName()) || "OperationDefinition".equals(next.getResource().getResourceName())) {
				ourLog.info("Skipping " + next.getResource().getResourceName() + " example");
				iterator.remove();
			} else {
				IdDt resourceId = new IdDt(next.getResource().getResourceName() + "/EX" + next.getResource().getId().getIdPart());
				if (!fullIds.add(resourceId.toUnqualifiedVersionless().getValue())) {
					ourLog.info("Discarding duplicate resource: " + resourceId.getValue());
					iterator.remove();
					continue;
				}

				String idPart = resourceId.getIdPart();
				if (idPart != null) {
					next.getResource().setId(resourceId);
				} else {
					ourLog.info("Discarding resource with not explicit ID");
					iterator.remove();
				}
			}
		}
		Set<String> qualIds = new TreeSet<String>();
		for (Iterator<Entry> iterator = bundle.getEntry().iterator(); iterator.hasNext();) {
			Entry next = iterator.next();
			if (next.getResource().getId().getIdPart() != null) {
				String nextId = next.getResource().getId().getValue();
				next.getRequest().setMethod(HTTPVerbEnum.PUT);
				next.getRequest().setUrl(nextId);
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

				if (isNotBlank(value)) {
					if (!qualIds.contains(value) && !nextRef.getResourceReference().getReferenceElement().isLocal()) {
						ourLog.info("Discarding unknown reference: {}", value);
						nextRef.getResourceReference().getReferenceElement().setValue(null);
					} else {
						goodRefs++;
					}
				}
				ourLog.info("Found ref: {}", value);
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

		ourLog.info("Final bundle: {} entries", bundle.getEntry().size());

	}

	private void processBundleDstu3(FhirContext ctx, org.hl7.fhir.dstu3.model.Bundle bundle) {

		Map<String, Integer> ids = new HashMap<String, Integer>();
		Set<String> fullIds = new HashSet<String>();

		for (Iterator<BundleEntryComponent> iterator = bundle.getEntry().iterator(); iterator.hasNext();) {
			BundleEntryComponent next = iterator.next();

			// DataElement have giant IDs that seem invalid, need to investigate this..
			if ("Subscription".equals(next.getResource().getResourceType()) || "DataElement".equals(next.getResource().getResourceType())
					|| "OperationOutcome".equals(next.getResource().getResourceType()) || "OperationDefinition".equals(next.getResource().getResourceType())) {
				ourLog.info("Skipping " + next.getResource().getResourceType() + " example");
				iterator.remove();
			} else {
				IdDt resourceId = new IdDt(next.getResource().getResourceType() + "/EX" + next.getResource().getIdElement().getIdPart());
				if (!fullIds.add(resourceId.toUnqualifiedVersionless().getValue())) {
					ourLog.info("Discarding duplicate resource: " + resourceId.getValue());
					iterator.remove();
					continue;
				}

				String idPart = resourceId.getIdPart();
				if (idPart != null) {
					next.getResource().setId(resourceId);
				} else {
					ourLog.info("Discarding resource with not explicit ID");
					iterator.remove();
				}
			}
		}
		Set<String> qualIds = new TreeSet<String>();
		for (Iterator<BundleEntryComponent> iterator = bundle.getEntry().iterator(); iterator.hasNext();) {
			BundleEntryComponent next = iterator.next();
			if (next.getResource().getIdElement().getIdPart() != null) {
				String nextId = next.getResource().getIdElement().getValue();
				next.getRequest().setMethod(HTTPVerb.PUT);
				next.getRequest().setUrl(nextId);
			}
		}

		int goodRefs = 0;
		for (BundleEntryComponent next : bundle.getEntry()) {
			List<ResourceReferenceInfo> refs = ctx.newTerser().getAllResourceReferences(next.getResource());
			for (ResourceReferenceInfo nextRef : refs) {
				// if (nextRef.getResourceReference().getReferenceElement().isAbsolute()) {
				// ourLog.info("Discarding absolute reference: {}",
				// nextRef.getResourceReference().getReferenceElement().getValue());
				// nextRef.getResourceReference().getReferenceElement().setValue(null);
				// }
				nextRef.getResourceReference().setResource(null);
				String value = nextRef.getResourceReference().getReferenceElement().toUnqualifiedVersionless().getValue();

				if (isNotBlank(value)) {
					if (!qualIds.contains(value) && !nextRef.getResourceReference().getReferenceElement().isLocal()) {
						ourLog.info("Discarding unknown reference: {}", value);
						nextRef.getResourceReference().getReferenceElement().setValue(null);
					} else {
						goodRefs++;
					}
				}
				ourLog.info("Found ref: {}", value);
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

		ourLog.info("Final bundle: {} entries", bundle.getEntry().size());

	}

	private Bundle getBundleFromFileDstu2(Integer limit, File inputFile, FhirContext ctx) throws IOException, UnsupportedEncodingException {

		Bundle bundle = new Bundle();

		ZipInputStream zis = new ZipInputStream(FileUtils.openInputStream(inputFile));
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
		return bundle;
	}

	@SuppressWarnings("unchecked")
	private org.hl7.fhir.dstu3.model.Bundle getBundleFromFileDstu3(Integer limit, File inputFile, FhirContext ctx) throws IOException, UnsupportedEncodingException {

		org.hl7.fhir.dstu3.model.Bundle bundle = new org.hl7.fhir.dstu3.model.Bundle();
		bundle.setType(BundleType.TRANSACTION);

		FhirValidator val = ctx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(new DefaultProfileValidationSupport()));

		ZipInputStream zis = new ZipInputStream(FileUtils.openInputStream(inputFile));
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
			} catch (Exception e) {
				ourLog.info("FAILED to parse example {}", nextEntry.getName(), e);
				continue;
			}
			ourLog.info("Found example {} - {} - {} chars", nextEntry.getName(), parsed.getClass().getSimpleName(), exampleString.length());

			ValidationResult result = val.validateWithResult(parsed);
			if (result.isSuccessful() == false) {
				ourLog.info("FAILED to validate example {} - {}", nextEntry.getName(), result.toString());
				continue;
			}

			if (ctx.getResourceDefinition(parsed).getName().equals("Bundle")) {
				BaseRuntimeChildDefinition entryChildDef = ctx.getResourceDefinition(parsed).getChildByName("entry");
				BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");

				for (IBase nextEntry1 : entryChildDef.getAccessor().getValues(parsed)) {
					List<IBase> resources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry1);
					if (resources == null) {
						continue;
					}
					for (IBase nextResource : resources) {
						if (nextResource == null) {
							continue;
						}
						if (!ctx.getResourceDefinition((Class<? extends IBaseResource>) nextResource.getClass()).getName().equals("Bundle")
								&& ctx.getResourceDefinition((Class<? extends IBaseResource>) nextResource.getClass()).getName().equals("SearchParameter")) {
							BundleEntryComponent entry = bundle.addEntry();
							entry.getRequest().setMethod(HTTPVerb.POST);
							entry.setResource((Resource) nextResource);
						}
					}
				}
			} else {
				if (ctx.getResourceDefinition(parsed).getName().equals("SearchParameter")) {
					continue;
				}
				BundleEntryComponent entry = bundle.addEntry();
				entry.getRequest().setMethod(HTTPVerb.POST);
				entry.setResource((Resource) parsed);
			}
		}
		return bundle;
	}

	private void downloadFileFromInternet(CloseableHttpResponse result, File localFile) throws IOException {
		FileOutputStream buffer = FileUtils.openOutputStream(localFile);
		try {

			long maxLength = result.getEntity().getContentLength();
			long nextLog = -1;
			// ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = result.getEntity().getContent().read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
				long fileSize = FileUtils.sizeOf(localFile);
				if (fileSize > nextLog) {
					System.err.print("\r" + Ansi.ansi().eraseLine());
					System.err.print(FileUtils.byteCountToDisplaySize(fileSize));
					if (maxLength > 0) {
						System.err.print(" [");
						int stars = (int) (50.0f * ((float) fileSize / (float) maxLength));
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

			System.err.println();
			System.err.flush();
		} finally {
			IOUtils.closeQuietly(buffer);
		}
	}

}
