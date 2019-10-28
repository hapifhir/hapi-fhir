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

		FhirContext ctx = FhirContext.forR5();
		RestSearchParameterTypeEnum wantType = RestSearchParameterTypeEnum.NUMBER;

		for (String nextResourceName : ctx.getResourceNames()) {
			RuntimeResourceDefinition nextResDef = ctx.getResourceDefinition(nextResourceName);
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {

				if (nextSp.getParamType() == wantType) {

					boolean logged = false;
					for (String nextPath : nextSp.getPathsSplit()) {

						List<String> pathsPart = new ArrayList<>(Arrays.asList(nextPath.split("\\.")));

						BaseRuntimeElementCompositeDefinition def = null;

						traverse(ctx, pathsPart, def);
//						for (int i = 0; i < pathsPart.length; i++) {
//							String nextPart = pathsPart[i];
//
//							if (i == 0) {
//								def = ctx.getResourceDefinition(nextPart);
//							} else {
//								BaseRuntimeChildDefinition child = def.getChildByName(nextPart);
//								if (child == null) {
//									child = def.getChildByName(nextPart + "[x]");
//								}
//								BaseRuntimeElementDefinition<?> childDef = child.getChildByName(nextPart);
//								if (childDef instanceof RuntimePrimitiveDatatypeDefinition) {
//									ourLog.info("SearchParam {} : {} : {} has {}", nextResourceName, nextSp.getName(), nextSp.getPath(), childDef.getName());
//									logged = true;
//									break;
//								}
//								def = (BaseRuntimeElementCompositeDefinition) childDef;
//							}
//
//						}
					}

					if (!logged) {
						ourLog.info("SearchParam {} : {} : {}", nextResourceName, nextSp.getName(), nextSp.getPath());
					}

				}


			}
		}

	}

	private static void traverse(FhirContext theContext, List<String> thePathsPart, BaseRuntimeElementCompositeDefinition theDef) {
		if (thePathsPart.size() == 0) {
			ourLog.info("{} - {}", thePathsPart, theDef.getName());
			return;
		}

		String nextName = thePathsPart.get(0);
		if (theDef == null) {
			RuntimeResourceDefinition def = theContext.getResourceDefinition(nextName);
			traverse(theContext, thePathsPart.subList(1, thePathsPart.size()), def);
		} else {
			BaseRuntimeChildDefinition child = theDef.getChildByName(nextName);
			if (child != null) {
				BaseRuntimeElementCompositeDefinition def = (BaseRuntimeElementCompositeDefinition) child.getChildByName(nextName);
				traverse(theContext, thePathsPart.subList(1, thePathsPart.size()), def);
			}

			RuntimeChildChoiceDefinition choiceChild = (RuntimeChildChoiceDefinition) theDef.getChildByName(nextName + "[x]");
			if (choiceChild != null){
				for (String nextChildName : choiceChild.getValidChildNames()) {
					if (nextChildName.startsWith(nextName)) {
						BaseRuntimeElementCompositeDefinition def = (BaseRuntimeElementCompositeDefinition) 		choiceChild.getChildByName(nextChildName);
						traverse(theContext, thePathsPart.subList(1, thePathsPart.size()), def);
					}
				}
			}
		}
	}


}
