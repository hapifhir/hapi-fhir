package ca.uhn.fhir.narrative.template.tags;

import ca.uhn.fhir.narrative.template.LValue;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.LNode;

/**
 * Tags are used for the logic in a template.
 */
public abstract class Tag extends LValue {

    public enum Statement {

        BREAK, CONTINUE;

        @Override
        public String toString() {
            return "";
        }
    }

    /**
     * A map holding all tags.
     */
    private static final Map<String, Tag> TAGS = new HashMap<String, Tag>();

    static {
        // Register all standard tags.
        registerTag(new Assign());
        registerTag(new Case());
        registerTag(new Capture());
        registerTag(new Comment());
        registerTag(new Cycle());
        registerTag(new For());
        registerTag(new If());
        registerTag(new Include());
        registerTag(new Raw());
        registerTag(new Tablerow());
        registerTag(new Unless());
    }

    /**
     * The name of this tag.
     */
    public final String name;

    /**
     * Used for all package protected tags in the ca.uhn.fhir.narrative.template.tags-package
     * whose name is their class name lower cased.
     */
    protected Tag() {
        this.name = this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Creates a new instance of a Tag.
     *
     * @param name
     *         the name of the tag.
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Retrieves a filter with a specific name.
     *
     * @param name
     *         the name of the filter to retrieve.
     *
     * @return a filter with a specific name.
     */
    public static Tag getTag(String name) {

        Tag tag = TAGS.get(name);

        if (tag == null) {
            throw new RuntimeException("unknown tag: " + name);
        }

        return tag;
    }

    /**
     * Returns all default tags.
     *
     * @return all default tags.
     */
    public static Map<String, Tag> getTags() {
        return new HashMap<String, Tag>(TAGS);
    }

    /**
     * Registers a new tag.
     *
     * @param tag
     *         the tag to be registered.
     */
    public static void registerTag(Tag tag) {
        TAGS.put(tag.name, tag);
    }

    /**
     * Renders this tag.
     *
     * @param context
     *         the context (variables) with which this
     *         node should be rendered.
     * @param nodes
     *         the nodes of this tag is created with. See
     *         the file `src/grammar/LiquidWalker.g` to see
     *         how each of the tags is created.
     *
     * @return an Object denoting the rendered AST.
     */
    public abstract Object render(Map<String, Object> context, LNode... nodes);
}
