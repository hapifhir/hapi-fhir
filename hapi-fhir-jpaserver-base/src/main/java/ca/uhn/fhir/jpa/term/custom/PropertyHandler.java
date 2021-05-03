package ca.uhn.fhir.jpa.term.custom;

import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermConceptPropertyTypeEnum;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class PropertyHandler implements IRecordHandler {

	public static final String CODE = "CODE";
	public static final String KEY = "KEY";
	public static final String VALUE = "VALUE";
	public static final String TYPE = "TYPE";
	private final Map<String, List<TermConceptProperty>> myCode2Properties;

	public PropertyHandler(Map<String, List<TermConceptProperty>> theCode2concept) {
		myCode2Properties = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String code = trim(theRecord.get(CODE));
		String key = trim(theRecord.get(KEY));

		if (isNotBlank(code) && isNotBlank(KEY)) {
			String value = trim(theRecord.get(VALUE));
			String type = trim(theRecord.get(TYPE));

			List<TermConceptProperty> conceptProperties = myCode2Properties.get(code);
			if (conceptProperties == null)
				conceptProperties = new ArrayList<>();

			TermConceptProperty conceptProperty = TermLoaderSvcImpl.getOrCreateConceptProperty(myCode2Properties, code, key);
			ValidateUtil.isNotNullOrThrowUnprocessableEntity(conceptProperty, "Concept property %s not found in file", conceptProperty);

			conceptProperty.setKey(key);
			conceptProperty.setValue(value);
			//TODO: check this for different types, other types should be added once TermConceptPropertyTypeEnum contain different types
			conceptProperty.setType(TermConceptPropertyTypeEnum.STRING);
			conceptProperties.add(conceptProperty);
			myCode2Properties.put(code, conceptProperties);
		}
	}
}
