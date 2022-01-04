package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryRequest;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ExampleDataUploader extends BaseRequestGeneratingCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExampleDataUploader.class);

	private IBaseBundle getBundleFromFile(Integer theLimit, File theSuppliedFile, FhirContext theCtx) throws ParseException, IOException {
		switch (theCtx.getVersion().getVersion()) {
			case DSTU2:
				return getBundleFromFileDstu2(theLimit, theSuppliedFile, theCtx);
			case DSTU3:
				return getBundleFromFileDstu3(theLimit, theSuppliedFile, theCtx);
			case R4:
				return getBundleFromFileR4(theLimit, theSuppliedFile, theCtx);
			default:
				throw new ParseException(Msg.code(1607) + "Invalid spec version for this command: " + theCtx.getVersion().getVersion());
		}
	}

	private Bundle getBundleFromFileDstu2(Integer limit, File inputFile, FhirContext ctx) throws IOException {

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

			int len;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((len = zis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			byte[] exampleBytes = bos.toByteArray();
			String exampleString = new String(exampleBytes, StandardCharsets.UTF_8);

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

			if (ctx.getResourceType(parsed).equals("Bundle")) {
				BaseRuntimeChildDefinition entryChildDef = ctx.getResourceDefinition(parsed).getChildByName("entry");
				BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");

				for (IBase nextEntry1 : entryChildDef.getAccessor().getValues(parsed)) {
					List<IBase> resources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry1);
					if (resources == null) {
						continue;
					}
					for (IBase nextResource : resources) {
						if (!ctx.getResourceType(parsed).equals("Bundle") && ctx.getResourceType(parsed).equals("SearchParameter")) {
							bundle.addEntry().setRequest(new EntryRequest().setMethod(HTTPVerbEnum.POST)).setResource((IResource) nextResource);
						}
					}
				}
			} else {
				if (ctx.getResourceType(parsed).equals("SearchParameter")) {
					continue;
				}
				bundle.addEntry().setRequest(new EntryRequest().setMethod(HTTPVerbEnum.POST)).setResource((IResource) parsed);
			}
		}
		return bundle;
	}

	@SuppressWarnings("unchecked")
	private org.hl7.fhir.dstu3.model.Bundle getBundleFromFileDstu3(Integer limit, File inputFile, FhirContext ctx) throws IOException {

		org.hl7.fhir.dstu3.model.Bundle bundle = new org.hl7.fhir.dstu3.model.Bundle();
		bundle.setType(BundleType.TRANSACTION);

		FhirValidator val = ctx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(new DefaultProfileValidationSupport(ctx)));

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

			int len;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((len = zis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			byte[] exampleBytes = bos.toByteArray();
			String exampleString = new String(exampleBytes, StandardCharsets.UTF_8);

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
			if (! result.isSuccessful()) {
				ourLog.info("FAILED to validate example {} - {}", nextEntry.getName(), result);
				continue;
			}

			if (ctx.getResourceType(parsed).equals("Bundle")) {
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
				if (ctx.getResourceType(parsed).equals("SearchParameter")) {
					continue;
				}
				BundleEntryComponent entry = bundle.addEntry();
				entry.getRequest().setMethod(HTTPVerb.POST);
				entry.setResource((Resource) parsed);
			}
		}
		return bundle;
	}

	@SuppressWarnings("unchecked")
	private org.hl7.fhir.r4.model.Bundle getBundleFromFileR4(Integer limit, File inputFile, FhirContext ctx) throws IOException {

		org.hl7.fhir.r4.model.Bundle bundle = new org.hl7.fhir.r4.model.Bundle();
		bundle.setType(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION);

		FhirValidator val = ctx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(new DefaultProfileValidationSupport(ctx)));

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

			int len;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((len = zis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			byte[] exampleBytes = bos.toByteArray();
			String exampleString = new String(exampleBytes, StandardCharsets.UTF_8);

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
			if (! result.isSuccessful()) {
				ourLog.info("FAILED to validate example {} - {}", nextEntry.getName(), result);
				continue;
			}

			if (ctx.getResourceType(parsed).equals("Bundle")) {
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
							org.hl7.fhir.r4.model.Bundle.BundleEntryComponent entry = bundle.addEntry();
							entry.getRequest().setMethod(org.hl7.fhir.r4.model.Bundle.HTTPVerb.POST);
							entry.setResource((org.hl7.fhir.r4.model.Resource) nextResource);
						}
					}
				}
			} else {
				if (ctx.getResourceType(parsed).equals("SearchParameter")) {
					continue;
				}
				org.hl7.fhir.r4.model.Bundle.BundleEntryComponent entry = bundle.addEntry();
				entry.getRequest().setMethod(org.hl7.fhir.r4.model.Bundle.HTTPVerb.POST);
				entry.setResource((org.hl7.fhir.r4.model.Resource) parsed);
			}
		}
		return bundle;
	}

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
		Options options = super.getOptions();
		Option opt;

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

		return options;
	}


	@Override
	protected Collection<Object> getFilterOutVersions() {
		Collection<Object> filterOutCollection = super.getFilterOutVersions();
		filterOutCollection.add(FhirVersionEnum.R5);
		return filterOutCollection;
	}

	private void processBundle(FhirContext ctx, IBaseBundle bundle) {
		switch (ctx.getVersion().getVersion()) {
			case DSTU2:
				processBundleDstu2(ctx, (Bundle) bundle);
				break;
			case DSTU3:
				processBundleDstu3(ctx, (org.hl7.fhir.dstu3.model.Bundle) bundle);
				break;
			case R4:
				processBundleR4(ctx, (org.hl7.fhir.r4.model.Bundle) bundle);
				break;
			default:
				throw new IllegalStateException(Msg.code(1608));
		}
	}

	private void processBundleDstu2(FhirContext ctx, Bundle bundle) {

		Set<String> fullIds = new HashSet<>();

		for (Iterator<Entry> iterator = bundle.getEntry().iterator(); iterator.hasNext(); ) {
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
		Set<String> qualIds = new TreeSet<>();

		for (Entry next : bundle.getEntry()) {
			if (next.getResource().getId().getIdPart() != null) {
				String nextId = next.getResource().getId().getValue();
				next.getRequest().setMethod(HTTPVerbEnum.PUT);
				next.getRequest().setUrl(nextId);
				qualIds.add(nextId);
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

		Set<String> fullIds = new HashSet<>();

		for (Iterator<BundleEntryComponent> iterator = bundle.getEntry().iterator(); iterator.hasNext(); ) {
			BundleEntryComponent next = iterator.next();

			// DataElement have giant IDs that seem invalid, need to investigate this..
			if ("Subscription".equals(next.getResource().getResourceType().name()) || "DataElement".equals(next.getResource().getResourceType().name())
				|| "OperationOutcome".equals(next.getResource().getResourceType().name()) || "OperationDefinition".equals(next.getResource().getResourceType().name())) {
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
		Set<String> qualIds = new TreeSet<>();
		for (BundleEntryComponent next : bundle.getEntry()) {
			if (next.getResource().getIdElement().getIdPart() != null) {
				String nextId = next.getResource().getIdElement().getValue();
				next.getRequest().setMethod(HTTPVerb.PUT);
				next.getRequest().setUrl(nextId);
				qualIds.add(nextId);
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

	private void processBundleR4(FhirContext ctx, org.hl7.fhir.r4.model.Bundle bundle) {

		Set<String> fullIds = new HashSet<>();

		for (Iterator<org.hl7.fhir.r4.model.Bundle.BundleEntryComponent> iterator = bundle.getEntry().iterator(); iterator.hasNext(); ) {
			org.hl7.fhir.r4.model.Bundle.BundleEntryComponent next = iterator.next();

			// DataElement have giant IDs that seem invalid, need to investigate this..
			if ("Subscription".equals(next.getResource().getResourceType().name()) || "DataElement".equals(next.getResource().getResourceType().name())
				|| "OperationOutcome".equals(next.getResource().getResourceType().name()) || "OperationDefinition".equals(next.getResource().getResourceType().name())) {
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
		Set<String> qualIds = new TreeSet<>();
		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent next : bundle.getEntry()) {
			if (next.getResource().getIdElement().getIdPart() != null) {
				String nextId = next.getResource().getIdElement().getValue();
				next.getRequest().setMethod(org.hl7.fhir.r4.model.Bundle.HTTPVerb.PUT);
				next.getRequest().setUrl(nextId);
				qualIds.add(nextId);
			}
		}

		int goodRefs = 0;
		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent next : bundle.getEntry()) {
			List<ResourceReferenceInfo> refs = ctx.newTerser().getAllResourceReferences(next.getResource());
			for (ResourceReferenceInfo nextRef : refs) {
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

		ourLog.info("{} good references", goodRefs);
		System.gc();

		ourLog.info("Final bundle: {} entries", bundle.getEntry().size());

	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);
		FhirContext ctx = getFhirContext();

		String targetServer = theCommandLine.getOptionValue("t");
		if (isBlank(targetServer)) {
			throw new ParseException(Msg.code(1609) + "No target server (-t) specified");
		} else if (! targetServer.startsWith("http") && ! targetServer.startsWith("file")) {
			throw new ParseException(Msg.code(1610) + "Invalid target server specified, must begin with 'http' or 'file'");
		}

		Integer limit = null;
		String limitString = theCommandLine.getOptionValue('l');
		if (isNotBlank(limitString)) {
			try {
				limit = Integer.parseInt(limitString);
			} catch (NumberFormatException e) {
				throw new ParseException(Msg.code(1611) + "Invalid number for limit (-l) option, must be a number: " + limitString);
			}
		}

		String specUrl;
		switch (ctx.getVersion().getVersion()) {
			case DSTU2:
				specUrl = "http://hl7.org/fhir/dstu2/examples-json.zip";
				break;
			case DSTU3:
				specUrl = "http://hl7.org/fhir/STU3/examples-json.zip";
				break;
			case R4:
				specUrl = "http://build.fhir.org/examples-json.zip";
				break;
			default:
				throw new ParseException(Msg.code(1612) + "Invalid spec version for this command: " + ctx.getVersion().getVersion());
		}

		String filepath = theCommandLine.getOptionValue('d');

		boolean cacheFile = theCommandLine.hasOption('c');

		Collection<File> inputFiles;
		try {
			inputFiles = loadFile(specUrl, filepath, cacheFile);
			for (File inputFile : inputFiles) {
				IBaseBundle bundle = getBundleFromFile(limit, inputFile, ctx);
				processBundle(ctx, bundle);
				sendBundleToTarget(targetServer, ctx, bundle, theCommandLine);
			}
		} catch (Exception e) {
			throw new CommandFailureException(Msg.code(1613) + e);
		}


	}

	private void sendBundleToTarget(String targetServer, FhirContext ctx, IBaseBundle bundle, CommandLine theCommandLine) throws Exception {
		List<IBaseResource> resources = BundleUtil.toListOfResources(ctx, bundle);

		for (Iterator<IBaseResource> iter = resources.iterator(); iter.hasNext(); ) {
			IBaseResource next = iter.next();

			String nextType = ctx.getResourceType(next);
			if (nextType.endsWith("Definition")) {
				iter.remove();
			} else if (nextType.contains("ValueSet")) {
				iter.remove();
			} else if (nextType.equals("CodeSystem")) {
				iter.remove();
			}
		}

		List<IBaseResource> subResourceList = new ArrayList<>();
		while (resources.size() > 0) {

			IBaseResource nextAddedResource = resources.remove(0);
			subResourceList.add(nextAddedResource);

			Set<String> checkedTargets = new HashSet<>();

			for (int i = 0; i < subResourceList.size(); i++) {
				IBaseResource nextCandidateSource = subResourceList.get(i);
				for (ResourceReferenceInfo nextRef : ctx.newTerser().getAllResourceReferences(nextCandidateSource)) {
					String nextRefResourceType = nextRef.getResourceReference().getReferenceElement().getResourceType();
					String nextRefIdPart = nextRef.getResourceReference().getReferenceElement().getIdPart();
					if (isBlank(nextRefResourceType) || isBlank(nextRefIdPart)) {
						nextRef.getResourceReference().setResource(null);
						nextRef.getResourceReference().setReference(null);
						continue;
					}
					if (nextRefIdPart.startsWith("EX")) {
						nextRefIdPart = nextRefIdPart.substring(2);
					}
					String nextTarget = nextRefResourceType + "/EX" + nextRefIdPart;
					nextRef.getResourceReference().setResource(null);
					nextRef.getResourceReference().setReference(nextTarget);
					if (! checkedTargets.add(nextTarget)) {
						continue;
					}

					for (int j = 0; j < resources.size(); j++) {
						String candidateTarget = resources.get(j).getIdElement().getValue();
						if (isNotBlank(nextTarget) && nextTarget.equals(candidateTarget)) {
							ourLog.info("Reflexively adding resource {} to bundle as it is a reference target", nextTarget);
							subResourceList.add(resources.remove(j));
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
			bundleFactory.addTotalResultsToBundle(subResourceList.size(), BundleTypeEnum.TRANSACTION);
			bundleFactory.addResourcesToBundle(new ArrayList<>(subResourceList), BundleTypeEnum.TRANSACTION, null, null, null);
			IBaseResource subBundle = bundleFactory.getResourceBundle();

			String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(subBundle);
			ourLog.info("Final bundle: {}", FileUtils.byteCountToDisplaySize(encoded.length()));

			if (targetServer.startsWith("file://")) {
				String path = targetServer.substring("file://".length());
				ourLog.info("Writing bundle to: {}", path);
				File file = new File(path);
				if (file.exists()) {
					throw new Exception(Msg.code(1614) + "File already exists: " + file.getAbsolutePath());
				}
				FileWriter w = new FileWriter(file, false);
				w.append(encoded);
				w.close();
			} else {
				ourLog.info("Uploading bundle to server: " + targetServer);

				IGenericClient fhirClient = newClient(theCommandLine);
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


}
