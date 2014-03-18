package ca.uhn.fhir.rest.param;

import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchParameter implements IParameter {

	private String name;
	private IParamBinder parser;
	private boolean required;
	private Class<?> type;

	public SearchParameter() {
	}

	public SearchParameter(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.rest.param.IParameter#encode(java.lang.Object)
	 */
	@Override
	public List<List<String>> encode(Object theObject) throws InternalErrorException {
		return parser.encode(theObject);
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.rest.param.IParameter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.rest.param.IParameter#parse(java.util.List)
	 */
	@Override
	public Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException {
		return parser.parse(theString);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@SuppressWarnings("unchecked")
	public void setType(final Class<?> type, Class<? extends Collection<?>> theInnerCollectionType, Class<? extends Collection<?>> theOuterCollectionType) {
		this.type = type;
		if (IQueryParameterType.class.isAssignableFrom(type)) {
			this.parser = new QueryParameterTypeBinder((Class<? extends IQueryParameterType>) type);
		} else if (IQueryParameterOr.class.isAssignableFrom(type)) {
			this.parser = new QueryParameterOrBinder((Class<? extends IQueryParameterOr>) type);
		} else {
			throw new ConfigurationException("Unsupported data type for parameter: " + type.getCanonicalName());
		}

//		if (theInnerCollectionType != null) {
//			this.parser = new CollectionBinder(this.parser, theInnerCollectionType);
//		}
//
//		if (theOuterCollectionType != null) {
//			this.parser = new CollectionBinder(this.parser, theOuterCollectionType);
//		}

	}

}
