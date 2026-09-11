/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CanonicalEID {

	private String mySystem;
	private String myUse;
	private String myValue;

	public CanonicalEID(String theSystem, String theValue, String theUse) {
		mySystem = theSystem;
		myUse = theUse;
		myValue = theValue;
	}

	/**
	 * Constructor is private as we expect you to use the factory method.
	 */
	@SuppressWarnings("rawtypes")
	private CanonicalEID(IFhirPath theFhirPath, IBase theIBase) {
		List<IPrimitiveType> value = theFhirPath.evaluate(theIBase, "value", IPrimitiveType.class);
		List<IPrimitiveType> system = theFhirPath.evaluate(theIBase, "system", IPrimitiveType.class);
		List<IPrimitiveType> use = theFhirPath.evaluate(theIBase, "use", IPrimitiveType.class);

		myUse = use.isEmpty() ? null : use.get(0).getValueAsString();
		myValue = value.isEmpty() ? null : value.get(0).getValueAsString();
		mySystem = system.isEmpty() ? null : system.get(0).getValueAsString();
	}

	/**
	 * Get the appropriate FHIRPath expression to extract the EID identifier values, regardless of resource type.
	 * e.g. if theBaseResource is a patient, and the MDM EID systems are test-system and other-system, this will
	 * return
	 *
	 * Patient.identifier.where(system='test-system' or system='other-system')
	 *
	 */
	private static String buildEidFhirPath(
			FhirContext theFhirContext, Collection<String> theEidSystems, IBaseResource theBaseResource) {
		String systemPredicate = theEidSystems.stream()
				.map(eidSystem -> "system='" + escapeFhirPathStringLiteral(eidSystem) + "'")
				.collect(Collectors.joining(" or "));
		return theFhirContext.getResourceType(theBaseResource) + ".identifier.where(" + systemPredicate + ")";
	}

	/**
	 * Escapes a value for use inside a single-quoted FHIRPath string literal.
	 */
	private static String escapeFhirPathStringLiteral(String theValue) {
		return theValue.replace("\\", "\\\\").replace("'", "\\'");
	}

	public Identifier toR4() {
		return new Identifier()
				.setUse(Identifier.IdentifierUse.fromCode(myUse))
				.setSystem(mySystem)
				.setValue(myValue);
	}

	public org.hl7.fhir.dstu3.model.Identifier toDSTU3() {
		return new org.hl7.fhir.dstu3.model.Identifier()
				.setUse(org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.fromCode(myUse))
				.setSystem(mySystem)
				.setValue(myValue);
	}

	public org.hl7.fhir.r5.model.Identifier toR5() {
		return new org.hl7.fhir.r5.model.Identifier()
				.setUse(org.hl7.fhir.r5.model.Identifier.IdentifierUse.fromCode(myUse))
				.setSystem(mySystem)
				.setValue(myValue);
	}

	public String getSystem() {
		return mySystem;
	}

	public String getUse() {
		return myUse;
	}

	public String getValue() {
		return myValue;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public void setUse(String theUse) {
		myUse = theUse;
	}

	private void setValue(String theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		return getSystemAndValueKey();
	}

	/**
	 * Returns the identity of this EID for matching purposes: its system and value, but not its use.
	 * {@code Identifier.use} routinely differs between a source resource and the golden resource cloned
	 * from it, so it must not take part in deciding whether two EIDs refer to the same thing.
	 *
	 * @return a key of the form {@code system|value}
	 */
	public String getSystemAndValueKey() {
		return mySystem + '|' + myValue;
	}

	/**
	 * A Factory method to generate a {@link CanonicalEID} object from an incoming resource.
	 *
	 * @param theFhirContext the {@link FhirContext} of the application, used to generate a FHIRPath parser.
	 * @param theEidSystem the enterprise identifier system URI used by this instance.
	 * @param theBaseResource the {@link IBaseResource} from which you would like to extract EIDs.
	 *
	 * @return an optional {@link CanonicalEID} object, representing a resource identifier that matched the given eidSystem.
	 */
	public static List<CanonicalEID> extractFromResource(
			FhirContext theFhirContext, String theEidSystem, IBaseResource theBaseResource) {
		return extractFromResource(
				theFhirContext,
				theEidSystem == null ? Collections.emptyList() : Collections.singletonList(theEidSystem),
				theBaseResource);
	}

	/**
	 * A Factory method to generate {@link CanonicalEID} objects from an incoming resource, matching against
	 * several EID systems at once.
	 *
	 * @param theFhirContext the {@link FhirContext} of the application, used to generate a FHIRPath parser.
	 * @param theEidSystems the enterprise identifier system URIs that identify this resource type.
	 * @param theBaseResource the {@link IBaseResource} from which you would like to extract EIDs.
	 *
	 * @return every resource identifier matching any of the given eidSystems; empty if there are none.
	 */
	public static List<CanonicalEID> extractFromResource(
			FhirContext theFhirContext, Collection<String> theEidSystems, IBaseResource theBaseResource) {
		// A null system matches nothing. It arrives here when a resource type has no configured EID system
		// and a caller pairs a value with the resulting null.
		List<String> eidSystems =
				theEidSystems.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (eidSystems.isEmpty()) {
			return Collections.emptyList();
		}

		IFhirPath fhirPath = theFhirContext.newFhirPath();
		String eidPath = buildEidFhirPath(theFhirContext, eidSystems, theBaseResource);
		List<IBase> evaluate = fhirPath.evaluate(theBaseResource, eidPath, IBase.class);

		return evaluate.stream().map(ibase -> new CanonicalEID(fhirPath, ibase)).collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CanonicalEID)) {
			return false;
		}
		CanonicalEID otherEid = (CanonicalEID) o;
		return Objects.equals(otherEid.getSystem(), this.getSystem())
				&& Objects.equals(otherEid.getValue(), this.getValue())
				&& Objects.equals(otherEid.getUse(), this.getUse());
	}

	@Override
	public int hashCode() {
		return Objects.hash(mySystem, myValue, myUse);
	}
}
