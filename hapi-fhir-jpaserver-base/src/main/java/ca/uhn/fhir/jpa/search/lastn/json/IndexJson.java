package ca.uhn.fhir.jpa.search.lastn.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class IndexJson {

    @JsonProperty(value = "index", required = true)
    private IdJson myIndex;

    public IndexJson(IdJson theIndex) {
        myIndex = theIndex;
    }

    public IdJson getId() { return myIndex; }

}
