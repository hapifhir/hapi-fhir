package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Entity
@Table(name = "HFJ_RES_SEARCH_URL", uniqueConstraints = {@UniqueConstraint(name = "IDX_RES_HASH", columnNames = "RES_HASH")
})
public class ResourceSearchUrlEntity {

	@Id
	@SequenceGenerator(name = "SEQ_RESSEARCHURL_ID", sequenceName = "SEQ_RESSEARCHURL_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESSEARCHURL_ID")
	@Column(name = "PID")
	private Long myId;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_RESSEARCHURL_RESID"))
	private ResourceTable myResource;
	@Column(name = "RES_ID", insertable = false, updatable = false, nullable = false)
	private Long myResourcePid;

	@Column(name = "RES_HASH", length = 128, nullable = false)
	private String myHash;

	@Column(name = "RES_SEARCH_URL", nullable = false)
	private String mySearchUrl;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	public static ResourceSearchUrlEntity from(String theUrl, Long theId) {
		return new ResourceSearchUrlEntity()
			.setResourcePid(theId)
			.setSearchUrl(theUrl)
			.setCreatedTime(new Date());
	}

	public Long getId() {
		return myId;
	}

	public ResourceSearchUrlEntity setId(Long theId) {
		myId = theId;
		return this;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public ResourceSearchUrlEntity setResource(ResourceTable theResource) {
		myResource = theResource;
		return this;
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public ResourceSearchUrlEntity setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
		return this;
	}

	public Date getCreatedTime() {
		return myCreatedTime;
	}

	public ResourceSearchUrlEntity setCreatedTime(Date theCreatedTime) {
		myCreatedTime = theCreatedTime;
		return this;
	}

	public String getHash() {
		return myHash;
	}

	public ResourceSearchUrlEntity setHash(String theHash) {
		myHash = theHash;
		return this;
	}

	public String getSearchUrl() {
		return mySearchUrl;
	}

	public ResourceSearchUrlEntity setSearchUrl(String theSearchUrl) {
		mySearchUrl = theSearchUrl;
		return this;
	}
}


