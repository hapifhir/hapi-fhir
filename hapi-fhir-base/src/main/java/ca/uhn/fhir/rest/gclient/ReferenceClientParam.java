package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Arrays;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
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


public class ReferenceClientParam extends BaseClientParam  implements IParam {

	private String myName;

	public ReferenceClientParam(String theName) {
		myName = theName;
	}

	@Override
	public String getParamName() {
		return myName;
	}

	/**
	 * Include a chained search. For example:
	 * <pre>
	 * Bundle resp = ourClient
	 *   .search()
	 *   .forResource(QuestionnaireResponse.class)
	 *   .where(QuestionnaireResponse.SUBJECT.hasChainedProperty(Patient.FAMILY.matches().value("SMITH")))
	 *   .returnBundle(Bundle.class)
	 *   .execute();
	 * </pre>
	 */
	public ICriterion<ReferenceClientParam> hasChainedProperty(ICriterion<?> theCriterion) {
		return new ReferenceChainCriterion(getParamName(), theCriterion);
	}

	/**
	 * Include a chained search with a resource type. For example:
	 * <pre>
	 * Bundle resp = ourClient
	 *   .search()
	 *   .forResource(QuestionnaireResponse.class)
	 *   .where(QuestionnaireResponse.SUBJECT.hasChainedProperty("Patient", Patient.FAMILY.matches().value("SMITH")))
	 *   .returnBundle(Bundle.class)
	 *   .execute();
	 * </pre>
	 */
	public ICriterion<ReferenceClientParam> hasChainedProperty(String theResourceType, ICriterion<?> theCriterion) {
		return new ReferenceChainCriterion(getParamName(), theResourceType, theCriterion);
	}

	/**
	 * Match the referenced resource if the resource has the given ID (this can be
	 * the logical ID or the absolute URL of the resource)
	 */
	public ICriterion<ReferenceClientParam> hasId(IIdType theId) {
		return new StringCriterion<>(getParamName(), theId.getValue());
	}

	/**
	 * Match the referenced resource if the resource has the given ID (this can be
	 * the logical ID or the absolute URL of the resource)
	 */
	public ICriterion<ReferenceClientParam> hasId(String theId) {
		return new StringCriterion<>(getParamName(), theId);
	}

	/**
	 * Match the referenced resource if the resource has ANY of the given IDs
	 * (this is an OR search, not an AND search), (this can be the logical ID or
	 * the absolute URL of the resource). Note that to specify an AND search,
	 * simply add a subsequent {@link IQuery#where(ICriterion) where} criteria
	 * with the same parameter.
	 */
	public ICriterion<ReferenceClientParam> hasAnyOfIds(Collection<String> theIds) {
		return new StringCriterion<>(getParamName(), theIds);
	}

	/**
	 * Match the referenced resource if the resource has ANY of the given IDs
	 * (this is an OR search, not an AND search), (this can be the logical ID or
	 * the absolute URL of the resource). Note that to specify an AND search,
	 * simply add a subsequent {@link IQuery#where(ICriterion) where} criteria
	 * with the same parameter.
	 */
	public ICriterion<ReferenceClientParam> hasAnyOfIds(String... theIds) {
		Validate.notNull(theIds, "theIds must not be null");
		return hasAnyOfIds(Arrays.asList(theIds));
	}

	private static class ReferenceChainCriterion implements ICriterion<ReferenceClientParam>, ICriterionInternal {

		private final String myResourceTypeQualifier;
		private String myParamName;
		private ICriterionInternal myWrappedCriterion;

		ReferenceChainCriterion(String theParamName, ICriterion<?> theWrappedCriterion) {
			this(theParamName, null, theWrappedCriterion);
		}

		ReferenceChainCriterion(String theParamName, String theResourceType, ICriterion<?> theWrappedCriterion) {
			myParamName = theParamName;
			myResourceTypeQualifier = isNotBlank(theResourceType) ? ":" + theResourceType : "";
			myWrappedCriterion = (ICriterionInternal) theWrappedCriterion;
		}

		@Override
		public String getParameterName() {
			return myParamName + myResourceTypeQualifier + "." + myWrappedCriterion.getParameterName();
		}

		@Override
		public String getParameterValue(FhirContext theContext) {
			return myWrappedCriterion.getParameterValue(theContext);
		}

	}

}
