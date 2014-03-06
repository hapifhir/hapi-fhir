package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Parameter {
    
	private String name;
	private IParamBinder parser;
    private boolean required;
    private Class<?> type;
	public Parameter(){}

    public Parameter(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public Object parse(String theString) throws InternalErrorException {
		return parser.parse(theString);
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @SuppressWarnings("unchecked")
	public void setType(final Class<?> type) {
        this.type = type;
        if (IQueryParameterType.class.isAssignableFrom(type)) {
        	this.parser = new IdentifierParamBinder((Class<? extends IQueryParameterType>) type);
        } else {
        	throw new ConfigurationException("Unsupported data type for parameter: " + type.getCanonicalName());
        }
    }


	
	private final class IdentifierParamBinder implements IParamBinder {
		private final Class<? extends IQueryParameterType> myType;

		private IdentifierParamBinder(Class<? extends IQueryParameterType> theType) {
			myType = theType;
		}

		@Override
		public Object parse(String theString) throws InternalErrorException {
			IQueryParameterType dt;
			try {
				dt = myType.newInstance();
				dt.setValueAsQueryToken(theString);
			} catch (InstantiationException e) {
				throw new InternalErrorException(e);
			} catch (IllegalAccessException e) {
				throw new InternalErrorException(e);
			} catch (SecurityException e) {
				throw new InternalErrorException(e);
			}
			return dt;
		}

		@Override
		public String encode(Object theString) throws InternalErrorException {
			return ((IQueryParameterType)theString).getValueAsQueryToken();
		}
	}

	

	private interface IParamBinder
	{
		Object parse(String theString) throws InternalErrorException;
		
		String encode(Object theString) throws InternalErrorException;

	}



	public String encode(Object theObject) throws InternalErrorException {
		return parser.encode(theObject);
	}
	
}
