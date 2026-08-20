package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A generated TypeScript interface representing a FHIR resource, complex datatype or backbone element.
 * The interface is emitted to a file named {@code <name>.ts} (e.g. {@code Patient.ts}).
 */
public class TsInterface {

	private final String myName;
	private final List<TsProperty> myProperties = new ArrayList<>();
	private String myExtendsName;

	public TsInterface(String theName) {
		myName = theName;
	}

	public String getName() {
		return myName;
	}

	/**
	 * The TypeScript interface name, e.g. {@code Patient}. Also used as the generated file's base name.
	 */
	public String getInterfaceName() {
		return myName;
	}

	/**
	 * Sets the bare name of the base interface this one extends (e.g. {@code "DomainResource"}), which is
	 * used verbatim in both the rendered {@code extends} clause and the generated import.
	 */
	public void setExtendsName(String theExtendsName) {
		myExtendsName = theExtendsName;
	}

	public String getExtendsName() {
		return myExtendsName;
	}

	/**
	 * The {@code extends} clause to splice into the interface declaration: either an empty string or
	 * {@code " extends Foo"}.
	 */
	public String getExtendsClause() {
		if (myExtendsName == null) {
			return "";
		}
		return " extends " + myExtendsName;
	}

	public List<TsProperty> getProperties() {
		return myProperties;
	}

	public void addProperty(TsProperty theProperty) {
		myProperties.add(theProperty);
	}

	/**
	 * Computes the set of {@code import} statements required by this interface, derived purely from the
	 * kinds and names of the properties' types. Self-references are omitted.
	 *
	 * @return a sorted list of fully formed TypeScript import lines
	 */
	public List<String> getImports() {
		Set<String> imports = new TreeSet<>();
		if (myExtendsName != null) {
			imports.add("import { " + myExtendsName + " } from './" + myExtendsName + "';");
		}
		for (TsProperty next : myProperties) {
			switch (next.getKind()) {
				case INTERFACE:
				case ENUM:
					String type = next.getTypeName();
					if (!type.equals(getInterfaceName())) {
						imports.add("import { " + type + " } from './" + type + "';");
					}
					break;
				case PRIMITIVE:
				default:
					break;
			}
		}
		return new ArrayList<>(imports);
	}
}
