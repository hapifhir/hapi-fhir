package ca.uhn.fhir.narrative.template.tags;

import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.LNode;

class Comment extends Tag {

    /*
     * Block tag, comments out the text in the block
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {
        return "";
    }
}
