package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} which may be pre-populated
 * with a collection of validation resources to be used by the validator.
 */
public class PrePopulatedValidationSupport extends BaseStaticResourceValidationSupport implements IValidationSupport {

	private final Map<String, IBaseResource> myCodeSystems;
	private final Map<String, IBaseResource> myStructureDefinitions;
	private final Map<String, IBaseResource> myValueSets;

	/**
	 * Constructor
	 */
	public PrePopulatedValidationSupport(FhirContext theContext) {
		this(theContext, new HashMap<>(), new HashMap<>(), new HashMap<>());
	}


	/**
	 * Constructor
	 *
	 * @param theStructureDefinitions The StructureDefinitions to be returned by this module. Keys are the logical URL for the resource, and
	 *                                values are the resource itself.
	 * @param theValueSets            The ValueSets to be returned by this module. Keys are the logical URL for the resource, and values are
	 *                                the resource itself.
	 * @param theCodeSystems          The CodeSystems to be returned by this module. Keys are the logical URL for the resource, and values are
	 *                                the resource itself.
	 */
	public PrePopulatedValidationSupport(FhirContext theFhirContext, Map<String, IBaseResource> theStructureDefinitions, Map<String, IBaseResource> theValueSets, Map<String, IBaseResource> theCodeSystems) {
		super(theFhirContext);
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		Validate.notNull(theStructureDefinitions, "theStructureDefinitions must not be null");
		Validate.notNull(theValueSets, "theValueSets must not be null");
		Validate.notNull(theCodeSystems, "theCodeSystems must not be null");
		myStructureDefinitions = theStructureDefinitions;
		myValueSets = theValueSets;
		myCodeSystems = theCodeSystems;
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
		Set<String> urls = processResourceAndReturnUrls(theCodeSystem, "CodeSystem");
		addToMap(theCodeSystem, myCodeSystems, urls);
	}

	private Set<String> processResourceAndReturnUrls(IBaseResource theResource, String theResourceName) {
		Validate.notNull(theResource, "the" + theResourceName + " must not be null");
		RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(theResource);
		String actualResourceName = resourceDef.getName();
		Validate.isTrue(actualResourceName.equals(theResourceName), "the" + theResourceName + " must be a " + theResourceName + " - Got: " + actualResourceName);

		Optional<IBase> urlValue = resourceDef.getChildByName("url").getAccessor().getFirstValueOrNull(theResource);
		String url = urlValue.map(t -> (((IPrimitiveType<?>) t).getValueAsString())).orElse(null);

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

		Optional<IBase> versionValue = resourceDef.getChildByName("version").getAccessor().getFirstValueOrNull(theResource);
		String version = versionValue.map(t -> (((IPrimitiveType<?>) t).getValueAsString())).orElse(null);
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
		Set<String> url = processResourceAndReturnUrls(theStructureDefinition, "StructureDefinition");
		addToMap(theStructureDefinition, myStructureDefinitions, url);
	}

	private <T extends IBaseResource> void addToMap(T theResource, Map<String, T> theMap, Collection<String> theUrls) {
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
		Set<String> urls = processResourceAndReturnUrls(theValueSet, "ValueSet");
		addToMap(theValueSet, myValueSets, urls);
	}


	/**
	 * @param theResource The resource. This method delegates to the type-specific methods (e.g. {@link #addCodeSystem(IBaseResource)})
	 *                    and will do nothing if the resource type is not supported by this class.
	 * @since 5.5.0
	 */
	public void addResource(@Nonnull IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");

		switch (getFhirContext().getResourceType(theResource)) {
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
		retVal.addAll(myCodeSystems.values());
		retVal.addAll(myStructureDefinitions.values());
		retVal.addAll(myValueSets.values());
		return retVal;
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return toList(myStructureDefinitions);
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return myCodeSystems.get(theSystem);
	}

	@Override
	public IBaseResource fetchValueSet(String theUri) {
		return myValueSets.get(theUri);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return myStructureDefinitions.get(theUrl);
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return myCodeSystems.containsKey(theSystem);
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return myValueSets.containsKey(theValueSetUrl);
	}
}
