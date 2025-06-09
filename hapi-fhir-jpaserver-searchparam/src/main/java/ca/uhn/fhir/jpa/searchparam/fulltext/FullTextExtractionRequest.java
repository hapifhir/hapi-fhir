package ca.uhn.fhir.jpa.searchparam.fulltext;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * This is a request object containing a request to extract the FullText index data from
 * a resource during storage.
 *
 * @see ca.uhn.fhir.interceptor.api.Pointcut#JPA_INDEX_EXTRACT_FULLTEXT for a description of how this should be used.
 * @since 8.4.0
 */
public class FullTextExtractionRequest {

	@Nullable
	private final IIdType myResourceId;
	@Nullable
	private final IBaseResource myResource;
	private final String myResourceType;
	private final IndexTypeEnum myIndexType;

	/**
	 * Constructor
	 */
	public FullTextExtractionRequest(IndexTypeEnum theIndexType, @Nullable IIdType theResourceId, @Nullable IBaseResource theResource, String theResourceType, Supplier<String> theDefaultSupplier) {
		myIndexType = theIndexType;
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
	 * @return Returns the ID of the resource being indexed. This may be <code>null</code> if a new resource is being created, and a type isn't assigned yet
	 */
	// FIXME: can this actually be null?
	@Nullable
	public IIdType getResourceId() {
		return myResourceId;
	}

	/**
	 * @return Returns the resource being indexed. May be <code>null</code> if the operation is a resource deletion.
	 */
	@Nullable
	public IBaseResource getResource() {
		return myResource;
	}

	/**
	 * @return Returns the resource type being indexed.
	 */
	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * @return Returns the type of index being generated. For any resource being stored, registered intercepors will be invoked once for each index type.
	 */
	public IndexTypeEnum getIndexType() {
		return myIndexType;
	}

	public enum IndexTypeEnum {

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
