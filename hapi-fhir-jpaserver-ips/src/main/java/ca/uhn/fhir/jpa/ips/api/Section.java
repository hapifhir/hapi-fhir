package ca.uhn.fhir.jpa.ips.api;

import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.List;

/**
 * Call {@link #newBuilder()} to create a new instance of this class.
 */
public class Section {

	private final String myTitle;
	private final String mySectionCode;
	private final String mySectionDisplay;
	private final List<String> myResourceTypes;
	private final String myProfile;
	private final INoInfoGenerator myNoInfoGenerator;

	private final String mySectionSystem;

	private Section(
		String theTitle,
		String theSectionSystem,
		String theSectionCode,
		String theSectionDisplay,
		List<String> theResourceTypes,
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

	public List<String> getResourceTypes() {
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
	 *
	 * @return
	 */
	public static SectionBuilder newBuilder() {
		return new SectionBuilder();
	}


	public static class SectionBuilder {

		private String myTitle;
		private String mySectionSystem;
		private String mySectionCode;
		private String mySectionDisplay;
		private List<String> myResourceTypes;
		private String myProfile;
		private INoInfoGenerator myNoInfoGenerator;

		private SectionBuilder() {
			super();
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

		public SectionBuilder withResourceTypes(String... theResourceTypes) {
			Validate.isTrue(theResourceTypes.length > 0);
			myResourceTypes = Arrays.asList(theResourceTypes);
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
