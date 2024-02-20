/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.api;

import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Call {@link #newBuilder()} to create a new instance of this class.
 */
public class Section {

	private final String myTitle;
	private final String mySectionCode;
	private final String mySectionDisplay;
	private final List<Class<? extends IBaseResource>> myResourceTypes;
	private final String myProfile;
	private final INoInfoGenerator myNoInfoGenerator;

	private final String mySectionSystem;

	private Section(
			String theTitle,
			String theSectionSystem,
			String theSectionCode,
			String theSectionDisplay,
			List<Class<? extends IBaseResource>> theResourceTypes,
			String theProfile,
			INoInfoGenerator theNoInfoGenerator) {
		myTitle = theTitle;
		mySectionSystem = theSectionSystem;
		mySectionCode = theSectionCode;
		mySectionDisplay = theSectionDisplay;
		myResourceTypes = List.copyOf(theResourceTypes);
		myProfile = theProfile;
		myNoInfoGenerator = theNoInfoGenerator;
	}

	@Nullable
	public INoInfoGenerator getNoInfoGenerator() {
		return myNoInfoGenerator;
	}

	public List<Class<? extends IBaseResource>> getResourceTypes() {
		return myResourceTypes;
	}

	public String getProfile() {
		return myProfile;
	}

	public String getTitle() {
		return myTitle;
	}

	public String getSectionSystem() {
		return mySectionSystem;
	}

	public String getSectionCode() {
		return mySectionCode;
	}

	public String getSectionDisplay() {
		return mySectionDisplay;
	}

	@Override
	public boolean equals(Object theO) {
		if (theO instanceof Section) {
			Section o = (Section) theO;
			return StringUtils.equals(myProfile, o.myProfile);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return myProfile.hashCode();
	}

	/**
	 * Create a new empty section builder
	 */
	public static SectionBuilder newBuilder() {
		return new SectionBuilder();
	}

	/**
	 * Create a new section builder which is a clone of an existing section
	 */
	public static SectionBuilder newBuilder(Section theSection) {
		return new SectionBuilder(
				theSection.myTitle,
				theSection.mySectionSystem,
				theSection.mySectionCode,
				theSection.mySectionDisplay,
				theSection.myProfile,
				theSection.myNoInfoGenerator,
				theSection.myResourceTypes);
	}

	public static class SectionBuilder {

		private String myTitle;
		private String mySectionSystem;
		private String mySectionCode;
		private String mySectionDisplay;
		private List<Class<? extends IBaseResource>> myResourceTypes = new ArrayList<>();
		private String myProfile;
		private INoInfoGenerator myNoInfoGenerator;

		private SectionBuilder() {
			super();
		}

		public SectionBuilder(
				String theTitle,
				String theSectionSystem,
				String theSectionCode,
				String theSectionDisplay,
				String theProfile,
				INoInfoGenerator theNoInfoGenerator,
				List<Class<? extends IBaseResource>> theResourceTypes) {
			myTitle = theTitle;
			mySectionSystem = theSectionSystem;
			mySectionCode = theSectionCode;
			mySectionDisplay = theSectionDisplay;
			myNoInfoGenerator = theNoInfoGenerator;
			myProfile = theProfile;
			myResourceTypes = new ArrayList<>(theResourceTypes);
		}

		public SectionBuilder withTitle(String theTitle) {
			Validate.notBlank(theTitle);
			myTitle = theTitle;
			return this;
		}

		public SectionBuilder withSectionSystem(String theSectionSystem) {
			Validate.notBlank(theSectionSystem);
			mySectionSystem = theSectionSystem;
			return this;
		}

		public SectionBuilder withSectionCode(String theSectionCode) {
			Validate.notBlank(theSectionCode);
			mySectionCode = theSectionCode;
			return this;
		}

		public SectionBuilder withSectionDisplay(String theSectionDisplay) {
			Validate.notBlank(theSectionDisplay);
			mySectionDisplay = theSectionDisplay;
			return this;
		}

		/**
		 * This method may be called multiple times if the section will contain multiple resource types
		 */
		public SectionBuilder withResourceType(Class<? extends IBaseResource> theResourceType) {
			Validate.notNull(theResourceType, "theResourceType must not be null");
			Validate.isTrue(!myResourceTypes.contains(theResourceType), "theResourceType has already been added");
			myResourceTypes.add(theResourceType);
			return this;
		}

		public SectionBuilder withProfile(String theProfile) {
			Validate.notBlank(theProfile);
			myProfile = theProfile;
			return this;
		}

		/**
		 * Supplies a {@link INoInfoGenerator} which is used to create a stub resource
		 * to place in this section if no actual contents are found. This can be
		 * {@literal null} if you do not want any such stub to be included for this
		 * section.
		 */
		@SuppressWarnings("UnusedReturnValue")
		public SectionBuilder withNoInfoGenerator(@Nullable INoInfoGenerator theNoInfoGenerator) {
			myNoInfoGenerator = theNoInfoGenerator;
			return this;
		}

		public Section build() {
			Validate.notBlank(mySectionSystem, "No section system has been defined for this section");
			Validate.notBlank(mySectionCode, "No section code has been defined for this section");
			Validate.notBlank(mySectionDisplay, "No section display has been defined for this section");

			return new Section(
					myTitle,
					mySectionSystem,
					mySectionCode,
					mySectionDisplay,
					myResourceTypes,
					myProfile,
					myNoInfoGenerator);
		}
	}
}
