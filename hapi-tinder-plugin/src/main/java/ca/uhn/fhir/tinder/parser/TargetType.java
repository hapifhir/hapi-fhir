/**
 * 
 */
package ca.uhn.fhir.tinder.parser;

/**
 * @author Bill.Denton
 *
 */
public enum TargetType {
	/*
	 * The primary target of a generator is Java source code
	 * but others might also be generated.
	 */
	SOURCE,
	
	/*
	 * The generator will primarilly produce non-source
	 * files that should be added to Maven Resources
	 */
	RESOURCE
}
