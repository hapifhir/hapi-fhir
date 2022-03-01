package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.jpa.util.JsonDateDeserializer;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public class ReindexRangeChunk implements IModelJson {

	@Nullable
	@JsonProperty("resourceType")
	private String myResourceType;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("start")
	@Nonnull
	private Date myStart;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("end")
	@Nonnull
	private String myEnd;

	@Nullable
	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(@Nullable String theResourceType) {
		myResourceType = theResourceType;
	}

	@Nonnull
	public Date getStart() {
		return myStart;
	}

	public void setStart(@Nonnull Date theStart) {
		myStart = theStart;
	}

	@Nonnull
	public String getEnd() {
		return myEnd;
	}

	public void setEnd(@Nonnull String theEnd) {
		myEnd = theEnd;
	}

}
