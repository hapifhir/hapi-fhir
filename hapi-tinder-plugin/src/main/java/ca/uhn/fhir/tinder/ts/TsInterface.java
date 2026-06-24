package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A generated TypeScript interface representing a FHIR resource, complex datatype or backbone element.
 * The interface is emitted to a file named {@code I<name>.ts} (e.g. {@code IPatient.ts}).
 */
public class TsInterface {

	private final String myName;
	private final List<TsProperty> myProperties = new ArrayList<>();

	public TsInterface(String theName) {
		myName = theName;
	}

	public String getName() {
		return myName;
	}

	/**
	 * The TypeScript interface name including the conventional "I" prefix, e.g. {@code IPatient}.
	 */
	public String getInterfaceName() {
		return "I" + myName;
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
		Set<String> lines = new TreeSet<>();
		for (TsProperty next : myProperties) {
			switch (next.getKind()) {
				case INTERFACE:
					String iface = "I" + next.getTypeName();
					if (!iface.equals(getInterfaceName())) {
						lines.add("import { " + iface + " } from './" + iface + "';");
					}
					break;
				case ENUM:
					lines.add("import { " + next.getTypeName() + " } from './" + next.getTypeName() + "';");
					break;
				case PRIMITIVE:
				default:
					break;
			}
		}
		return new ArrayList<>(lines);
	}
}
