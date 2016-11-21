package ca.uhn.fhir.narrative.template.tags;

import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.LNode;

class Capture extends Tag {

    /*
     * Block tag that captures text into a variable
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {

        String id = super.asString(nodes[0].render(context));

        LNode block = nodes[1];

        context.put(id, block.render(context));

        return null;
    }
}
