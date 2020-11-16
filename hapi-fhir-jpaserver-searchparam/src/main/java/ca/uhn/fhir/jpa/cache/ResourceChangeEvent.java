package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An immutable list of resource ids that have been changed, updated, or deleted.
 */
public class ResourceChangeEvent {
	private final List<IdDt> myCreatedResourceIds;
	private final List<IdDt> myUpdatedResourceIds;
	private final List<IdDt> myDeletedResourceIds;

	private ResourceChangeEvent(Collection<IIdType> theCreatedResourceIds, Collection<IIdType> theUpdatedResourceIds, Collection<IIdType> theDeletedResourceIds) {
		myCreatedResourceIds = copyFrom(theCreatedResourceIds);
		myUpdatedResourceIds = copyFrom(theUpdatedResourceIds);
		myDeletedResourceIds = copyFrom(theDeletedResourceIds);
	}

	public static ResourceChangeEvent fromCreatedResourceIds(Collection<IIdType> theCreatedResourceIds) {
		return new ResourceChangeEvent(theCreatedResourceIds, Collections.emptyList(), Collections.emptyList());
	}

	public static ResourceChangeEvent fromCreatedUpdatedDeletedResourceIds(List<IIdType> theCreatedResourceIds, List<IIdType> theUpdatedResourceIds, List<IIdType> theDeletedResourceIds) {
		return new ResourceChangeEvent(theCreatedResourceIds, theUpdatedResourceIds, theDeletedResourceIds);
	}

	private List<IdDt> copyFrom(Collection<IIdType> theResourceIds) {
		ArrayList<IdDt> retval = new ArrayList<>();
		theResourceIds.forEach(id -> retval.add(new IdDt(id)));
		return Collections.unmodifiableList(retval);
	}

	public List<IdDt> getCreatedResourceIds() {
		return myCreatedResourceIds;
	}

	public List<IdDt> getUpdatedResourceIds() {
		return myUpdatedResourceIds;
	}

	public List<IdDt> getDeletedResourceIds() {
		return myDeletedResourceIds;
	}

	public boolean isEmpty() {
		return myCreatedResourceIds.isEmpty() && myUpdatedResourceIds.isEmpty() && myDeletedResourceIds.isEmpty();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myCreatedResourceIds", myCreatedResourceIds)
			.append("myUpdatedResourceIds", myUpdatedResourceIds)
			.append("myDeletedResourceIds", myDeletedResourceIds)
			.toString();
	}
}
