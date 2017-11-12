package ca.uhn.fhir.jpa.cqf.ruler.cds;

import ca.uhn.fhir.context.FhirContext;
import com.google.gson.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 5/4/2017.
 */
public class CdsCard {

    private String summary;
    private String detail;
    private String indicator;

    public boolean hasSummary() {
        return this.summary != null && !this.summary.isEmpty();
    }
    public String getSummary() {
        return this.summary;
    }
    public CdsCard setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public boolean hasDetail() {
        return this.detail != null && !this.detail.isEmpty();
    }
    public String getDetail() {
        return this.detail;
    }
    public CdsCard setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public boolean hasIndicator() {
        return this.indicator != null && !this.indicator.isEmpty();
    }
    public String getIndicator() {
        return this.detail;
    }
    public CdsCard setIndicator(String indicator) {
        this.indicator = indicator;
        return this;
    }


    private Source source;
    public static class Source {
        private String label;
        private String url;

        public boolean hasLabel() {
            return this.label != null && !this.label.isEmpty();
        }
        public String getLabel() {
            return this.label;
        }
        public CdsCard.Source setLabel(String label) {
            this.label = label;
            return this;
        }

        public boolean hasUrl() {
            return this.url != null && !this.url.isEmpty();
        }
        public String getUrl() {
            return this.url;
        }
        public CdsCard.Source setUrl(String url) {
            this.url = url;
            return this;
        }
    }
    public boolean hasSource() {
        return  source.hasLabel() || source.hasUrl();
    }
    public Source getSource() {
        return this.source;
    }
    public CdsCard setSource(Source source) {
        this.source = source;
        return this;
    }


    private List<Suggestions> suggestions;
    public static class Suggestions {
        private String label;
        private String uuid;
        private List<Action> actions;

        public boolean hasLabel() {
            return this.label != null && !this.label.isEmpty();
        }
        public String getLabel() {
            return this.label;
        }
        public CdsCard.Suggestions setLabel(String label) {
            this.label = label;
            return this;
        }

        public boolean hasUuid() {
            return this.uuid != null && !this.uuid.isEmpty();
        }
        public String getUuid() {
            return this.uuid;
        }
        public CdsCard.Suggestions setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public boolean hasActions() {
            return this.actions != null && !this.actions.isEmpty();
        }
        public List<Action> getActions() {
            return this.actions;
        }
        public CdsCard.Suggestions setActions(List<Action> actions) {
            this.actions = actions;
            return this;
        }

        public static class Action {
            enum ActionType {create, update, delete}

            private ActionType type;
            private String description;
            private IBaseResource resource;

            public boolean hasType() {
                return this.type != null;
            }
            public ActionType getType() {
                return this.type;
            }
            public Action setType(ActionType type) {
                this.type = type;
                return this;
            }

            public boolean hasDescription() {
                return this.description != null && !this.description.isEmpty();
            }
            public String getDescription() {
                return this.description;
            }
            public Action setDescription(String description) {
                this.description = description;
                return this;
            }

            public boolean hasResource() {
                return this.resource != null;
            }
            public IBaseResource getResource() {
                return this.resource;
            }
            public Action setResource(IBaseResource resource) {
                this.resource = resource;
                return this;
            }
        }
    }
    public boolean hasSuggestions() {
        return this.suggestions != null && !this.suggestions.isEmpty();
    }
    public List<Suggestions> getSuggestions() {
        return this.suggestions;
    }
    public CdsCard setSuggestions(List<Suggestions> suggestions) {
        this.suggestions = suggestions;
        return this;
    }


    private List<Links> links;
    public static class Links {
        private String label;
        private String url;
        private String type;

        public boolean hasLabel() {
            return this.label != null && !this.label.isEmpty();
        }
        public String getLabel() {
            return this.label;
        }
        public CdsCard.Links setLabel(String label) {
            this.label = label;
            return this;
        }

        public boolean hasUrl() {
            return this.url != null && !this.url.isEmpty();
        }
        public String getUrl() {
            return this.url;
        }
        public CdsCard.Links setUrl(String url) {
            this.url = url;
            return this;
        }

