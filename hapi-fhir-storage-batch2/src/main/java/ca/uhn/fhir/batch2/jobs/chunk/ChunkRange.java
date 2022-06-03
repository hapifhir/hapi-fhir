package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.jpa.util.JsonDateDeserializer;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nonnull;
import java.util.Date;

public class ChunkRange implements IModelJson {
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

	@Nonnull
	public Date getStart() {
		return myStart;
	}

	public ChunkRange setStart(@Nonnull Date theStart) {
		myStart = theStart;
		return this;
	}

	@Nonnull
	public Date getEnd() {
		return myEnd;
	}

	public ChunkRange setEnd(@Nonnull Date theEnd) {
		myEnd = theEnd;
		return this;
	}
}
