package ca.uhn.fhir.mdm.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

// TODO: where should this live?
// TODO: what should this contain?
public class MdmLinkWithRevision<T extends IMdmLink<?>> {
	private final T myMdmLink;
	private final EnversRevision myEnversRevision;

	public MdmLinkWithRevision(T theMdmLink, EnversRevision theEnversRevision) {
		myMdmLink = theMdmLink;
		myEnversRevision = theEnversRevision;
	}

	public T getMdmLink() {
		return myMdmLink;
	}

	public EnversRevision getEnversRevision() {
		return myEnversRevision;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		final MdmLinkWithRevision<?> that = (MdmLinkWithRevision<?>) theO;
		return Objects.equals(myMdmLink, that.myMdmLink) && Objects.equals(myEnversRevision, that.myEnversRevision);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myMdmLink, myEnversRevision);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myMdmLink", myMdmLink)
			.append("myEnversRevision", myEnversRevision)
			.toString();
	}

}
