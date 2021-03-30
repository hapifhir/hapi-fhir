package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.slf4j.LoggerFactory.getLogger;

@Deprecated
public final class TerserUtil {
	private static final Logger ourLog = getLogger(TerserUtil.class);

	public static final Collection<String> IDS_AND_META_EXCLUDES = ca.uhn.fhir.util.TerserUtil.IDS_AND_META_EXCLUDES;

	public static final Predicate<String> EXCLUDE_IDS_AND_META = ca.uhn.fhir.util.TerserUtil.EXCLUDE_IDS_AND_META;

	private TerserUtil() {
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void cloneEidIntoResource(FhirContext theFhirContext, BaseRuntimeChildDefinition theIdentifierDefinition, IBase theEid, IBase theResourceToCloneEidInto) {
		ca.uhn.fhir.util.TerserUtil.cloneEidIntoResource(theFhirContext, theIdentifierDefinition, theEid, theResourceToCloneEidInto);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static boolean hasValues(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		return ca.uhn.fhir.util.TerserUtil.hasValues(theFhirContext, theResource, theFieldName);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static List<IBase> getValues(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		return ca.uhn.fhir.util.TerserUtil.getValues(theFhirContext, theResource, theFieldName);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void cloneCompositeField(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, String field) {
		ca.uhn.fhir.util.TerserUtil.cloneCompositeField(theFhirContext, theFrom, theTo, field);

	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void mergeAllFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo) {
		ca.uhn.fhir.util.TerserUtil.mergeAllFields(theFhirContext, theFrom, theTo);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void replaceFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, Predicate<String> inclusionStrategy) {
		ca.uhn.fhir.util.TerserUtil.replaceFields(theFhirContext, theFrom, theTo, inclusionStrategy);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static boolean fieldExists(FhirContext theFhirContext, String theFieldName, IBaseResource theInstance) {
		return ca.uhn.fhir.util.TerserUtil.fieldExists(theFhirContext, theFieldName, theInstance);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void replaceField(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		ca.uhn.fhir.util.TerserUtil.replaceField(theFhirContext, theFieldName, theFrom, theTo);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void replaceField(FhirContext theFhirContext, FhirTerser theTerser, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		ca.uhn.fhir.util.TerserUtil.replaceField(theFhirContext, theTerser, theFieldName, theFrom, theTo);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void mergeFieldsExceptIdAndMeta(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo) {
		ca.uhn.fhir.util.TerserUtil.mergeFieldsExceptIdAndMeta(theFhirContext, theFrom, theTo);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void mergeFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, Predicate<String> inclusionStrategy) {
		ca.uhn.fhir.util.TerserUtil.mergeFields(theFhirContext, theFrom, theTo, inclusionStrategy);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void mergeField(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		ca.uhn.fhir.util.TerserUtil.mergeField(theFhirContext, theFieldName, theFrom, theTo);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static void mergeField(FhirContext theFhirContext, FhirTerser theTerser, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		ca.uhn.fhir.util.TerserUtil.mergeField(theFhirContext, theTerser, theFieldName, theFrom, theTo);
	}

	/**
	 * @deprecated Use {@link ca.uhn.fhir.util.TerserUtil} instead
	 */
	public static <T extends IBaseResource> T clone(FhirContext theFhirContext, T theInstance) {
		return ca.uhn.fhir.util.TerserUtil.clone(theFhirContext, theInstance);
	}

}
