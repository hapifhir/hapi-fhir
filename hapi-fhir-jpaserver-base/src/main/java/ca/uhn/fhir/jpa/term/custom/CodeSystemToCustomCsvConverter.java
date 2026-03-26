package ca.uhn.fhir.jpa.term.custom;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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
	public CodeSystemToCustomCsvConverter(FhirContext theFhirContext) {
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
			StringBuilder b = new StringBuilder();
			b.append(ConceptHandler.CODE);
			b.append(",");
			b.append(ConceptHandler.DISPLAY);
			b.append("\n");
			for (Map.Entry<String, String> nextEntry : codes.entrySet()) {
				b.append(csvEscape(nextEntry.getKey()));
				b.append(",");
				b.append(csvEscape(nextEntry.getValue()));
				b.append("\n");
			}
			byte[] bytes = b.toString().getBytes(Charsets.UTF_8);
			String fileName = TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE;
			ITermLoaderSvc.ByteArrayFileDescriptor fileDescriptor =
					new ITermLoaderSvc.ByteArrayFileDescriptor(fileName, bytes);
			outputFiles.add(fileDescriptor);
		}

		// Create hierarchy file
		if (!codeToParentCodes.isEmpty()) {
			StringBuilder b = new StringBuilder();
			b.append(HierarchyHandler.CHILD);
			b.append(",");
			b.append(HierarchyHandler.PARENT);
			b.append("\n");
			for (Map.Entry<String, String> nextEntry : codeToParentCodes.entries()) {
				b.append(csvEscape(nextEntry.getKey()));
				b.append(",");
				b.append(csvEscape(nextEntry.getValue()));
				b.append("\n");
			}
			byte[] bytes = b.toString().getBytes(Charsets.UTF_8);
			String fileName = TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE;
			ITermLoaderSvc.ByteArrayFileDescriptor fileDescriptor =
					new ITermLoaderSvc.ByteArrayFileDescriptor(fileName, bytes);
			outputFiles.add(fileDescriptor);
		}
		// Create codeToProperties file
		if (!codeToProperties.isEmpty()) {
			StringBuilder b = new StringBuilder();
			b.append(PropertyHandler.CODE);
			b.append(",");
			b.append(PropertyHandler.KEY);
			b.append(",");
			b.append(PropertyHandler.VALUE);
			b.append(",");
			b.append(PropertyHandler.TYPE);
			b.append("\n");

			for (Map.Entry<String, List<CodeSystem.ConceptPropertyComponent>> nextEntry : codeToProperties.entrySet()) {
				for (CodeSystem.ConceptPropertyComponent propertyComponent : nextEntry.getValue()) {
					if (propertyComponent.getValue() == null) {
						continue;
					}

					b.append(csvEscape(nextEntry.getKey()));
					b.append(",");
					b.append(csvEscape(propertyComponent.getCode()));
					b.append(",");

					String valueType = myFhirContext
							.getElementDefinition(propertyComponent.getValue().getClass())
							.getName();
					TermConceptPropertyTypeEnum propertyType = TermConceptPropertyTypeEnum.forDatatype(valueType);
					if (propertyType == null) {
						throw new InvalidRequestException(Msg.code(2885) + "Don't know how to handle value of type: "
								+ myFhirContext
										.getElementDefinition(
												propertyComponent.getValue().getClass())
										.getName());
					}

					if (propertyType.isPrimitive()) {
						b.append(csvEscape(propertyComponent.getValue().primitiveValue()));
					} else {
						b.append(csvEscape(myFhirContext.newJsonParser().encodeToString(propertyComponent.getValue())));
					}

					b.append(",");
					b.append(csvEscape(propertyType.name()));
					b.append("\n");
				}
			}
			byte[] bytes = b.toString().getBytes(Charsets.UTF_8);
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

		CodeSystem nextCodeSystem =
				switch (myFhirContext.getVersion().getVersion()) {
					case DSTU3 -> (CodeSystem) VersionConvertorFactory_30_40.convertResource(
							(org.hl7.fhir.dstu3.model.CodeSystem) theCodeSystem, new BaseAdvisor_30_40(false));
					case R5 -> (CodeSystem) VersionConvertorFactory_40_50.convertResource(
							(org.hl7.fhir.r5.model.CodeSystem) theCodeSystem, new BaseAdvisor_40_50(false));
					default -> (CodeSystem) theCodeSystem;
				};
		return nextCodeSystem;
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

	private static String csvEscape(String theValue) {
		if (theValue == null) {
			return "";
		}
		return '"' + theValue.replace("\"", "\"\"").replace("\n", "\\n").replace("\r", "") + '"';
	}
}
