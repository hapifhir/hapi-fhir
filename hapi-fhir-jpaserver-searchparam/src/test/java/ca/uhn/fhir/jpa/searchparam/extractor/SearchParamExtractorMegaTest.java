package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class SearchParamExtractorMegaTest {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorMegaTest.class);

	/**
	 * This test is my magnum opus :P
	 *
	 * It navigates almost every possible path in every FHIR resource in every version of FHIR,
	 * and creates a resource with that path populated, just to ensure that we can index it
	 * without generating any warnings.
	 */
	@Test
	public void testAllCombinations() throws Exception {

		FhirContext ctx = FhirContext.forDstu2();
		ISearchParamRegistry searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorDstu2(ctx, searchParamRegistry));

		ctx = FhirContext.forDstu3();
		searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorDstu3(null, ctx, new DefaultProfileValidationSupport(ctx), searchParamRegistry));

		ctx = FhirContext.forR4();
		searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorR4(null, ctx, new DefaultProfileValidationSupport(ctx), searchParamRegistry));

		ctx = FhirContext.forR5();
		searchParamRegistry = new MySearchParamRegistry(ctx);
		process(ctx, new SearchParamExtractorR5(ctx, new DefaultProfileValidationSupport(ctx), searchParamRegistry));
	}

	private void process(FhirContext theCtx, BaseSearchParamExtractor theExtractor) throws Exception {
		AtomicInteger indexesCounter = new AtomicInteger();

		for (String nextResourceName : theCtx.getResourceNames()) {
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
				previousChildArguments = theChildStack.get(i-1).getInstanceConstructorArguments();
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
		assertEquals(String.join("\n", set.getWarnings()), 0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

		set = theExtractor.extractSearchParamTokens(resource);
		assertEquals(0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

		set = theExtractor.extractSearchParamUri(resource);
		assertEquals(0, set.getWarnings().size());
		theIndexesCounter.addAndGet(set.size());

	}

	private static class MySearchParamRegistry implements ISearchParamRegistry {

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
		public boolean refreshCacheIfNecessary() {
			// nothing
			return false;
		}

		@Override
		public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
			RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(theResourceName);
			Map<String, RuntimeSearchParam> sps = new HashMap<>();
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				sps.put(nextSp.getName(), nextSp);
			}
			for (RuntimeSearchParam next : myAddedSearchParams) {
				sps.put(next.getName(), next);
			}
			return sps;
		}

		@Override
		public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void requestRefresh() {
			// nothing
		}

		@Override
		public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
			return null;
		}

		@Override
		public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
			return null;
		}
	}

}
