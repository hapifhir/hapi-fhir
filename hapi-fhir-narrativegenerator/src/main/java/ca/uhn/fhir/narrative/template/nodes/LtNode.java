package ca.uhn.fhir.narrative.template.nodes;

import ca.uhn.fhir.narrative.template.LValue;

import java.util.Map;

class LtNode extends LValue implements LNode {

    private LNode lhs;
    private LNode rhs;

    public LtNode(LNode lhs, LNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object render(Map<String, Object> context) {

        Object a = lhs.render(context);
        Object b = rhs.render(context);

        return (a instanceof Number) && (b instanceof Number) &&
                super.asNumber(a).doubleValue() < super.asNumber(b).doubleValue();
    }
}
