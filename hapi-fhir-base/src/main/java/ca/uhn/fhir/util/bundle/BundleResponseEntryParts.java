package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ElementUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import static ca.uhn.fhir.util.ElementUtil.getSingleValueOrNull;
import static ca.uhn.fhir.util.ElementUtil.setValue;

/**
 * Components of a transaction-respose bundle entry.
 */
public record BundleResponseEntryParts(
		String fullUrl,
		IBaseResource resource,
		String responseStatus,
		String responseLocation,
		String responseEtag,
		IPrimitiveType<Date> responseLastModified,
		IBaseResource responseOutcome) {

	static class Metadata implements PartsConverter<BundleResponseEntryParts> {

		private final BaseRuntimeChildDefinition myFullUrlChildDef;
		private final BaseRuntimeChildDefinition myResourceChildDef;
		private final BaseRuntimeChildDefinition myResponseChildDef;
		private final BaseRuntimeChildDefinition myResponseOutcomeChildDef;
		private final BaseRuntimeChildDefinition myResponseStatusChildDef;
		private final BaseRuntimeChildDefinition myResponseLocation;
		private final BaseRuntimeChildDefinition myResponseEtag;
		private final BaseRuntimeChildDefinition myResponseLastModified;
		private final BaseRuntimeElementCompositeDefinition<?> myEntryElementDef;
		private final BaseRuntimeElementCompositeDefinition<?> myResponseChildContentsDef;

		private Metadata(FhirContext theFhirContext) {

			BaseRuntimeChildDefinition entryChildDef =
					theFhirContext.getResourceDefinition("Bundle").getChildByName("entry");

			myEntryElementDef = (BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");

			myFullUrlChildDef = myEntryElementDef.getChildByName("fullUrl");
			myResourceChildDef = myEntryElementDef.getChildByName("resource");

			myResponseChildDef = myEntryElementDef.getChildByName("response");

			myResponseChildContentsDef =
					(BaseRuntimeElementCompositeDefinition<?>) myResponseChildDef.getChildByName("response");

			myResponseOutcomeChildDef = myResponseChildContentsDef.getChildByName("outcome");
			myResponseStatusChildDef = myResponseChildContentsDef.getChildByName("status");
			myResponseLocation = myResponseChildContentsDef.getChildByName("location");
			myResponseEtag = myResponseChildContentsDef.getChildByName("etag");
			myResponseLastModified = myResponseChildContentsDef.getChildByName("lastModified");
		}

		@Override
		@Nullable
		public BundleResponseEntryParts fromElement(IBase base) {
			if (base == null) {
				return null;
			}

			IBase response = getSingleValueOrNull(base, myResponseChildDef, Function.identity());

			return new BundleResponseEntryParts(
					getSingleValueOrNull(base, myFullUrlChildDef, ElementUtil.CONVERT_PRIMITIVE_TO_STRING),
					getSingleValueOrNull(base, myResourceChildDef, ElementUtil.CAST_BASE_TO_RESOURCE),
					getSingleValueOrNull(response, myResponseStatusChildDef, ElementUtil.CONVERT_PRIMITIVE_TO_STRING),
					getSingleValueOrNull(response, myResponseLocation, ElementUtil.CONVERT_PRIMITIVE_TO_STRING),
					getSingleValueOrNull(response, myResponseEtag, ElementUtil.CONVERT_PRIMITIVE_TO_STRING),
					getSingleValueOrNull(response, myResponseLastModified, ElementUtil.CAST_TO_PRIMITIVE_DATE),
					getSingleValueOrNull(response, myResponseOutcomeChildDef, ElementUtil.CAST_BASE_TO_RESOURCE));
		}

		@Override
		public IBase toElement(BundleResponseEntryParts theParts) {
			Objects.requireNonNull(theParts);
			IBase entry = myEntryElementDef.newInstance();

			setValue(entry, myFullUrlChildDef, theParts.fullUrl());
			setValue(entry, myResourceChildDef, theParts.resource());

			// response parts
			IBase response = myResponseChildContentsDef.newInstance();
			setValue(entry, myResponseChildDef, response);

			setValue(response, myResponseStatusChildDef, theParts.responseStatus());
			setValue(response, myResponseLocation, theParts.responseLocation());
			setValue(response, myResponseEtag, theParts.responseEtag());
			setValue(response, myResponseLastModified, theParts.responseLastModified());
			setValue(response, myResponseOutcomeChildDef, theParts.responseOutcome());

			return entry;
		}
	}

	/**
	 * Build an extractor function that can be used to extract the parts of a bundle entry.
	 * @param theFhirContext for the mappings
	 * @return an extractor function on IBase objects that returns a BundleResponseEntryParts object
	 */
	public static Function<IBase, BundleResponseEntryParts> buildPartsExtractor(FhirContext theFhirContext) {
		PartsConverter<BundleResponseEntryParts> m = getConverter(theFhirContext);
		return m::fromElement;
	}

	@Nonnull
	public static PartsConverter<BundleResponseEntryParts> getConverter(FhirContext theFhirContext) {
		return new Metadata(theFhirContext);
	}

	public static Function<BundleResponseEntryParts, IBase> builder(FhirContext theFhirContext) {
		PartsConverter<BundleResponseEntryParts> m = getConverter(theFhirContext);

		return m::toElement;
	}
}
