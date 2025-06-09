package ca.uhn.fhir.jpa.searchparam.fulltext;

import jakarta.validation.constraints.Null;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FullTextExtractionRequest {

	@Nullable
	private final IIdType myResourceId;
	@Nullable
	private final IBaseResource myResource;
	private final String myResourceType;
	private final RequestTypeEnum myRequestType;

	private boolean myOverrideDoNotIndex;
	private String myOverridePayload;
	private String myOverridePayload;

	public FullTextExtractionRequest(RequestTypeEnum theRequestType, @Nullable IIdType theResourceId, @Nullable IBaseResource theResource, String theResourceType, Supplier<String> theDefaultSupplier) {
		myRequestType = theRequestType;
		myResourceId = theResourceId;
		myResource = theResource;
		myResourceType = theResourceType;
	}

	/**
	 * @return Returns {@literal true} if this request represents a resource deletion
	 */
	public boolean isDelete() {
		return myResource == null;
	}

	/**
	 * Interceptors may invoke this method to signal that the given resource should not
	 * be indexed.
	 */
	public void setDoNotIndex() {
		myOverrideDoNotIndex = true;
	}

	public void

	public enum RequestTypeEnum {

		/**
		 * This is a request to index the resource content (i.e. the <code>_content</code> SearchParameter)
		 */
		CONTENT,

		/**
		 * This is a request to index the resource narrative (i.e. the <code>_text</code> SearchParameter)
		 */
		TEXT

	}
}
