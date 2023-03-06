package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Entity
@Table(name = "HFJ_RES_SEARCH_URL",
   indexes = {@Index(name = "IDX_RESSEARCHURL_ID_TIME", columnList = "RES_ID,CREATED_TIME")},
	uniqueConstraints = {@UniqueConstraint(name = "IDX_RESSEARCHURL_SEARCH_URL", columnNames = "RES_SEARCH_URL")})
public class ResourceSearchUrlEntity {

	public static final int RES_SEARCH_URL_LENGTH = 512;

	@Id
	@SequenceGenerator(name = "SEQ_RESSEARCHURL_ID", sequenceName = "SEQ_RESSEARCHURL_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESSEARCHURL_ID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "RES_ID", updatable = false, nullable = false)
	private Long myResourcePid;

	@Column(name = "RES_SEARCH_URL", length = RES_SEARCH_URL_LENGTH, nullable = false)
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

	public String getSearchUrl() {
		return mySearchUrl;
	}

	public ResourceSearchUrlEntity setSearchUrl(String theSearchUrl) {
		mySearchUrl = theSearchUrl;
		return this;
	}
}


