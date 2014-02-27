package ca.uhn.fhir.ws;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Parameter {
    private String name;
    private boolean required;
    private Class<?> type;

    public Parameter(){}

    public Parameter(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
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
}
