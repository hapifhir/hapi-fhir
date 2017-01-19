package ca.uhn.fhir.narrative.template.nodes;

import java.util.Map;

class AttributeNode implements LNode {

    private LNode key;
    private LNode value;

    public AttributeNode(LNode key, LNode value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Object render(Map<String, Object> context) {

        return new Object[]{
                key.render(context),
                value.render(context)
        };
    }
}
