package ca.uhn.fhir.narrative.template.nodes;

import java.util.Map;

public class AtomNode implements LNode {

    public static final AtomNode EMPTY = new AtomNode(new Object());

    private Object value;

    public AtomNode(Object value) {
        this.value = value;
    }

    public static boolean isEmpty(Object o) {
        return o == EMPTY.value;
    }

    @Override
    public Object render(Map<String, Object> context) {

        return value;
    }
}
