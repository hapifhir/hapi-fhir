/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.fhirpath;

import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;
import java.util.Optional;

public interface IFhirPath {

	/**
	 * Apply the given FhirPath expression against the given input and return
	 * all results in a list
	 *
	 * @param theInput The input object (generally a resource or datatype)
	 * @param thePath The fluent path expression
	 * @param theReturnType The type to return (in order to avoid casting)
	 */
	<T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType);

	/**
	 * Apply the given FhirPath expression against the given input and return
	 * all results in a list. Unlike the {@link #evaluate(IBase, String, Class)} method which
	 * uses a String containing a FHIRPath expression, this method takes a parsed FHIRPath
	 * expression returned by the {@link #parse(String)} method. This has the advantage
	 * of avoiding re-parsing expressions if the same expression will be evaluated
	 * repeatedly.
	 *
	 * @param theInput            The input object (generally a resource or datatype)
	 * @param theParsedExpression A parsed FHIRPath expression returned by {@link #parse(String)}
	 * @param theReturnType       The type to return (in order to avoid casting)
	 * @since 6.8.0
	 */
	<T extends IBase> List<T> evaluate(IBase theInput, IParsedExpression theParsedExpression, Class<T> theReturnType);

	/**
	 * Apply the given FhirPath expression against the given input and return
	 * the first match (if any)
	 *
	 * @param theInput      The input object (generally a resource or datatype)
	 * @param thePath       The fluent path expression
	 * @param theReturnType The type to return (in order to avoid casting)
	 */
	<T extends IBase> Optional<T> evaluateFirst(IBase theInput, String thePath, Class<T> theReturnType);

	/**
	 * Apply the given FhirPath expression against the given input and return
	 * the first match (if any). Unlike the {@link #evaluateFirst(IBase, String, Class)} method which
	 * uses a String containing a FHIRPath expression, this method takes a parsed FHIRPath
	 * expression returned by the {@link #parse(String)} method. This has the advantage
	 * of avoiding re-parsing expressions if the same expression will be evaluated
	 * repeatedly.
	 *
	 * @param theInput            The input object (generally a resource or datatype)
	 * @param theParsedExpression A parsed FHIRPath expression returned by {@link #parse(String)}
	 * @param theReturnType       The type to return (in order to avoid casting)
	 * @since 6.8.0
	 */
	<T extends IBase> Optional<T> evaluateFirst(
			IBase theInput, IParsedExpression theParsedExpression, Class<T> theReturnType);

	/**
	 * Parses the expression and throws an exception if it can not parse correctly.
	 * Note that the return type from this method is intended to be a "black box". It can
	 * be passed back into the {@link #evaluate(IBase, IParsedExpression, Class)}
	 * method on any FHIRPath instance that comes from the same {@link ca.uhn.fhir.context.FhirContext}
	 * instance. Any other use will produce unspecified results.
	 */
	IParsedExpression parse(String theExpression) throws Exception;

	/**
	 * This method can be used optionally to supply an evaluation context for the
	 * FHIRPath evaluator instance. The context can be used to supply data needed by
	 * specific functions, e.g. allowing the <code>resolve()</code> function to
	 * fetch referenced resources.
	 *
	 * @since 6.4.0
	 */
	void setEvaluationContext(@Nonnull IFhirPathEvaluationContext theEvaluationContext);

	/**
	 * This interface is a marker interface representing a parsed FHIRPath expression.
	 * Instances of this class will be returned by {@link #parse(String)} and can be
	 * passed to {@link #evaluate(IBase, IParsedExpression, Class)} and
	 * {@link #evaluateFirst(IBase, IParsedExpression, Class)}. Using a pre-parsed
	 * FHIRPath expression can perform much faster in some situations where an
	 * identical expression will be evaluated many times against different targets,
	 * since the parsing step doesn't need to be repeated.
	 * <p>
	 * Instances of this interface should be treated as a "black box". There are no
	 * methods that can be used to manipulate parsed FHIRPath expressions.
	 * </p>
	 *
	 * @since 6.8.0
	 */
	interface IParsedExpression {
		// no methods
	}
}
