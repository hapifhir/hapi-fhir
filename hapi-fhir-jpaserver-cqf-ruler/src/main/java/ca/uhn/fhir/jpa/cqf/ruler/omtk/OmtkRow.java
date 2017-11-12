package ca.uhn.fhir.jpa.cqf.ruler.omtk;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bryn on 4/24/2017.
 */
public class OmtkRow {

    private Map<String, Object> data = new HashMap<String, Object>();

    public Object getValue(String key) {
        return data.get(key);
    }

    public void setValue(String key, Object value) {
        data.put(key, value);
    }

}
