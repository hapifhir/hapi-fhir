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

// LUKETODO:  javadoc
public class ResourceHistoryCalculator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceHistoryCalculator.class);
	private static final HashFunction SHA_256 = Hashing.sha256();

	private final FhirContext myFhirContext;
	private final boolean myIsOracleDialect;

	public ResourceHistoryCalculator(FhirContext theFhirContext, boolean theIsOracleDialect) {
		myFhirContext = theFhirContext;
		myIsOracleDialect = theIsOracleDialect;
	}

	ResourceHistoryState calculate(IBaseResource theResource, ResourceEncodingEnum theEncoding, List<String> theExcludeElements) {
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

	boolean calculatedIsChangedOther(
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

	boolean calculateIsChanged(
		ResourceHistoryTable theCurrentHistoryVersion,
		@Nullable byte[] theResourceBinary,
		@Nullable String resourceText) {
		if (myIsOracleDialect) {
			return !Arrays.equals(theCurrentHistoryVersion.getResource(), theResourceBinary);
		}

		return !StringUtils.equals(theCurrentHistoryVersion.getResourceTextVc(), resourceText);
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

	String encodeResource(
			IBaseResource theResource,
			ResourceEncodingEnum theEncoding,
			List<String> theExcludeElements) {
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
		return switch (theEncoding) {
            case JSON -> theEncodedResource.getBytes(StandardCharsets.UTF_8);
            case JSONC -> GZipUtil.compress(theEncodedResource);
            default -> new byte[0];
        };
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
