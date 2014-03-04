package ca.uhn.fhir.ws;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.ws.exceptions.InternalErrorException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Parameter {
    private String name;
    private boolean required;
    private Class<?> type;
	private IParser parser;

    public Parameter(){}

    public Parameter(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(final Class<?> type) {
        this.type = type;
        if (IdentifierDt.class.isAssignableFrom(type)) {
        	this.parser = new IParser() {
				@Override
				public Object parse(String theString) throws InternalErrorException {
					IdentifierDt dt;
					try {
						dt = (IdentifierDt) type.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new InternalErrorException(e);
					}
					dt.setValueAsQueryToken(theString);
					return dt;
				}
			};
        } else {
        	throw new ConfigurationException("Unsupported data type for parameter: " + type.getCanonicalName());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

	public Object parse(String theString) throws InternalErrorException {
		return parser.parse(theString);
	}
	
	private interface IParser
	{
		Object parse(String theString) throws InternalErrorException;
	}
	
}
