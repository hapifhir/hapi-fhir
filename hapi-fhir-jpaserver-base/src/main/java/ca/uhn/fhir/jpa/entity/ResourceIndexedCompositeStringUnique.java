package ca.uhn.fhir.jpa.entity;

import org.apache.commons.lang3.builder.*;
import org.hl7.fhir.r4.model.Resource;

import javax.persistence.*;

@Entity()
@Table(name = "HFJ_IDX_CMP_STRING_UNIQ", indexes = {
	@Index(name = ResourceIndexedCompositeStringUnique.IDX_IDXCMPSTRUNIQ_STRING, columnList = "IDX_STRING", unique = true)
})
public class ResourceIndexedCompositeStringUnique implements Comparable<ResourceIndexedCompositeStringUnique> {

	public static final int MAX_STRING_LENGTH = 800;
	public static final String IDX_IDXCMPSTRUNIQ_STRING = "IDX_IDXCMPSTRUNIQ_STRING";

	@SequenceGenerator(name = "SEQ_IDXCMPSTRUNIQ_ID", sequenceName = "SEQ_IDXCMPSTRUNIQ_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IDXCMPSTRUNIQ_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID")
	private ResourceTable myResource;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("resourceId", myResourceId)
			.append("indexString", myIndexString)
			.toString();
	}

	@Column(name="RES_ID", insertable = false, updatable = false)
	private Long myResourceId;

	@Column(name = "IDX_STRING", nullable = false, length = MAX_STRING_LENGTH)
	private String myIndexString;

	/**
	 * Constructor
	 */
	public ResourceIndexedCompositeStringUnique() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedCompositeStringUnique(ResourceTable theResource, String theIndexString) {
		setResource(theResource);
		setIndexString(theIndexString);
	}

	@Override
	public int compareTo(ResourceIndexedCompositeStringUnique theO) {
		CompareToBuilder b = new CompareToBuilder();
		b.append(myResource, theO.getResource());
		b.append(myIndexString, theO.getIndexString());
		return b.toComparison();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		ResourceIndexedCompositeStringUnique that = (ResourceIndexedCompositeStringUnique) theO;

		return new EqualsBuilder()
			.append(getResource(), that.getResource())
			.append(myIndexString, that.myIndexString)
			.isEquals();
	}

	public String getIndexString() {
		return myIndexString;
	}

	public void setIndexString(String theIndexString) {
		myIndexString = theIndexString;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getResource())
			.append(myIndexString)
			.toHashCode();
	}
}
