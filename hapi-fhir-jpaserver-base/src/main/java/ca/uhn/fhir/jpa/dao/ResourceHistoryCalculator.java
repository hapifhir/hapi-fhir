/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.parser.IParser;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Responsible for various resource history-centric and {@link FhirContext} aware operations called by
 * {@link BaseHapiFhirDao} or {@link BaseHapiFhirResourceDao} that require knowledge of whether an Oracle database is
 * being used.
 */
public class ResourceHistoryCalculator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceHistoryCalculator.class);
	private static final HashFunction SHA_256 = Hashing.sha256();

	private final FhirContext myFhirContext;
	private final boolean myIsOracleDialect;

	public ResourceHistoryCalculator(FhirContext theFhirContext, boolean theIsOracleDialect) {
		myFhirContext = theFhirContext;
		myIsOracleDialect = theIsOracleDialect;
	}

	ResourceHistoryState calculateResourceHistoryState(
			IBaseResource theResource, ResourceEncodingEnum theEncoding, List<String> theExcludeElements) {
		final String encodedResource = encodeResource(theResource, theEncoding, theExcludeElements);
		final byte[] resourceBinary;
		final String resourceText;
		final ResourceEncodingEnum encoding;
		final HashCode hashCode;

		if (myIsOracleDialect) {
			resourceText = null;
			resourceBinary = getResourceBinary(theEncoding, encodedResource);
			encoding = theEncoding;
			hashCode = SHA_256.hashBytes(resourceBinary);
		} else {
			resourceText = encodedResource;
			resourceBinary = null;
			encoding = ResourceEncodingEnum.JSON;
			hashCode = SHA_256.hashUnencodedChars(encodedResource);
		}

		return new ResourceHistoryState(resourceText, resourceBinary, encoding, hashCode);
	}

	boolean conditionallyAlterHistoryEntity(
			ResourceTable theEntity, ResourceHistoryTable theHistoryEntity, String theResourceText) {
		if (!myIsOracleDialect) {
			ourLog.debug(
					"Storing text of resource {} version {} as inline VARCHAR",
					theEntity.getResourceId(),
					theHistoryEntity.getVersion());
			theHistoryEntity.setResourceTextVc(theResourceText);
			theHistoryEntity.setResource(null);
			theHistoryEntity.setEncoding(ResourceEncodingEnum.JSON);
			return true;
		}

		return false;
	}

	boolean isResourceHistoryChanged(
			ResourceHistoryTable theCurrentHistoryVersion,
			@Nullable byte[] theResourceBinary,
			@Nullable String resourceText) {
		if (myIsOracleDialect) {
			return !Arrays.equals(theCurrentHistoryVersion.getResource(), theResourceBinary);
		}

		return !StringUtils.equals(theCurrentHistoryVersion.getResourceTextVc(), resourceText);
	}

	String encodeResource(
			IBaseResource theResource, ResourceEncodingEnum theEncoding, List<String> theExcludeElements) {
		final IParser parser = theEncoding.newParser(myFhirContext);
		parser.setDontEncodeElements(theExcludeElements);
		return parser.encodeResourceToString(theResource);
	}

	/**
	 * helper for returning the encoded byte array of the input resource string based on the theEncoding.
	 *
	 * @param theEncoding        the theEncoding to used
	 * @param theEncodedResource the resource to encode
	 * @return byte array of the resource
	 */
	@Nonnull
	static byte[] getResourceBinary(ResourceEncodingEnum theEncoding, String theEncodedResource) {
		switch (theEncoding) {
			case JSON:
				return theEncodedResource.getBytes(StandardCharsets.UTF_8);
			case JSONC:
				return GZipUtil.compress(theEncodedResource);
			default:
				return new byte[0];
		}
	}

	void populateEncodedResource(
			EncodedResource theEncodedResource,
			String theEncodedResourceString,
			@Nullable byte[] theResourceBinary,
			ResourceEncodingEnum theEncoding) {
		if (myIsOracleDialect) {
			populateEncodedResourceInner(theEncodedResource, null, theResourceBinary, theEncoding);
		} else {
			populateEncodedResourceInner(theEncodedResource, theEncodedResourceString, null, ResourceEncodingEnum.JSON);
		}
	}

	private void populateEncodedResourceInner(
			EncodedResource encodedResource,
			String encodedResourceString,
			byte[] theResourceBinary,
			ResourceEncodingEnum theEncoding) {
		encodedResource.setResourceText(encodedResourceString);
		encodedResource.setResourceBinary(theResourceBinary);
		encodedResource.setEncoding(theEncoding);
	}
}
