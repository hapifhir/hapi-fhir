package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchParamFinder {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamFinder.class);

	public static void main(String[] args) {

		RestSearchParameterTypeEnum type = RestSearchParameterTypeEnum.TOKEN;
		process(FhirContext.forDstu2(), type);
		process(FhirContext.forDstu3(), type);
		process(FhirContext.forR4(), type);
		process(FhirContext.forR5(), type);

	}

	public static void process(FhirContext theCtx, RestSearchParameterTypeEnum theWantType) {
		for (String nextResourceName : theCtx.getResourceNames()) {
			RuntimeResourceDefinition nextResDef = theCtx.getResourceDefinition(nextResourceName);
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				if (nextSp.getParamType() == theWantType) {
					for (String nextPath : nextSp.getPathsSplit()) {
						List<String> pathsPart = new ArrayList<>(Arrays.asList(nextPath.split("\\.")));
						BaseRuntimeElementCompositeDefinition def = null;
						traverse(theCtx, nextSp, pathsPart, theCtx, pathsPart, def);
					}
				}
			}
		}
	}

	private static void traverse(FhirContext theCtx, RuntimeSearchParam theSearchParam, List<String> theFullPath, FhirContext theContext, List<String> thePathsPart, BaseRuntimeElementCompositeDefinition theDef) {
		if (thePathsPart.size() == 0) {
			ourLog.info("Found {} - {} - {} - {}", theCtx.getVersion().getVersion(), theSearchParam.getName(), String.join(".", theFullPath), theDef.getName());
			return;
		}

		String nextName = thePathsPart.get(0);
		if (theDef == null) {
			RuntimeResourceDefinition def = theContext.getResourceDefinition(nextName);
			traverse(theCtx, theSearchParam, theFullPath, theContext, thePathsPart.subList(1, thePathsPart.size()), def);
		} else {
			BaseRuntimeChildDefinition child = theDef.getChildByName(nextName);
			if (child != null) {
				BaseRuntimeElementDefinition<?> subChild = child.getChildByName(nextName);
				if (subChild instanceof RuntimePrimitiveDatatypeDefinition) {
					assert thePathsPart.size() == 1;
					ourLog.info("Found {} - {} - {} - {}", theCtx.getVersion().getVersion(), theSearchParam.getName(), String.join(".", theFullPath), subChild.getName());
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
							return;
						}
						BaseRuntimeElementCompositeDefinition def = (BaseRuntimeElementCompositeDefinition) subChild;
						traverse(theCtx, theSearchParam, theFullPath, theContext, thePathsPart.subList(1, thePathsPart.size()), def);
					}
				}
			}
		}
	}


}
