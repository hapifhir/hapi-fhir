package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_HISTORY_TAG")
public class ResourceHistoryTag extends BaseTag implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private Long myId;
	
	@ManyToOne()
	@JoinColumns(value= {
			@JoinColumn(name="RES_TYPE", referencedColumnName="RES_TYPE"),
			@JoinColumn(name="PID", referencedColumnName="PID"),
			@JoinColumn(name="VERSION", referencedColumnName="VERSION")
	}/*, foreignKey=@ForeignKey(name="FK_HT_RT")*/)
	private ResourceHistoryTable myResourceHistory;

	public ResourceHistoryTag() {
	}
	

	public ResourceHistoryTag(ResourceHistoryTable theResourceHistoryTable, TagDefinition theTag) {
		myResourceHistory=theResourceHistoryTable;
		setTag(theTag);
	}

	public ResourceHistoryTable getResourceHistory() {
		return myResourceHistory;
	}

	public void setResource(ResourceHistoryTable theResourceHistory) {
		myResourceHistory = theResourceHistory;
	}

}
