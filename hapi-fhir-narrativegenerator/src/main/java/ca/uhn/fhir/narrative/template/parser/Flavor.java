package ca.uhn.fhir.narrative.template.parser;

public enum Flavor {

    LIQUID("snippets"),
    JEKYLL("_includes");

    public final String snippetsFolderName;

    Flavor(String snippetsFolderName) {
        this.snippetsFolderName = snippetsFolderName;
    }
}
