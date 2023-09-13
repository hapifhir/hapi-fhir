package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.util.ILockable;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} which may be pre-populated
 * with a collection of validation resources to be used by the validator.
 */
public class PrePopulatedValidationSupport extends BaseStaticResourceValidationSupport
		implements IValidationSupport, ILockable {

	private final Map<String, IBaseResource> myUrlToCodeSystems;
	private final Map<String, IBaseResource> myUrlToStructureDefinitions;
	private final Map<String, IBaseResource> myUrlToSearchParameters;
	private final Map<String, IBaseResource> myUrlToValueSets;
	private final List<IBaseResource> myCodeSystems;
	private final List<IBaseResource> myStructureDefinitions;
	private final List<IBaseResource> mySearchParameters;
	private final List<IBaseResource> myValueSets;
	private final Map<String, byte[]> myBinaries;
	private boolean myLocked;

	/**
	 * Constructor
	 */
	public PrePopulatedValidationSupport(FhirContext theContext) {
		this(theContext, new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
	}

	/**
	 * Constructor
	 *
	 * @param theUrlToStructureDefinitions The StructureDefinitions to be returned by this module. Keys are the logical URL for the resource, and
	 *                                values are the resource itself.
	 * @param theUrlToValueSets            The ValueSets to be returned by this module. Keys are the logical URL for the resource, and values are
	 *                                the resource itself.
	 * @param theUrlToCodeSystems          The CodeSystems to be returned by this module. Keys are the logical URL for the resource, and values are
	 *                                the resource itself.
	 **/
	public PrePopulatedValidationSupport(
			FhirContext theFhirContext,
			Map<String, IBaseResource> theUrlToStructureDefinitions,
			Map<String, IBaseResource> theUrlToValueSets,
			Map<String, IBaseResource> theUrlToCodeSystems) {
		this(
				theFhirContext,
				theUrlToStructureDefinitions,
				theUrlToValueSets,
				theUrlToCodeSystems,
				new HashMap<>(),
				new HashMap<>());
	}

	/**
	 * Constructor
	 *
	 * @param theUrlToStructureDefinitions The StructureDefinitions to be returned by this module. Keys are the logical URL for the resource, and
	 *                                values are the resource itself.
	 * @param theUrlToValueSets            The ValueSets to be returned by this module. Keys are the logical URL for the resource, and values are
	 *                                the resource itself.
	 * @param theUrlToCodeSystems          The CodeSystems to be returned by this module. Keys are the logical URL for the resource, and values are
	 *                                the resource itself.
	 * @param theBinaries				 The binary files to be returned by this module. Keys are the unique filename for the binary, and values
	 *                                are the contents of the file as a byte array.
	 */
	public PrePopulatedValidationSupport(
			FhirContext theFhirContext,
			Map<String, IBaseResource> theUrlToStructureDefinitions,
			Map<String, IBaseResource> theUrlToValueSets,
			Map<String, IBaseResource> theUrlToCodeSystems,
			Map<String, IBaseResource> theUrlToSearchParameters,
			Map<String, byte[]> theBinaries) {
		super(theFhirContext);
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		Validate.notNull(theUrlToStructureDefinitions, "theStructureDefinitions must not be null");
		Validate.notNull(theUrlToValueSets, "theValueSets must not be null");
		Validate.notNull(theUrlToCodeSystems, "theCodeSystems must not be null");
		Validate.notNull(theUrlToSearchParameters, "theSearchParameters must not be null");
		Validate.notNull(theBinaries, "theBinaries must not be null");
		myUrlToStructureDefinitions = theUrlToStructureDefinitions;
		myStructureDefinitions =
				theUrlToStructureDefinitions.values().stream().distinct().collect(Collectors.toList());

		myUrlToValueSets = theUrlToValueSets;
		myValueSets = theUrlToValueSets.values().stream().distinct().collect(Collectors.toList());

		myUrlToCodeSystems = theUrlToCodeSystems;
		myCodeSystems = theUrlToCodeSystems.values().stream().distinct().collect(Collectors.toList());

		myUrlToSearchParameters = theUrlToSearchParameters;
		mySearchParameters =
				theUrlToSearchParameters.values().stream().distinct().collect(Collectors.toList());

		myBinaries = theBinaries;
	}

	public void addBinary(byte[] theBinary, String theBinaryKey) {
		validateNotLocked();
		Validate.notNull(theBinary, "theBinaryKey must not be null");
		Validate.notNull(theBinary, "the" + theBinaryKey + " must not be null");
		myBinaries.put(theBinaryKey, theBinary);
	}

	private synchronized void validateNotLocked() {
		Validate.isTrue(myLocked == false, "Can not add to validation support, module is locked");
	}

	/**
	 * Add a new CodeSystem resource which will be available to the validator. Note that
	 * {@link CodeSystem#getUrl() the URL field) in this resource must contain a value as this
	 * value will be used as the logical URL.
	 * <p>
	 * Note that if the URL is a canonical FHIR URL (e.g. http://hl7.org/StructureDefinition/Extension),
	 * it will be stored in three ways:
	 * <ul>
	 * <li>Extension</li>
	 * <li>StructureDefinition/Extension</li>
	 * <li>http://hl7.org/StructureDefinition/Extension</li>
	 * </ul>
	 * </p>
	 */
	public void addCodeSystem(IBaseResource theCodeSystem) {
		validateNotLocked();
		Set<String> urls = processResourceAndReturnUrls(theCodeSystem, "CodeSystem");
		addToMap(theCodeSystem, myCodeSystems, myUrlToCodeSystems, urls);
	}

	private Set<String> processResourceAndReturnUrls(IBaseResource theResource, String theResourceName) {
		Validate.notNull(theResource, "the" + theResourceName + " must not be null");
		RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(theResource);
		String actualResourceName = resourceDef.getName();
		Validate.isTrue(
				actualResourceName.equals(theResourceName),
				"the" + theResourceName + " must be a " + theResourceName + " - Got: " + actualResourceName);

		Optional<IBase> urlValue =
				resourceDef.getChildByName("url").getAccessor().getFirstValueOrNull(theResource);
		String url =
				urlValue.map(t -> (((IPrimitiveType<?>) t).getValueAsString())).orElse(null);

		Validate.notNull(url, "the" + theResourceName + ".getUrl() must not return null");
		Validate.notBlank(url, "the" + theResourceName + ".getUrl() must return a value");

		String urlWithoutVersion;
		int pipeIdx = url.indexOf('|');
		if (pipeIdx != -1) {
			urlWithoutVersion = url.substring(0, pipeIdx);
		} else {
			urlWithoutVersion = url;
		}

		HashSet<String> retVal = Sets.newHashSet(url, urlWithoutVersion);

		Optional<IBase> versionValue =
				resourceDef.getChildByName("version").getAccessor().getFirstValueOrNull(theResource);
		String version = versionValue
				.map(t -> (((IPrimitiveType<?>) t).getValueAsString()))
				.orElse(null);
		if (isNotBlank(version)) {
			retVal.add(urlWithoutVersion + "|" + version);
		}

		return retVal;
	}

	/**
	 * Add a new StructureDefinition resource which will be available to the validator. Note that
	 * {@link StructureDefinition#getUrl() the URL field) in this resource must contain a value as this
	 * value will be used as the logical URL.
	 * <p>
	 * Note that if the URL is a canonical FHIR URL (e.g. http://hl7.org/StructureDefinition/Extension),
	 * it will be stored in three ways:
	 * <ul>
	 * <li>Extension</li>
	 * <li>StructureDefinition/Extension</li>
	 * <li>http://hl7.org/StructureDefinition/Extension</li>
	 * </ul>
	 * </p>
	 */
	public void addStructureDefinition(IBaseResource theStructureDefinition) {
		validateNotLocked();
		Set<String> url = processResourceAndReturnUrls(theStructureDefinition, "StructureDefinition");
		addToMap(theStructureDefinition, myStructureDefinitions, myUrlToStructureDefinitions, url);
	}

	public void addSearchParameter(IBaseResource theSearchParameter) {
		validateNotLocked();
		Set<String> url = processResourceAndReturnUrls(theSearchParameter, "SearchParameter");
		addToMap(theSearchParameter, mySearchParameters, myUrlToSearchParameters, url);
	}

	private <T extends IBaseResource> void addToMap(
			T theResource, List<T> theList, Map<String, T> theMap, Collection<String> theUrls) {
		theList.add(theResource);
		for (String urls : theUrls) {
			if (isNotBlank(urls)) {
				theMap.put(urls, theResource);

				int lastSlashIdx = urls.lastIndexOf('/');
				if (lastSlashIdx != -1) {
					theMap.put(urls.substring(lastSlashIdx + 1), theResource);
					int previousSlashIdx = urls.lastIndexOf('/', lastSlashIdx - 1);
					if (previousSlashIdx != -1) {
						theMap.put(urls.substring(previousSlashIdx + 1), theResource);
					}
				}
			}
		}
	}

	/**
	 * Add a new ValueSet resource which will be available to the validator. Note that
	 * {@link ValueSet#getUrl() the URL field) in this resource must contain a value as this
	 * value will be used as the logical URL.
	 * <p>
	 * Note that if the URL is a canonical FHIR URL (e.g. http://hl7.org/StructureDefinition/Extension),
	 * it will be stored in three ways:
	 * <ul>
	 * <li>Extension</li>
	 * <li>StructureDefinition/Extension</li>
	 * <li>http://hl7.org/StructureDefinition/Extension</li>
	 * </ul>
	 * </p>
	 */
	public void addValueSet(IBaseResource theValueSet) {
		validateNotLocked();
		Set<String> urls = processResourceAndReturnUrls(theValueSet, "ValueSet");
		addToMap(theValueSet, myValueSets, myUrlToValueSets, urls);
	}

	/**
	 * @param theResource The resource. This method delegates to the type-specific methods (e.g. {@link #addCodeSystem(IBaseResource)})
	 *                    and will do nothing if the resource type is not supported by this class.
	 * @since 5.5.0
	 */
	public void addResource(@Nonnull IBaseResource theResource) {
		validateNotLocked();
		Validate.notNull(theResource, "theResource must not be null");

		switch (getFhirContext().getResourceType(theResource)) {
			case "SearchParameter":
				addSearchParameter(theResource);
				break;
			case "StructureDefinition":
				addStructureDefinition(theResource);
				break;
			case "CodeSystem":
				addCodeSystem(theResource);
				break;
			case "ValueSet":
				addValueSet(theResource);
				break;
		}
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		retVal.addAll(myCodeSystems);
		retVal.addAll(myStructureDefinitions);
		retVal.addAll(myValueSets);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllSearchParameters() {
		return (List<T>) Collections.unmodifiableList(mySearchParameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return (List<T>) Collections.unmodifiableList(myStructureDefinitions);
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return myUrlToCodeSystems.get(theSystem);
	}

	@Override
	public IBaseResource fetchValueSet(String theUri) {
		return myUrlToValueSets.get(theUri);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return myUrlToStructureDefinitions.get(theUrl);
	}

	@Override
	public byte[] fetchBinary(String theBinaryKey) {
		return myBinaries.get(theBinaryKey);
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return myUrlToCodeSystems.containsKey(theSystem);
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return myUrlToValueSets.containsKey(theValueSetUrl);
	}

	/**
	 * Returns a count of all known resources
	 */
	public int countAll() {
		return myBinaries.size()
				+ myCodeSystems.size()
				+ myStructureDefinitions.size()
				+ myValueSets.size()
				+ myStructureDefinitions.size();
	}

	@Override
	public synchronized void lock() {
		myLocked = true;
	}
}
