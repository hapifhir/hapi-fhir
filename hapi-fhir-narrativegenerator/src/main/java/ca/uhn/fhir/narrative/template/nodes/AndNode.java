package ca.uhn.fhir.narrative.template.nodes;

import ca.uhn.fhir.narrative.template.LValue;

import java.util.Map;

class AndNode extends LValue implements LNode {

    private LNode lhs;
    private LNode rhs;

    public AndNode(LNode lhs, LNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object render(Map<String, Object> context) {

        Object a = lhs.render(context);
        Object b = rhs.render(context);

        return super.asBoolean(a) && super.asBoolean(b);

    }
}
