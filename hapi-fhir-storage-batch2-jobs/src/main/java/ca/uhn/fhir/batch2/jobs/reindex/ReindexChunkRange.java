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

public class ReindexChunkRange implements IModelJson {

	@Nullable
	@JsonProperty("resourceType")
	private String myUrl;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("start")
	@Nonnull
	private Date myStart;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("end")
	@Nonnull
	private Date myEnd;

	@Nullable
	public String getUrl() {
		return myUrl;
	}

	public void setUrl(@Nullable String theUrl) {
		myUrl = theUrl;
	}

	@Nonnull
	public Date getStart() {
		return myStart;
	}

	public void setStart(@Nonnull Date theStart) {
		myStart = theStart;
	}

	@Nonnull
	public Date getEnd() {
		return myEnd;
	}

	public void setEnd(@Nonnull Date theEnd) {
		myEnd = theEnd;
	}

}
