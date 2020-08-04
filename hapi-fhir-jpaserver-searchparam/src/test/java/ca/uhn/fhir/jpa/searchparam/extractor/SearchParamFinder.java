package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamFinder {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamFinder.class);

	public static void main(String[] args) {

		RestSearchParameterTypeEnum type = RestSearchParameterTypeEnum.STRING;
		process(FhirContext.forDstu2(), type);
		process(FhirContext.forDstu3(), type);
		process(FhirContext.forR4(), type);
		process(FhirContext.forR5(), type);

	}

	public static void process(FhirContext theCtx, RestSearchParameterTypeEnum theWantType) {
		for (String nextResourceName : theCtx.getResourceTypes()) {
			RuntimeResourceDefinition nextResDef = theCtx.getResourceDefinition(nextResourceName);
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				if (nextSp.getName().equals("_id")){
					continue;
				}

				if (nextSp.getParamType() == theWantType) {
					for (String nextPath : nextSp.getPathsSplit()) {
						if (isBlank(nextPath)) {
							continue;
						}

						if (nextPath.startsWith("(") && nextPath.endsWith(")")) {
							nextPath = nextPath.substring(1, nextPath.length()-1);
						}
						if (nextPath.startsWith("(")) {
							ourLog.warn("Skipping unparseable path: " + nextPath);
							continue;
						}


						if (Character.isLowerCase(nextPath.charAt(0))) {
							nextPath = nextResourceName + "." + nextPath;
						}

						List<String> pathsPart = new ArrayList<>(Arrays.asList(nextPath.split("\\.")));
						BaseRuntimeElementCompositeDefinition def = null;
						traverse(theCtx, nextSp, pathsPart, theCtx, pathsPart, def);
					}
				}
			}
		}
	}

	private static void traverse(FhirContext theCtx, RuntimeSearchParam theSearchParam, List<String> theFullPath, FhirContext theContext, List<String> thePathsPart, BaseRuntimeElementCompositeDefinition theDef) {
		Validate.notNull(theSearchParam);

		if (thePathsPart.size() == 0) {
			ourLog.info("Found {} - {} - {} - {}", theCtx.getVersion().getVersion(), theSearchParam.getName(), String.join(".", theFullPath), theDef.getName());
			handle(theCtx, theSearchParam, theDef.getName());
			return;
		}

		String nextName = thePathsPart.get(0);
		if (nextName.endsWith("[x]")) {
			nextName = nextName.substring(0, nextName.length() - 3);
		}

		if (theDef == null) {
			Validate.notBlank(nextName, "Invalid path: " + theFullPath + " for " + theSearchParam);
			RuntimeResourceDefinition def;
			try {
				def = theContext.getResourceDefinition(nextName);
			} catch (DataFormatException e) {
				throw new IllegalArgumentException("Unknown resource: " + theFullPath + " for " + theSearchParam);
			}
			traverse(theCtx, theSearchParam, theFullPath, theContext, thePathsPart.subList(1, thePathsPart.size()), def);
		} else {
			BaseRuntimeChildDefinition child = theDef.getChildByName(nextName);
			if (child != null) {
				BaseRuntimeElementDefinition<?> subChild = child.getChildByName(nextName);
				Validate.notNull(subChild, "Unknown child: " + nextName + " in path: " + theFullPath);
				if (subChild instanceof RuntimePrimitiveDatatypeDefinition) {
					assert thePathsPart.size() == 1;
					ourLog.info("Found {} - {} - {} - {}", theCtx.getVersion().getVersion(), theSearchParam.getName(), String.join(".", theFullPath), subChild.getName());
					handle(theCtx, theSearchParam, subChild.getName());
					return;
				}

				BaseRuntimeElementCompositeDefinition def = (BaseRuntimeElementCompositeDefinition) subChild;
				traverse(theCtx, theSearchParam, theFullPath, theContext, thePathsPart.subList(1, thePathsPart.size()), def);
			}

			RuntimeChildChoiceDefinition choiceChild = (RuntimeChildChoiceDefinition) theDef.getChildByName(nextName + "[x]");
			if (choiceChild != null){
				for (String nextChildName : choiceChild.getValidChildNames()) {
					if (nextChildName.startsWith(nextName)) {
						BaseRuntimeElementDefinition<?> subChild = choiceChild.getChildByName(nextChildName);
						if (subChild instanceof RuntimePrimitiveDatatypeDefinition) {
							assert thePathsPart.size() == 1;
							ourLog.info("Found {} - {} - {} - {}", theCtx.getVersion().getVersion(), theSearchParam.getName(), String.join(".", theFullPath), subChild.getName());
							handle(theCtx, theSearchParam, subChild.getName());
							return;
						}
						BaseRuntimeElementCompositeDefinition def = (BaseRuntimeElementCompositeDefinition) subChild;
						traverse(theCtx, theSearchParam, theFullPath, theContext, thePathsPart.subList(1, thePathsPart.size()), def);
					}
				}
			}
		}
	}

	private static void handle(FhirContext theCtx, RuntimeSearchParam theSearchParam, String theName) {
		BaseSearchParamExtractor extractor;
		switch (theCtx.getVersion().getVersion()) {
			case DSTU2:
				extractor = new SearchParamExtractorDstu2();
				break;
			default:
				return;
		}


		IBase element = theCtx.getElementDefinition(theName).newInstance();
//		extractor.extractSearchParamDates()

	}


}
