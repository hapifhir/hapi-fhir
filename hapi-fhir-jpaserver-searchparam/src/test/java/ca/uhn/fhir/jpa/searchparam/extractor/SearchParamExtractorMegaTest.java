package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.BaseRuntimeChildDatatypeDefinition;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildDirectResource;
import ca.uhn.fhir.context.RuntimeChildExtension;
import ca.uhn.fhir.context.RuntimeChildResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeNarrativeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"EnhancedSwitchMigration", "PatternVariableCanBeUsed"})
public class SearchParamExtractorMegaTest {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorMegaTest.class);

	/**
	 * This test is my magnum opus :P
	 * <p>
	 * It navigates almost every possible path in every FHIR resource in every version of FHIR,
	 * and creates a resource with that path populated, just to ensure that we can index it
	 * without generating any warnings.
	 */
	@ParameterizedTest
	@MethodSource("provideContexts")
	public void testAllCombinations(FhirContext theFhirContext) throws Exception {
		PartitionSettings partitionSettings = new PartitionSettings();

		ISearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(theFhirContext);
		BaseSearchParamExtractor extractor;
		switch (theFhirContext.getVersion().getVersion()) {
			case DSTU2:
				extractor = new SearchParamExtractorDstu2(new StorageSettings(), partitionSettings, theFhirContext, searchParamRegistry);
				break;
			case DSTU3:
				extractor = new SearchParamExtractorDstu3(new StorageSettings(), partitionSettings, theFhirContext, searchParamRegistry);
				break;
			case R4:
				extractor = new SearchParamExtractorR4(new StorageSettings(), partitionSettings, theFhirContext, searchParamRegistry);
				break;
			case R5:
				extractor = new SearchParamExtractorR5(new StorageSettings(), partitionSettings, theFhirContext, searchParamRegistry);
				break;
			case R4B:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				throw new UnsupportedOperationException();
		}

		process(theFhirContext, extractor);

	}

	private void process(FhirContext theCtx, BaseSearchParamExtractor theExtractor) throws Exception {
		AtomicInteger indexesCounter = new AtomicInteger();

		for (String nextResourceName : theCtx.getResourceTypes()) {
			RuntimeResourceDefinition resourceDefinition = theCtx.getResourceDefinition(nextResourceName);

			List<BaseRuntimeElementDefinition<?>> elementStack = new ArrayList<>();
			List<BaseRuntimeChildDefinition> childStack = new ArrayList<>();

			processElement(theCtx, theExtractor, resourceDefinition, elementStack, childStack, indexesCounter);
		}

		ourLog.info("Found {} indexes", indexesCounter.get());
	}

	private void processElement(FhirContext theCtx, BaseSearchParamExtractor theExtractor, BaseRuntimeElementDefinition<?> theElementDef, List<BaseRuntimeElementDefinition<?>> theElementStack, List<BaseRuntimeChildDefinition> theChildStack, AtomicInteger theIndexesCounter) throws Exception {
		if (theElementDef.getName().equals("ElementDefinition")) {
			return;
		}


		theElementStack.add(theElementDef);

		if (theElementDef instanceof BaseRuntimeElementCompositeDefinition) {
			BaseRuntimeElementCompositeDefinition<?> composite = (BaseRuntimeElementCompositeDefinition<?>) theElementDef;

			for (BaseRuntimeChildDefinition nextChild : composite.getChildren()) {
				if (theChildStack.contains(nextChild)) {
					continue;
				}

				theChildStack.add(nextChild);

				if (nextChild instanceof RuntimeChildResourceBlockDefinition) {
					BaseRuntimeElementDefinition<?> def = nextChild.getChildByName(nextChild.getElementName());
					processElement(theCtx, theExtractor, def, theElementStack, theChildStack, theIndexesCounter);
				} else if (nextChild instanceof BaseRuntimeChildDatatypeDefinition) {
					BaseRuntimeElementDefinition<?> def = nextChild.getChildByName(nextChild.getElementName());
					processElement(theCtx, theExtractor, def, theElementStack, theChildStack, theIndexesCounter);
				} else if (nextChild instanceof RuntimeChildExtension) {
					ourLog.trace("Ignoring RuntimeChildExtension");
				} else if (nextChild instanceof RuntimeChildContainedResources) {
					ourLog.trace("Ignoring RuntimeChildContainedResources");
				} else if (nextChild instanceof RuntimeChildResourceDefinition) {
					ourLog.trace("Ignoring RuntimeChildResourceDefinition");
				} else if (nextChild instanceof RuntimeChildChoiceDefinition) {
					RuntimeChildChoiceDefinition choice = (RuntimeChildChoiceDefinition) nextChild;
					for (String nextOption : choice.getValidChildNames()) {
						BaseRuntimeElementDefinition<?> def = nextChild.getChildByName(nextOption);
						processElement(theCtx, theExtractor, def, theElementStack, theChildStack, theIndexesCounter);
					}
				} else if (nextChild instanceof RuntimeChildDirectResource) {
					ourLog.trace("Ignoring RuntimeChildDirectResource");
				} else {
					throw new Exception("Unexpected child type: " + nextChild.getClass());
				}

				theChildStack.remove(theChildStack.size() - 1);
			}
		} else if (theElementDef instanceof RuntimePrimitiveDatatypeDefinition) {
			handlePathToPrimitive(theCtx, theExtractor, theElementStack, theChildStack, theIndexesCounter);
		} else if (theElementDef instanceof RuntimePrimitiveDatatypeNarrativeDefinition) {
			ourLog.trace("Ignoring RuntimePrimitiveDatatypeNarrativeDefinition");
		} else if (theElementDef instanceof RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition) {
			ourLog.trace("Ignoring RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition");
		} else {
			throw new Exception("Unexpected def type: " + theElementDef.getClass());
		}

		theElementStack.remove(theElementStack.size() - 1);
	}

	private void handlePathToPrimitive(FhirContext theCtx, BaseSearchParamExtractor theExtractor, List<BaseRuntimeElementDefinition<?>> theElementStack, List<BaseRuntimeChildDefinition> theChildStack, AtomicInteger theIndexesCounter) {
		IBase previousObject = null;
		IBaseResource resource = null;
		StringBuilder path = new StringBuilder(theElementStack.get(0).getName());
		Object previousChildArguments = null;
		for (int i = 0; i < theElementStack.size(); i++) {
			BaseRuntimeElementDefinition<?> nextElement = theElementStack.get(i);

			if (i > 0) {
				previousChildArguments = theChildStack.get(i - 1).getInstanceConstructorArguments();
			}

			IBase nextObject = nextElement.newInstance(previousChildArguments);
			if (i == 0) {
				resource = (IBaseResource) nextObject;
			} else {
				BaseRuntimeChildDefinition child = theChildStack.get(i - 1);
				child.getMutator().addValue(previousObject, nextObject);
				path.append(".").append(child.getChildNameByDatatype(nextObject.getClass()));
			}

			previousObject = nextObject;
		}

		IPrimitiveType<?> leaf = (IPrimitiveType<?>) previousObject;

		if (leaf instanceof IBaseEnumeration<?>) {
			return;
		}

		String typeName = theCtx.getElementDefinition(leaf.getClass()).getRootParentDefinition().getName();
		switch (typeName) {
			case "boolean":
				leaf.setValueAsString("true");
				break;
			case "date":
				leaf.setValueAsString("2019-10-10");
				break;
			case "dateTime":
			case "instant":
				leaf.setValueAsString("2019-10-10T11:11:11Z");
				break;
			case "integer64":
			case "integer":
			case "decimal":
				leaf.setValueAsString("1");
				break;
			default:
				leaf.setValueAsString("a");
				break;
		}

		ourLog.debug("Found path: {}", path);

		{
			ISearchParamExtractor.SearchParamSet<?> set = theExtractor.extractSearchParamDates(resource);
			List<String> warnings = set.getWarnings();
			assertThat(warnings.size()).as(() -> String.join("\n", warnings)).isEqualTo(0);
			theIndexesCounter.addAndGet(set.size());
		}
		{
			ISearchParamExtractor.SearchParamSet<?> set = theExtractor.extractSearchParamNumber(resource);
			List<String> warnings = set.getWarnings();
			// This is an R5 parameter that needs special handling
			warnings.remove("Search param [Patient]#age is unable to index value of type date as a NUMBER at path: Patient.birthDate");
			assertThat(warnings.size()).as(() -> String.join("\n", warnings)).isEqualTo(0);
			theIndexesCounter.addAndGet(set.size());
		}
		{
			ISearchParamExtractor.SearchParamSet<?> set = theExtractor.extractSearchParamStrings(resource);
			List<String> warnings = set.getWarnings();
			assertThat(warnings.size()).as(() -> String.join("\n", warnings)).isEqualTo(0);
			theIndexesCounter.addAndGet(set.size());
		}
		{
			ISearchParamExtractor.SearchParamSet<?> set = theExtractor.extractSearchParamQuantity(resource);
			List<String> warnings = set.getWarnings();
			assertThat(warnings.size()).as(() -> String.join("\n", warnings)).isEqualTo(0);
			theIndexesCounter.addAndGet(set.size());
		}
		{
			ISearchParamExtractor.SearchParamSet<?> set = theExtractor.extractSearchParamTokens(resource);
			List<String> warnings = set.getWarnings();
			// Two invalid params in draft R5
			warnings.remove("Search param [MedicationUsage]#adherence is unable to index value of type org.hl7.fhir.r5.model.MedicationUsage.MedicationUsageAdherenceComponent as a TOKEN at path: MedicationUsage.adherence");
			warnings.remove("Search param [ResearchStudy]#focus is unable to index value of type org.hl7.fhir.r5.model.ResearchStudy.ResearchStudyFocusComponent as a TOKEN at path: ResearchStudy.focus");
			assertThat(warnings.size()).as(() -> String.join("\n", warnings)).isEqualTo(0);
			theIndexesCounter.addAndGet(set.size());
		}
		{
			ISearchParamExtractor.SearchParamSet<?> set = theExtractor.extractSearchParamUri(resource);
			List<String> warnings = set.getWarnings();
			assertThat(warnings.size()).as(() -> String.join("\n", warnings)).isEqualTo(0);
			theIndexesCounter.addAndGet(set.size());
		}
	}

	public static List<FhirContext> provideContexts() {
		return Lists.newArrayList(
			FhirContext.forDstu2Cached(),
			FhirContext.forDstu3Cached(),
			FhirContext.forR4Cached(),
			FhirContext.forR5Cached()
		);
	}

}
