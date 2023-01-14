package ca.uhn.fhir.jpa.ips.api;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class IpsContext {

	private final IBaseResource mySubject;
	private final IIdType mySubjectId;

	public IpsContext(IBaseResource theSubject, IIdType theSubjectId) {
		mySubject = theSubject;
		mySubjectId = theSubjectId;
	}

	/**
	 * Returns the subject Patient resource for the IPS being generated. Note that
	 * the Resource.id value may not match the ID of the resource stored in the
	 * repository if {@link IIpsGenerationStrategy#massageResourceId(IpsContext, IBaseResource)} has
	 * returned a different ID. Use {@link #getSubjectId()} if you want the originally stored ID.
	 *
	 * @see #getSubjectId() for the originally stored ID.
	 */
	public IBaseResource getSubject() {
		return mySubject;
	}

	/**
	 * Returns the ID of the subject for the given IPS. This value should match the
	 * ID which was originally fetched from the repository.
	 */
	public IIdType getSubjectId() {
		return mySubjectId;
	}

	public IpsSectionContext newSectionContext(IpsSectionEnum theSection, String theResourceType) {
		return new IpsSectionContext(mySubject, mySubjectId, theSection, theResourceType);
	}

	public static class IpsSectionContext extends IpsContext {

		private final IpsSectionEnum mySection;
		private final String myResourceType;

		private IpsSectionContext(IBaseResource theSubject, IIdType theSubjectId, IpsSectionEnum theSection, String theResourceType) {
			super(theSubject, theSubjectId);
			mySection = theSection;
			myResourceType = theResourceType;
		}

		public String getResourceType() {
			return myResourceType;
		}

		public IpsSectionEnum getSection() {
			return mySection;
		}
	}

}
