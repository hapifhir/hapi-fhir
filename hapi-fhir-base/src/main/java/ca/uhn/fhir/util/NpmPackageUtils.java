package ca.uhn.fhir.util;

import ca.uhn.fhir.context.RuntimeResourceSource;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.npm.NpmPackageMetadataLiteJson;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class NpmPackageUtils {

	public static final String LOADER_WITH_CACHE = "loaderWithCache";

	/**
	 * Default install types
	 */
	public static List<String> DEFAULT_INSTALL_TYPES = Collections.unmodifiableList(Lists.newArrayList(
		"NamingSystem",
		"CodeSystem",
		"ValueSet",
		"StructureDefinition",
		"ConceptMap",
		"SearchParameter",
		"Subscription"));

	public static final String PKG_METADATA_KEY = "PKG_METADATA";

	/**
	 * Adds package metadata to the resource for identification purposes downstream.
	 * @param theResource the resource
	 * @param thePkg the npm package (IG) it is from
	 */
	public static void addPackageMetadata(IBaseResource theResource, String thePkgName, String thePkgVersion) {
		NpmPackageMetadataLiteJson metadata = new NpmPackageMetadataLiteJson();
		metadata.setName(thePkgName);
		metadata.setVersion(thePkgVersion);

		theResource.setUserData(PKG_METADATA_KEY, JsonUtil.serialize(metadata));
	}

	/**
	 * Retrieves whatever npm pkg metadata is on this resource, or null if none is found.
	 * @param theResource resource that originated from an npm package
	 * @return the metadata about the pkg (or null if not available)
	 */
	public static NpmPackageMetadataLiteJson getPackageMetadata(IBaseResource theResource) {
		String metadataJson = (String) theResource.getUserData(PKG_METADATA_KEY);
		if (isBlank(metadataJson)) {
			// metadata was not on this resource
			return null;
		}
		return JsonUtil.deserialize(metadataJson, NpmPackageMetadataLiteJson.class);
	}

	public static boolean isFromNpmPackage(IBaseResource theResource) {
		return theResource.getUserData(PKG_METADATA_KEY) != null;
	}

	public static void setSourceForSP(IBaseResource theResource, RuntimeSearchParam theSearchParam) {
		assert isFromNpmPackage(theResource);
		NpmPackageMetadataLiteJson metadata = getPackageMetadata(theResource);
		theSearchParam.setSource(RuntimeResourceSource.npmSource(metadata.getName(), metadata.getVersion()));
	}
}
