/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term.custom;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.util.CsvUtil;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_40;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class converts one or more CodeSystems containing concepts as well as their
 * hierarchies and properties into the equivalent CSV files, suitable for passing
 * to the {@link ITermLoaderSvc}.
 */
public class CodeSystemToCustomCsvConverter {

	private final FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public CodeSystemToCustomCsvConverter(@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myFhirContext = theFhirContext;
	}

	/**
	 * Converts a collection of CodeSystem resources into CSV files
	 *
	 * @param theInputCodeSystems The CodeSystem resources to convert, or {@literal null}
	 */
	@Nonnull
	public List<ITermLoaderSvc.FileDescriptor> convertCodeSystemsToFileDescriptors(
			@Nullable Collection<IBaseResource> theInputCodeSystems) {

		List<ITermLoaderSvc.FileDescriptor> outputFiles = new ArrayList<>();
		Map<String, String> codes = new LinkedHashMap<>();
		Map<String, List<CodeSystem.ConceptPropertyComponent>> codeToProperties = new LinkedHashMap<>();

		Multimap<String, String> codeToParentCodes = ArrayListMultimap.create();

		if (theInputCodeSystems != null) {
			for (IBaseResource nextCodeSystemUncast : theInputCodeSystems) {
				CodeSystem nextCodeSystem = canonicalizeCodeSystem(nextCodeSystemUncast);
				convertCodeSystemCodesToCsv(
						nextCodeSystem.getConcept(), codes, codeToProperties, null, codeToParentCodes);
			}
		}

		// Create concept file
		if (!codes.isEmpty()) {
			String[] headerFields = {ConceptHandler.CODE, ConceptHandler.DISPLAY};
			CsvUtil.ICsvProducer csvProducer = csvPrinter -> {
				for (Map.Entry<String, String> nextEntry : codes.entrySet()) {
					csvPrinter.print(nextEntry.getKey());
					csvPrinter.print(nextEntry.getValue());
					csvPrinter.println();
				}
			};
			byte[] bytes = CsvUtil.writeCsvToByteArray(headerFields, csvProducer);
			String fileName = TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE;
			ITermLoaderSvc.ByteArrayFileDescriptor fileDescriptor =
					new ITermLoaderSvc.ByteArrayFileDescriptor(fileName, bytes);
			outputFiles.add(fileDescriptor);
		}

		// Create hierarchy file
		if (!codeToParentCodes.isEmpty()) {
			String[] headerFields = {HierarchyHandler.CHILD, HierarchyHandler.PARENT};
			CsvUtil.ICsvProducer csvProducer = csvPrinter -> {
				for (Map.Entry<String, String> nextEntry : codeToParentCodes.entries()) {
					csvPrinter.print(nextEntry.getKey());
					csvPrinter.print(nextEntry.getValue());
					csvPrinter.println();
				}
			};
			byte[] bytes = CsvUtil.writeCsvToByteArray(headerFields, csvProducer);
			String fileName = TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE;
			ITermLoaderSvc.ByteArrayFileDescriptor fileDescriptor =
					new ITermLoaderSvc.ByteArrayFileDescriptor(fileName, bytes);
			outputFiles.add(fileDescriptor);
		}
		// Create codeToProperties file
		if (!codeToProperties.isEmpty()) {
			String[] headerFields = {
				PropertyHandler.CODE, PropertyHandler.KEY, PropertyHandler.VALUE, PropertyHandler.TYPE
			};
			CsvUtil.ICsvProducer csvProducer = csvPrinter -> {
				IParser jsonParser = myFhirContext.newJsonParser().setPrettyPrint(false);
				for (Map.Entry<String, List<CodeSystem.ConceptPropertyComponent>> nextEntry :
						codeToProperties.entrySet()) {
					for (CodeSystem.ConceptPropertyComponent propertyComponent : nextEntry.getValue()) {
						if (propertyComponent.getValue() == null) {
							continue;
						}

						csvPrinter.print(nextEntry.getKey());
						csvPrinter.print(propertyComponent.getCode());

						String valueType = myFhirContext
								.getElementDefinition(
										propertyComponent.getValue().getClass())
								.getName();
						TermConceptPropertyTypeEnum propertyType = TermConceptPropertyTypeEnum.forDatatype(valueType);
						if (propertyType == null) {
							throw new InvalidRequestException(Msg.code(2885)
									+ "Don't know how to handle value of type: "
									+ myFhirContext
											.getElementDefinition(
													propertyComponent.getValue().getClass())
											.getName());
						}

						if (propertyType.isPrimitive()) {
							csvPrinter.print(propertyComponent.getValue().primitiveValue());
						} else {
							csvPrinter.print(jsonParser.encodeToString(propertyComponent.getValue()));
						}

						csvPrinter.print(propertyType.name());
						csvPrinter.println();
					}
				}
			};

			byte[] bytes = CsvUtil.writeCsvToByteArray(headerFields, csvProducer);
			String fileName = TermLoaderSvcImpl.CUSTOM_PROPERTIES_FILE;
			ITermLoaderSvc.ByteArrayFileDescriptor fileDescriptor =
					new ITermLoaderSvc.ByteArrayFileDescriptor(fileName, bytes);
			outputFiles.add(fileDescriptor);
		}

		return outputFiles;
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	@Nonnull
	CodeSystem canonicalizeCodeSystem(@Nonnull IBaseResource theCodeSystem) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theCodeSystem);
		ValidateUtil.isTrueOrThrowInvalidRequest(
				resourceDef.getName().equals("CodeSystem"), "Resource '%s' is not a CodeSystem", resourceDef.getName());

		return switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3 -> (CodeSystem) VersionConvertorFactory_30_40.convertResource(
					(org.hl7.fhir.dstu3.model.CodeSystem) theCodeSystem, new BaseAdvisor_30_40(false));
			case R5 -> (CodeSystem) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r5.model.CodeSystem) theCodeSystem, new BaseAdvisor_40_50(false));
			case R4 -> (CodeSystem) theCodeSystem;
			default -> throw new IllegalStateException(Msg.code(2888) + "Can't canonicalize FHIR version: "
					+ myFhirContext.getVersion().getVersion());
		};
	}

	private void convertCodeSystemCodesToCsv(
			List<CodeSystem.ConceptDefinitionComponent> theConcept,
			Map<String, String> theCodes,
			Map<String, List<CodeSystem.ConceptPropertyComponent>> theProperties,
			String theParentCode,
			Multimap<String, String> theCodeToParentCodes) {
		for (CodeSystem.ConceptDefinitionComponent nextConcept : theConcept) {
			if (isNotBlank(nextConcept.getCode())) {
				theCodes.put(nextConcept.getCode(), nextConcept.getDisplay());
				if (isNotBlank(theParentCode)) {
					theCodeToParentCodes.put(nextConcept.getCode(), theParentCode);
				}
				if (nextConcept.getProperty() != null) {
					theProperties.put(nextConcept.getCode(), nextConcept.getProperty());
				}
				convertCodeSystemCodesToCsv(
						nextConcept.getConcept(), theCodes, theProperties, nextConcept.getCode(), theCodeToParentCodes);
			}
		}
	}
}
