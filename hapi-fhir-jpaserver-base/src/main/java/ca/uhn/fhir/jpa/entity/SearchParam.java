package ca.uhn.fhir.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "HFJ_SEARCH_PARM", uniqueConstraints= {
	@UniqueConstraint(name="IDX_SEARCHPARM_RESTYPE_SPNAME", columnNames= {"RES_TYPE", "PARAM_NAME"})
})
public class SearchParam {

	@Id
	@SequenceGenerator(name = "SEQ_SEARCHPARM_ID", sequenceName = "SEQ_SEARCHPARM_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SEARCHPARM_ID")
	@Column(name = "PID")
	private Long myId;

	@Column(name="PARAM_NAME", length=BaseResourceIndexedSearchParam.MAX_SP_NAME, nullable=false, updatable=false)
	private String myParamName;

	@Column(name="RES_TYPE", length=ResourceTable.RESTYPE_LEN, nullable=false, updatable=false)
	private String myResourceName;

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theParamName) {
		myParamName = theParamName;
	}

	public void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	
}
