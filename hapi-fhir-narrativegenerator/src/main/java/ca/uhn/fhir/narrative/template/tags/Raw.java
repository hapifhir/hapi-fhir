package ca.uhn.fhir.narrative.template.tags;

import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.LNode;

class Raw extends Tag {

    /*
     * temporarily disable tag processing to avoid syntax conflicts.
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {
        return nodes[0].render(context);
    }
}
