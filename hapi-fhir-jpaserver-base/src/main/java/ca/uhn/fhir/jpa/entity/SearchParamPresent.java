package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "HFJ_RES_PARAM_PRESENT", indexes = {
		@Index(name = "IDX_RESPARMPRESENT_RESID", columnList = "RES_ID")
}, uniqueConstraints = {
		@UniqueConstraint(name = "IDX_RESPARMPRESENT_SPID_RESID", columnNames = { "SP_ID", "RES_ID" })
})
public class SearchParamPresent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_RESPARMPRESENT_ID", sequenceName = "SEQ_RESPARMPRESENT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESPARMPRESENT_ID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "SP_PRESENT", nullable = false)
	private boolean myPresent;

	@ManyToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RESPARMPRES_RESID"))
	private ResourceTable myResource;

	@ManyToOne()
	@JoinColumn(name = "SP_ID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name = "FK_RESPARMPRES_SPID"))
	private SearchParam mySearchParam;

	public ResourceTable getResource() {
		return myResource;
	}

	public SearchParam getSearchParam() {
		return mySearchParam;
	}

	public boolean isPresent() {
		return myPresent;
	}

	public void setPresent(boolean thePresent) {
		myPresent = thePresent;
	}

	public void setResource(ResourceTable theResourceTable) {
		myResource = theResourceTable;
	}

	public void setSearchParam(SearchParam theSearchParam) {
		mySearchParam = theSearchParam;
	}

}
