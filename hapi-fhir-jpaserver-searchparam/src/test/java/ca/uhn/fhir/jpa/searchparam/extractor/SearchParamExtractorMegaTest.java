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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.jpa.cache.ResourceChangeResult;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO JA Please fix this test. Expanding FhirContext.getResourceTypes() to cover all resource types broke this test.
@Disabled
public class SearchParamExtractorMegaTest {
	// TODO: JA remove unused?

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorMegaTest.class);

	/**
	 * This test is my magnum opus :P
	 * <p>
	 * It navigates almost every possible path in every FHIR resource in every version of FHIR,
	 * and creates a resource with that path populated, just to ensure that we can index it
	 * without generating any warnings.
	 */
	@Test
	public void testAllCombinations() throws Exception {
		PartitionSettings partitionSettings = new PartitionSettings();

		FhirContext ctx = FhirContext.forDstu2();
		ISearchParamRegistry searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorDstu2(new ModelConfig(), partitionSettings, ctx, searchParamRegistry));

		ctx = FhirContext.forDstu3();
		searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorDstu3(new ModelConfig(), partitionSettings, ctx, searchParamRegistry));

		ctx = FhirContext.forR4();
		searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorR4(new ModelConfig(), partitionSettings, ctx, searchParamRegistry));

		ctx = FhirContext.forR5();
		searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorR5(new ModelConfig(), partitionSettings, ctx, searchParamRegistry));
	}

	private void process(FhirContext theCtx, BaseSearchParamExtractor theExtractor) throws Exception {
		AtomicInteger indexesCounter = new AtomicInteger();

		for (String nextResourceName : theCtx.getResourceTypes()) {
			RuntimeResourceDefinition resourceDefinition = theCtx.getResourceDefinition(nextResourceName);

			List<BaseRuntimeElementDefinition> elementStack = new ArrayList<>();
			List<BaseRuntimeChildDefinition> childStack = new ArrayList<>();

			processElement(theCtx, theExtractor, resourceDefinition, elementStack, childStack, indexesCounter);
		}

		ourLog.info("Found {} indexes", indexesCounter.get());
	}

	private void processElement(FhirContext theCtx, BaseSearchParamExtractor theExtractor, BaseRuntimeElementDefinition theElementDef, List<BaseRuntimeElementDefinition> theElementStack, List<BaseRuntimeChildDefinition> theChildStack, AtomicInteger theIndexesCounter) throws Exception {
		if (theElementDef.getName().equals("ElementDefinition")) {
			return;
		}


		theElementStack.add(theElementDef);

		if (theElementDef instanceof BaseRuntimeElementCompositeDefinition) {
			BaseRuntimeElementCompositeDefinition<?> composite = (BaseRuntimeElementCompositeDefinition) theElementDef;

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
					// ignore extensions
				} else if (nextChild instanceof RuntimeChildContainedResources) {
					// ignore extensions
				} else if (nextChild instanceof RuntimeChildResourceDefinition) {
					// ignore extensions
				} else if (nextChild instanceof RuntimeChildChoiceDefinition) {
					RuntimeChildChoiceDefinition choice = (RuntimeChildChoiceDefinition) nextChild;
					for (String nextOption : choice.getValidChildNames()) {
						BaseRuntimeElementDefinition<?> def = nextChild.getChildByName(nextOption);
						processElement(theCtx, theExtractor, def, theElementStack, theChildStack, theIndexesCounter);
					}
				} else if (nextChild instanceof RuntimeChildDirectResource) {
					// ignore
				} else {
					throw new Exception("Unexpected child type: " + nextChild.getClass());
				}

				theChildStack.remove(theChildStack.size() - 1);
			}
		} else if (theElementDef instanceof RuntimePrimitiveDatatypeDefinition) {
			handlePathToPrimitive(theCtx, theExtractor, theElementStack, theChildStack, theIndexesCounter);
		} else if (theElementDef instanceof RuntimePrimitiveDatatypeNarrativeDefinition) {
			// ignore
		} else if (theElementDef instanceof RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition) {
			// ignore
		} else {
			throw new Exception("Unexpected def type: " + theElementDef.getClass());
		}

		theElementStack.remove(theElementStack.size() - 1);
	}

	private void handlePathToPrimitive(FhirContext theCtx, BaseSearchParamExtractor theExtractor, List<BaseRuntimeElementDefinition> theElementStack, List<BaseRuntimeChildDefinition> theChildStack, AtomicInteger theIndexesCounter) {
		IBase previousObject = null;
		IBaseResource resource = null;
		StringBuilder path = new StringBuilder(theElementStack.get(0).getName());
		Object previousChildArguments = null;
		for (int i = 0; i < theElementStack.size(); i++) {
			BaseRuntimeElementDefinition nextElement = theElementStack.get(i);

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

		ourLog.info("Found path: {}", path);


		ISearchParamExtractor.SearchParamSet<?> set;

		set = theExtractor.extractSearchParamDates(resource);
		assertEquals(0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

		set = theExtractor.extractSearchParamNumber(resource);
		assertEquals(0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

		set = theExtractor.extractSearchParamStrings(resource);
		assertEquals(0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

		set = theExtractor.extractSearchParamQuantity(resource);
		assertEquals(0, set.getWarnings().size(), String.join("\n", set.getWarnings()));
		theIndexesCounter.addAndGet(set.size());

		set = theExtractor.extractSearchParamTokens(resource);
		assertEquals(0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

		set = theExtractor.extractSearchParamUri(resource);
		assertEquals(0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

	}

	private static class MySearchParamRegistry implements ISearchParamRegistry, ISearchParamRegistryController {

		private final FhirContext myCtx;
		private List<RuntimeSearchParam> myAddedSearchParams = new ArrayList<>();

		private MySearchParamRegistry(FhirContext theCtx) {
			myCtx = theCtx;
		}

		public void addSearchParam(RuntimeSearchParam... theSearchParam) {
			myAddedSearchParams.clear();
			for (RuntimeSearchParam next : theSearchParam) {
				myAddedSearchParams.add(next);
			}
		}

		@Override
		public void forceRefresh() {
			// nothing
		}

		@Override
		public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceChangeResult refreshCacheIfNecessary() {
			// nothing
			return new ResourceChangeResult();
		}

		public ReadOnlySearchParamCache getActiveSearchParams() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceSearchParams getActiveSearchParams(String theResourceName) {
			RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(theResourceName);
			ResourceSearchParams retval = new ResourceSearchParams(theResourceName);
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				retval.put(nextSp.getName(), nextSp);
			}
			for (RuntimeSearchParam next : myAddedSearchParams) {
				retval.put(next.getName(), next);
			}
			return retval;
		}

		@Override
		public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName, Set<String> theParamNames) {
			throw new UnsupportedOperationException();
		}

		@Nullable
		@Override
		public RuntimeSearchParam getActiveSearchParamByUrl(String theUrl) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void requestRefresh() {
			// nothing
		}

		@Override
		public void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
			// nothing
		}
	}

}
