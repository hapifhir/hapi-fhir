/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.fhir.jpa.dao.validation;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a custom resource class structure representing a Meal that was
 * prepared by a chef (Patient).
 */
@ResourceDef(name = "Meal", profile = "http://example.org/StructureDefinition/Meal")
public class Meal extends DomainResource {

	public static final Base[] EMPTY_BASE_ARRAY = new Base[0];

	@Child(name = "identifier", min = 0, max = Child.MAX_UNLIMITED)
	private List<Identifier> myIdentifier;
	@Child(name = "name")
	private StringType myName;
	@Child(name = "chef", type = {Patient.class})
	private Reference myChef;

	/**
	 * By convention in HAPI FHIR, the getter for a repeating (List) field should create
	 * the list if it isn't already initialized.
	 */
	public List<Identifier> getIdentifier() {
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<>();
		}
		return myIdentifier;
	}

	/**
	 * Every field annotated with {@link Child @Child} needs a getter
	 */
	public StringType getName() {
		return myName;
	}

	/**
	 * Every non-List field annotated with {@link Child @Child} needs a setter too
	 */
	public void setName(StringType theName) {
		myName = theName;
	}

	public Reference getChef() {
		return myChef;
	}

	public void setChef(Reference theChef) {
		myChef = theChef;
	}

	/**
	 * All resource classes must implement the {@literal copy()} method.
	 */
	@Override
	public DomainResource copy() {
		Meal retVal = new Meal();
		super.copyValues(retVal);
		for (Identifier next : getIdentifier()) {
			retVal.getIdentifier().add(next.copy());
		}
		retVal.myName = myName != null ? myName.copy() : null;
		retVal.myChef = myChef != null ? myChef.copy() : null;
		return retVal;
	}

	@Override
	public String fhirType() {
		return "Meal";
	}

	@Override
	public Base[] getProperty(int theHash, String theName, boolean theCheckValid) throws FHIRException {
		switch (theName) {
			case "identifier":
				return myIdentifier != null ? myIdentifier.toArray(EMPTY_BASE_ARRAY) : EMPTY_BASE_ARRAY;
			case "chef":
				return myChef != null ? new Base[]{myChef} : EMPTY_BASE_ARRAY;
			case "name":
				return myName != null ? new Base[]{myName} : EMPTY_BASE_ARRAY;
			default:
				return super.getProperty(theHash, theName, theCheckValid);
		}
	}

	@Override
	public ResourceType getResourceType() {
		return null;
	}
}
