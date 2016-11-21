package ca.uhn.fhir.narrative.template.nodes;

import java.io.File;
import java.util.Map;

import ca.uhn.fhir.narrative.template.parser.Flavor;
import ca.uhn.fhir.narrative.template.tags.Include;
import ca.uhn.fhir.narrative.template.tags.Tag;

class TagNode implements LNode {

    private Tag tag;
    private LNode[] tokens;
    private Flavor flavor;

    public TagNode(String tagName, Tag tag, LNode... tokens) {
        this(tagName, tag, Flavor.LIQUID, tokens);
    }

    public TagNode(String tagName, Tag tag, Flavor flavor, LNode... tokens) {
        if (tag == null) {
            throw new IllegalArgumentException("no tag available named: " + tagName);
        }
        this.tag = tag;
        this.tokens = tokens;
        this.flavor = flavor;
    }

    @Override
    public Object render(Map<String, Object> context) {

        // Check if the INCLUDES_DIRECTORY_KEY has already been set, and if not,
        // set it based on the value in the flavor.
        if (!context.containsKey(Include.INCLUDES_DIRECTORY_KEY)) {
            context.put(Include.INCLUDES_DIRECTORY_KEY, new File(flavor.snippetsFolderName));
        }

        return tag.render(context, tokens);
    }
}