        public boolean hasType() {
            return this.type != null && !this.type.isEmpty();
        }
        public String getType() {
            return this.type;
        }
        public CdsCard.Links setType(String type) {
            this.type = type;
            return this;
        }
    }
    public boolean hasLinks() {
        return this.links != null && !this.links.isEmpty();
    }
    public List<Links> getLinks() {
        return this.links;
    }
    public CdsCard setLinks(List<Links> links) {
        this.links = links;
        return this;
    }


    public CdsCard() {
        this.source = new Source();
        this.suggestions = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    public JsonObject toJson() {
        JsonObject card = new JsonObject();
        if (hasSummary()) {
            card.addProperty("summary", summary);
        }

        if (hasIndicator()) {
            card.addProperty("indicator", indicator);
        }

        if (hasDetail()) {
            card.addProperty("detail", detail);
        }

        // TODO: Source


        if (hasSuggestions()) {
            JsonArray suggestionArray = new JsonArray();
            for (Suggestions suggestion : getSuggestions()) {
                JsonObject suggestionObj = new JsonObject();
                if (suggestion.hasLabel()) {
                    suggestionObj.addProperty("label", suggestion.getLabel());
                }
                if (suggestion.hasUuid()) {
                    suggestionObj.addProperty("uuid", suggestion.getUuid());
                }
                if (suggestion.hasActions()) {
                    JsonArray actionArray = new JsonArray();
                    for (Suggestions.Action action : suggestion.getActions()) {
                        JsonObject actionObj = new JsonObject();
                        if (action.hasDescription()) {
                            actionObj.addProperty("description", action.getDescription());
                        }
                        if (action.hasType()) {
                            actionObj.addProperty("type", action.getType().toString());
                        }
                        if (action.hasResource()) {
                            JsonElement res = new JsonParser().parse(FhirContext.forDstu3().newJsonParser().setPrettyPrint(true).encodeResourceToString(action.getResource()));
                            actionObj.add("resource", res);
                        }
                        actionArray.add(actionObj);
                    }
                    suggestionObj.add("actions", actionArray);
                }
                suggestionArray.add(suggestionObj);
            }
            card.add("suggestions", suggestionArray);
        }

        if (hasLinks()) {
            JsonArray linksArray = new JsonArray();
            for (Links linkElement : getLinks()) {
                JsonObject link = new JsonObject();
                if (linkElement.hasLabel()) {
                    link.addProperty("label", linkElement.getLabel());
                }

                if (linkElement.hasUrl()) {
                    link.addProperty("url", linkElement.getUrl());
                }

                if (linkElement.hasType()) {
                    link.addProperty("type", linkElement.getType());
                }
                linksArray.add(link);
            }
            card.add("links", linksArray);
        }
        return card;
    }

//    public JSONObject toJson() {
//        JSONObject card = new JSONObject();
//        if (hasSummary()) {
//            card.put("summary", summary);
//        }
//        if (hasIndicator()) {
//            card.put("indicator", indicator);
//        }
//        if (hasDetail()) {
//            card.put("detail", detail);
//        }
//        // TODO: Source & Suggestions
//        if (hasLinks()) {
//            JSONArray linksArray = new JSONArray();
//            for (Links linkElement : getLinks()) {
//                JSONObject link = new JSONObject();
//                if (linkElement.hasLabel()) {
//                    link.put("label", linkElement.getLabel());
//                }
//                if (linkElement.hasUrl()) {
//                    link.put("url", linkElement.getUrl());
//                }
//                if (linkElement.hasType()) {
//                    link.put("type", linkElement.getType());
//                }
//                linksArray.add(link);
//            }
//            card.put("links", linksArray);
//        }
//        return card;
//    }

    public JSONObject returnSuccess() {
        JSONObject success = new JSONObject();
        success.put("summary", "Success");
        success.put("indicator", "success");
        success.put("detail", "The MME is within the recommended range.");
        return success;
    }
}
