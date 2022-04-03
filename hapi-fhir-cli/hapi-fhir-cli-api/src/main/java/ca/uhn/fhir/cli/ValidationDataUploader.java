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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValidationDataUploader extends BaseCommand {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidationDataUploader.class);

	private ArrayList<IIdType> myExcludes = new ArrayList<>();

	private void filterBundle(ca.uhn.fhir.model.dstu2.resource.Bundle theBundle) {
		for (Iterator<Entry> iter = theBundle.getEntry().iterator(); iter.hasNext(); ) {
			IBaseResource next = iter.next().getResource();
			for (IIdType nextExclude : myExcludes) {
				if (nextExclude.hasResourceType() && nextExclude.toUnqualifiedVersionless().getValue().equals(next.getIdElement().toUnqualifiedVersionless().getValue())) {
					iter.remove();
					continue;
				} else if (nextExclude.getIdPart().equals(next.getIdElement().getIdPart())) {
					iter.remove();
					continue;
				}
			}
		}
	}

	private void filterBundle(org.hl7.fhir.dstu3.model.Bundle theBundle) {
		for (Iterator<BundleEntryComponent> iter = theBundle.getEntry().iterator(); iter.hasNext(); ) {
			IBaseResource next = iter.next().getResource();
			for (IIdType nextExclude : myExcludes) {
				if (nextExclude.hasResourceType() && nextExclude.toUnqualifiedVersionless().getValue().equals(next.getIdElement().toUnqualifiedVersionless().getValue())) {
					iter.remove();
					continue;
				} else if (nextExclude.getIdPart().equals(next.getIdElement().getIdPart())) {
					iter.remove();
					continue;
				}
			}
		}
	}

	private void filterBundle(org.hl7.fhir.r4.model.Bundle theBundle) {
		for (Iterator<org.hl7.fhir.r4.model.Bundle.BundleEntryComponent> iter = theBundle.getEntry().iterator(); iter.hasNext(); ) {
			IBaseResource next = iter.next().getResource();
			for (IIdType nextExclude : myExcludes) {
				if (nextExclude.hasResourceType() && nextExclude.toUnqualifiedVersionless().getValue().equals(next.getIdElement().toUnqualifiedVersionless().getValue())) {
					iter.remove();
					continue;
				} else if (nextExclude.getIdPart().equals(next.getIdElement().getIdPart())) {
					iter.remove();
					continue;
				}
			}
		}
	}

	@Override
	public String getCommandDescription() {
		return "Uploads the conformance resources (StructureDefinition and ValueSet) from the official FHIR definitions.";
	}

	@Override
	public String getCommandName() {
		return "upload-definitions";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		Option opt;

		addFhirVersionOption(options);

		opt = new Option(BASE_URL_PARAM, "target", true, "Base URL for the target server (e.g. \"http://example.com/fhir\")");
		opt.setRequired(true);
		options.addOption(opt);

		addBasicAuthOption(options);

		opt = new Option("e", "exclude", true, "Exclude uploading the given resources, e.g. \"-e dicom-dcim,foo\"");
		opt.setRequired(false);
		options.addOption(opt);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);

		String targetServer = theCommandLine.getOptionValue("t");
		if (isBlank(targetServer)) {
			throw new ParseException(Msg.code(1589) + "No target server (-t) specified");
		} else if (targetServer.startsWith("http") == false) {
			throw new ParseException(Msg.code(1590) + "Invalid target server specified, must begin with 'http'");
		}

		FhirContext ctx = getFhirContext();
		String exclude = theCommandLine.getOptionValue("e");

		if (isNotBlank(exclude)) {
			for (String next : exclude.split(",")) {
				if (isNotBlank(next)) {
					IIdType id = ctx.getVersion().newIdType();
					id.setValue(next);
					myExcludes.add(id);
				}
			}
		}

		if (ctx.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
			uploadDefinitionsDstu2(theCommandLine, ctx);
		} else if (ctx.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			uploadDefinitionsDstu3(theCommandLine, ctx);
		} else if (ctx.getVersion().getVersion() == FhirVersionEnum.R4) {
			uploadDefinitionsR4(theCommandLine, ctx);
		}

	}

	private void uploadDefinitionsDstu2(CommandLine theCommandLine, FhirContext ctx) throws CommandFailureException, ParseException {
		IGenericClient client = newClient(theCommandLine);

		ourLog.info("Uploading definitions to server");

		long start = System.currentTimeMillis();

		String vsContents;
		try {
			ctx.getVersion().getPathToSchemaDefinitions();
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/" + "valuesets.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1591) + e.toString());
		}
		Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);

		int total = bundle.getEntry().size();
		int count = 1;
		for (Entry i : bundle.getEntry()) {
			ValueSet next = (ValueSet) i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading ValueSet {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			client.update().resource(next).execute();

			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/" + "v3-codesystems.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1592) + e.toString());
		}

		bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);
		total = bundle.getEntry().size();
		count = 1;
		for (Entry i : bundle.getEntry()) {
			ValueSet next = (ValueSet) i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v3-codesystems ValueSet {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			client.update().resource(next).execute();

			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/" + "v2-tables.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1593) + e.toString());
		}
		bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);
		total = bundle.getEntry().size();
		count = 1;
		for (Entry i : bundle.getEntry()) {
			ValueSet next = (ValueSet) i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v2-tables ValueSet {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			client.update().resource(next).execute();
			count++;
		}

		ourLog.info("Finished uploading ValueSets");

		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] mappingLocations;
		try {
			mappingLocations = patternResolver.getResources("classpath*:org/hl7/fhir/instance/model/profile/" + "*.profile.xml");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1594) + e.toString());
		}
		total = mappingLocations.length;
		count = 1;
		for (Resource i : mappingLocations) {
			StructureDefinition next;
			try {
				next = ctx.newXmlParser().parseResource(StructureDefinition.class, IOUtils.toString(i.getInputStream(), "UTF-8"));
			} catch (Exception e) {
				throw new CommandFailureException(Msg.code(1595) + e.toString());
			}
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading StructureDefinition {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			try {
				client.update().resource(next).execute();
			} catch (Exception e) {
				ourLog.warn("Failed to upload {} - {}", next.getIdElement().getValue(), e.getMessage());
			}
			count++;
		}

		ourLog.info("Finished uploading ValueSets");

		long delay = System.currentTimeMillis() - start;

		ourLog.info("Finished uploading definitions to server (took {} ms)", delay);
	}

	private void uploadDefinitionsDstu3(CommandLine theCommandLine, FhirContext theCtx) throws CommandFailureException, ParseException {
		IGenericClient client = newClient(theCommandLine);
		ourLog.info("Uploading definitions to server");

		long start = System.currentTimeMillis();
		int total = 0;
		int count = 0;
		org.hl7.fhir.dstu3.model.Bundle bundle;
		String vsContents;

		try {
			theCtx.getVersion().getPathToSchemaDefinitions();
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/dstu3/model/valueset/" + "valuesets.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1596) + e.toString());
		}
		bundle = theCtx.newXmlParser().parseResource(org.hl7.fhir.dstu3.model.Bundle.class, vsContents);
		filterBundle(bundle);

		total = bundle.getEntry().size();
		count = 1;
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.dstu3.model.Resource next = i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			int bytes = theCtx.newXmlParser().encodeResourceToString(next).length();

			ourLog.info("Uploading ValueSet {}/{} : {} ({} bytes}", new Object[] {count, total, next.getIdElement().getValue(), bytes});
			try {
				IIdType id = client.update().resource(next).execute().getId();
				ourLog.info("  - Got ID: {}", id.getValue());
			} catch (UnprocessableEntityException e) {
				ourLog.warn("UnprocessableEntityException: " + e.toString());
			} catch (BaseServerResponseException e) {
				ourLog.warn("Server responded HTTP " + e.getStatusCode() + ": " + e.toString());
			}
			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/dstu3/model/valueset/" + "v3-codesystems.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1597) + e.toString());
		}

		bundle = theCtx.newXmlParser().parseResource(org.hl7.fhir.dstu3.model.Bundle.class, vsContents);
		filterBundle(bundle);

		total = bundle.getEntry().size();
		count = 1;
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.dstu3.model.Resource next = i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v3-codesystems ValueSet {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			try {
				client.update().resource(next).execute();
			} catch (Exception e) {
				ourLog.error("Failed to upload: {}", e.toString());
			}
			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/dstu3/model/valueset/" + "v2-tables.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1598) + e.toString());
		}
		bundle = theCtx.newXmlParser().parseResource(org.hl7.fhir.dstu3.model.Bundle.class, vsContents);
		filterBundle(bundle);
		total = bundle.getEntry().size();
		count = 1;
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.dstu3.model.Resource next = i.getResource();
			if (next.getIdElement().isIdPartValidLong()) {
				next.setIdElement(new IdType("v2-" + next.getIdElement().getIdPart()));
			}
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v2-tables ValueSet {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			client.update().resource(next).execute();
			count++;
		}

		ourLog.info("Finished uploading ValueSets");


		uploadDstu3Profiles(theCtx, client, "profile/profiles-resources");
		uploadDstu3Profiles(theCtx, client, "profile/profiles-types");
		uploadDstu3Profiles(theCtx, client, "profile/profiles-others");
		uploadDstu3Profiles(theCtx, client, "extension/extension-definitions");

		ourLog.info("Finished uploading ValueSets");

		long delay = System.currentTimeMillis() - start;

		ourLog.info("Finished uploading definitions to server (took {} ms)", delay);
	}

	private void uploadDefinitionsR4(CommandLine theCommandLine, FhirContext theCtx) throws CommandFailureException, ParseException {
		IGenericClient client = newClient(theCommandLine);
		ourLog.info("Uploading definitions to server");

		long start = System.currentTimeMillis();
		int total = 0;
		int count = 0;
		org.hl7.fhir.r4.model.Bundle bundle;
		String vsContents;

		try {
			theCtx.getVersion().getPathToSchemaDefinitions();
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/r4/model/valueset/" + "valuesets.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1599) + e.toString());
		}
		bundle = theCtx.newXmlParser().parseResource(org.hl7.fhir.r4.model.Bundle.class, vsContents);
		filterBundle(bundle);

		total = bundle.getEntry().size();
		count = 1;
		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.r4.model.Resource next = i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			int bytes = theCtx.newXmlParser().encodeResourceToString(next).length();

			ourLog.info("Uploading ValueSet {}/{} : {} ({} bytes}", new Object[] {count, total, next.getIdElement().getValue(), bytes});
			try {
				IIdType id = client.update().resource(next).execute().getId();
				ourLog.info("  - Got ID: {}", id.getValue());
			} catch (UnprocessableEntityException e) {
				ourLog.warn("UnprocessableEntityException: " + e.toString());
			}
			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/r4/model/valueset/" + "v3-codesystems.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1600) + e.toString());
		}

		bundle = theCtx.newXmlParser().parseResource(org.hl7.fhir.r4.model.Bundle.class, vsContents);
		filterBundle(bundle);
		total = bundle.getEntry().size();
		count = 1;
		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.r4.model.Resource next = i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v3-codesystems ValueSet {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			client.update().resource(next).execute();

			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/r4/model/valueset/" + "v2-tables.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1601) + e.toString());
		}
		bundle = theCtx.newXmlParser().parseResource(org.hl7.fhir.r4.model.Bundle.class, vsContents);
		filterBundle(bundle);
		total = bundle.getEntry().size();
		count = 1;
		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.r4.model.Resource next = i.getResource();
			if (next.getIdElement().isIdPartValidLong()) {
				next.setIdElement(new org.hl7.fhir.r4.model.IdType("v2-" + next.getIdElement().getIdPart()));
			}
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v2-tables ValueSet {}/{} : {}", new Object[] {count, total, next.getIdElement().getValue()});
			client.update().resource(next).execute();
			count++;
		}

		ourLog.info("Finished uploading ValueSets");


		uploadR4Profiles(theCtx, client, "profile/profiles-resources");
		uploadR4Profiles(theCtx, client, "profile/profiles-types");
		uploadR4Profiles(theCtx, client, "profile/profiles-others");
		uploadR4Profiles(theCtx, client, "extension/extension-definitions");

		ourLog.info("Finished uploading ValueSets");

		long delay = System.currentTimeMillis() - start;

		ourLog.info("Finished uploading definitions to server (took {} ms)", delay);
	}

	private void uploadDstu3Profiles(FhirContext ctx, IGenericClient client, String theName) throws CommandFailureException {
		int total;
		int count;
		org.hl7.fhir.dstu3.model.Bundle bundle;
		ourLog.info("Uploading " + theName);
		String vsContents;
		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/dstu3/model/" + theName + ".xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1602) + e.toString());
		}

		bundle = ctx.newXmlParser().parseResource(org.hl7.fhir.dstu3.model.Bundle.class, vsContents);
		filterBundle(bundle);
		total = bundle.getEntry().size();
		count = 1;

		Collections.sort(bundle.getEntry(), new Comparator<BundleEntryComponent>() {
			@Override
			public int compare(BundleEntryComponent theO1, BundleEntryComponent theO2) {
				if (theO1.getResource() == null && theO2.getResource() == null) {
					return 0;
				}
				if (theO1.getResource() == null) {
					return 1;
				}
				if (theO2.getResource() == null) {
					return -1;
				}
				// StructureDefinition, then OperationDefinition, then CompartmentDefinition
				return theO2.getResource().getClass().getName().compareTo(theO1.getResource().getClass().getName());
			}
		});

		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.dstu3.model.Resource next = i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());
			if (next instanceof CapabilityStatement) {
				continue;
			}

			ourLog.info("Uploading {} StructureDefinition {}/{} : {}", new Object[] {theName, count, total, next.getIdElement().getValue()});
			client.update().resource(next).execute();

			count++;
		}
	}

	private void uploadR4Profiles(FhirContext theContext, IGenericClient theClient, String theName) throws CommandFailureException {
		int total;
		int count;
		org.hl7.fhir.r4.model.Bundle bundle;
		ourLog.info("Uploading " + theName);
		String vsContents;
		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/r4/model/" + theName + ".xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(1603) + e.toString());
		}

		bundle = theContext.newXmlParser().parseResource(org.hl7.fhir.r4.model.Bundle.class, vsContents);
		filterBundle(bundle);
		total = bundle.getEntry().size();
		count = 1;

		Collections.sort(bundle.getEntry(), new Comparator<org.hl7.fhir.r4.model.Bundle.BundleEntryComponent>() {
			@Override
			public int compare(org.hl7.fhir.r4.model.Bundle.BundleEntryComponent theO1, org.hl7.fhir.r4.model.Bundle.BundleEntryComponent theO2) {
				if (theO1.getResource() == null && theO2.getResource() == null) {
					return 0;
				}
				if (theO1.getResource() == null) {
					return 1;
				}
				if (theO2.getResource() == null) {
					return -1;
				}
				// StructureDefinition, then OperationDefinition, then CompartmentDefinition
				return theO2.getResource().getClass().getName().compareTo(theO1.getResource().getClass().getName());
			}
		});

		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.r4.model.Resource next = i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());
			if (next instanceof org.hl7.fhir.r4.model.CapabilityStatement) {
				continue;
			}

			ourLog.info("Uploading {} StructureDefinition {}/{} : {}", new Object[] {theName, count, total, next.getIdElement().getValue()});
			try {
				theClient.update().resource(next).execute();
			} catch (BaseServerResponseException e) {
				ourLog.warn("Server responded HTTP " + e.getStatusCode() + ": " + e.toString());
			}

			count++;
		}
	}

}
