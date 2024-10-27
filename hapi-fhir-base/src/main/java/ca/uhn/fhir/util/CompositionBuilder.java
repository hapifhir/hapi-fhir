/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;

/**
 * This class can be used to generate <code>Composition</code> resources in
 * a version independent way.
 *
 * @since 6.4.0
 */
public class CompositionBuilder {

	private final FhirContext myCtx;
	private final IBaseResource myComposition;
	private final RuntimeResourceDefinition myCompositionDef;
	private final FhirTerser myTerser;

	public CompositionBuilder(@Nonnull FhirContext theFhirContext) {
		myCtx = theFhirContext;
		myCompositionDef = myCtx.getResourceDefinition("Composition");
		myTerser = myCtx.newTerser();
		myComposition = myCompositionDef.newInstance();
	}

	@SuppressWarnings("unchecked")
	public <T extends IBaseResource> T getComposition() {
		return (T) myComposition;
	}

	/**
	 * Add a value to <code>Composition.author</code>
	 */
	public void addAuthor(IIdType theAuthorId) {
		IBaseReference reference = myTerser.addElement(myComposition, "Composition.author");
		reference.setReference(theAuthorId.getValue());
	}

	/**
	 * Set a value in <code>Composition.status</code>
	 */
	public void setStatus(String theStatusCode) {
		myTerser.setElement(myComposition, "Composition.status", theStatusCode);
	}

	/**
	 * Set a value in <code>Composition.subject</code>
	 */
	public void setSubject(IIdType theSubject) {
		myTerser.setElement(myComposition, "Composition.subject.reference", theSubject.getValue());
	}

	/**
	 * Add a Coding to <code>Composition.type.coding</code>
	 */
	public void addTypeCoding(String theSystem, String theCode, String theDisplay) {
		IBaseCoding coding = myTerser.addElement(myComposition, "Composition.type.coding");
		coding.setCode(theCode);
		coding.setSystem(theSystem);
		coding.setDisplay(theDisplay);
	}

	/**
	 * Set a value in <code>Composition.date</code>
	 */
	public void setDate(IPrimitiveType<Date> theDate) {
		myTerser.setElement(myComposition, "Composition.date", theDate.getValueAsString());
	}

	/**
	 * Set a value in <code>Composition.title</code>
	 */
	public void setTitle(String theTitle) {
		myTerser.setElement(myComposition, "Composition.title", theTitle);
	}

	/**
	 * Set a value in <code>Composition.confidentiality</code>
	 */
	public void setConfidentiality(String theConfidentiality) {
		myTerser.setElement(myComposition, "Composition.confidentiality", theConfidentiality);
	}

	/**
	 * Set a value in <code>Composition.id</code>
	 */
	public void setId(IIdType theId) {
		myComposition.setId(theId.getValue());
	}

	public SectionBuilder addSection() {
		IBase section = myTerser.addElement(myComposition, "Composition.section");
		return new SectionBuilder(section);
	}

	public class SectionBuilder {

		private final IBase mySection;

		/**
		 * Constructor
		 */
		private SectionBuilder(IBase theSection) {
			mySection = theSection;
		}

		/**
		 * Sets the section title
		 */
		public void setTitle(String theTitle) {
			myTerser.setElement(mySection, "title", theTitle);
		}

		/**
		 * Add a coding to section.code
		 */
		public void addCodeCoding(String theSystem, String theCode, String theDisplay) {
			IBaseCoding coding = myTerser.addElement(mySection, "code.coding");
			coding.setCode(theCode);
			coding.setSystem(theSystem);
			coding.setDisplay(theDisplay);
		}

		/**
		 * Adds a reference to entry.reference
		 */
		public void addEntry(IIdType theReference) {
			IBaseReference entry = myTerser.addElement(mySection, "entry");
			entry.setReference(theReference.getValue());
		}

		/**
		 * Adds narrative text to the section
		 */
		public void setText(String theStatus, String theDivHtml) {
			IBase text = myTerser.addElement(mySection, "text");
			myTerser.setElement(text, "status", theStatus);
			myTerser.setElement(text, "div", theDivHtml);
		}
	}
}
