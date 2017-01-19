package ca.uhn.fhir.narrative.template.nodes;

import java.util.Map;

/**
 * Denotes a node in the AST the parse creates from the
 * input source.
 */
public interface LNode {

    /**
     * Renders this AST.
     *
     * @param context
     *         the context (variables) with which this
     *         node should be rendered.
     *
     * @return an Object denoting the rendered AST.
     */
    Object render(Map<String, Object> context);
}
