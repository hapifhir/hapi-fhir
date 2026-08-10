/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.FileUtil;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * System-level operation that runs a server-side JavaScript file (via the embedded GraalJS engine)
 * to transform FHIR resources.
 *
 * <p>Callers do <b>not</b> supply the JavaScript itself - they reference one of the scripts the
 * server administrator has placed in the configured scripts directory
 * ({@link JpaStorageSettings#setJavaScriptExecutionScriptsDirectory(String)}). This keeps the set of
 * executable code fixed and vetted, instead of accepting arbitrary code over the wire.
 *
 * <p>Invoke it as {@code POST [base]/$execute-javascript} with a {@code Parameters} body carrying a
 * {@code script} (the file name, with or without the {@code .js} suffix), any number of inline
 * {@code resource} parameters, and any number of {@code reference} parameters - literal
 * references the server reads before the script runs, e.g.
 *
 * <pre>{@code
 * {
 *   "resourceType": "Parameters",
 *   "parameter": [
 *     { "name": "script", "valueString": "merge-patients" },
 *     { "name": "resource", "resource": { "resourceType": "Patient", "id": "1" } },
 *     { "name": "reference", "valueReference": { "reference": "Patient/2" } }
 *   ]
 * }
 * }</pre>
 *
 * <p>Inside the script:
 * <ul>
 *   <li>{@code input} is a JavaScript array of the input resources (parsed JSON objects): the
 *       inline {@code resource} parameters first, followed by the resources resolved from the
 *       {@code reference} parameters, in declared order - possibly empty if none were sent.</li>
 *   <li>The value the script evaluates to is collected as output. It may be a single resource
 *       object or an array of resource objects. Each element is parsed back into a FHIR resource
 *       and returned under a {@code return} parameter of the response {@code Parameters}.</li>
 * </ul>
 *
 * <p><b>Security:</b> this operation is unavailable unless a scripts directory has been configured.
 * The {@code script} parameter is only ever resolved to a file inside the configured scripts
 * directory - the name is validated to a bare file name and the resolved path is verified to
 * stay within that directory, so callers cannot traverse the filesystem or execute code that the
 * administrator has not installed. As defense-in-depth, each script runs in a fresh GraalJS
 * {@link Context} created with no host access - Java class lookup, filesystem, network, and
 * thread creation are all denied by default - so scripts are limited to pure JavaScript and
 * cannot reach the host JVM, filesystem or network. Each invocation is also bounded by an execution
 * timeout ({@link JpaStorageSettings#setJavaScriptExecutionTimeoutSeconds(long)}, default 30s); a
 * script that overruns has its context canceled and the call fails.
 *
 * <p>Note that this provider is not wired into the JPA server by default - deployments which want
 * to offer the operation need to add the {@code org.graalvm.js:js} dependency (declared as optional
 * by this module), construct this provider, and register it against their
 * {@link ca.uhn.fhir.rest.server.RestfulServer}.
 *
 * @since 8.14.0
 */
public class JavaScriptExecutionProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(JavaScriptExecutionProvider.class);

	/** A script name is a bare file name (optionally ending in {@code .js}) - never a path. */
	private static final Pattern SCRIPT_NAME_PATTERN = Pattern.compile("[A-Za-z0-9_-]+(\\.js)?");

	/**
	 * One engine shared by all evaluations: contexts created on a shared engine skip per-request
	 * engine/language initialization and share the parsed-source cache. Isolation is unaffected -
	 * each request still runs in its own fresh {@link Context} with no host access.
	 */
	private static final Engine ourEngine =
			Engine.newBuilder().option("engine.WarnInterpreterOnly", "false").build();

	private static final Source ourParseInputSource = Source.create("js", "var input = JSON.parse(__inputJson);");
	private static final Source ourStringifySource = Source.create("js", "JSON.stringify");

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final JpaStorageSettings myStorageSettings;

	private final ExecutorService myExecutor = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
			.setNameFormat("js-exec-%d")
			.setDaemon(true)
			.build());

	/**
	 * Constructor
	 */
	public JavaScriptExecutionProvider(
			FhirContext theFhirContext, DaoRegistry theDaoRegistry, JpaStorageSettings theStorageSettings) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myStorageSettings = theStorageSettings;
	}

	@Description(
			value =
					"This operation runs a named, administrator-vetted server-side JavaScript file against the supplied resources and returns the transformed resources.",
			shortDefinition = "Runs a vetted server-side JavaScript transformation over FHIR resources")
	@Operation(name = ProviderConstants.OPERATION_EXECUTE_JAVASCRIPT, idempotent = true)
	public IBaseParameters executeJavascript(
			@Description("The name of the script to execute, with or without the '.js' suffix")
					@OperationParam(name = "script", typeName = "string", min = 1, max = 1)
					IPrimitiveType<String> theScriptName,
			@Description("Inline resources to pass to the script as input")
					@OperationParam(name = "resource", min = 0, max = OperationParam.MAX_UNLIMITED)
					List<IBaseResource> theInputResources,
			@Description("Literal references (e.g. Patient/123) resolved by the server before the script runs")
					@OperationParam(
							name = "reference",
							typeName = "reference",
							min = 0,
							max = OperationParam.MAX_UNLIMITED)
					List<IBaseReference> theReferences,
			RequestDetails theRequestDetails) {

		String scriptName = theScriptName != null ? theScriptName.getValue() : null;
		ValidateUtil.isNotBlankOrThrowInvalidRequest(
				scriptName, "A non-empty 'script' parameter (the script name) is required.");

		String script = loadScript(scriptName);

		// 'input' = inline 'resource' parameters first, then the resources resolved from the literal
		// 'reference' parameters (read from the server before the script runs), in declared order.
		List<IBaseResource> inputs = new ArrayList<>();
		if (theInputResources != null) {
			inputs.addAll(theInputResources);
		}
		if (theReferences != null) {
			for (IBaseReference reference : theReferences) {
				inputs.add(resolveReference(reference, theRequestDetails));
			}
		}

		List<String> outputJsons = runScript(script, serializeInput(inputs));

		IBaseParameters response = ParametersUtil.newInstance(myFhirContext);
		for (IBaseResource resource : parseOutputResources(outputJsons)) {
			ParametersUtil.addParameterToParameters(myFhirContext, response, "return", resource);
		}
		return response;
	}

	/** Reads the resource named by a literal reference (e.g. {@code Patient/123}) from the server. */
	private IBaseResource resolveReference(IBaseReference theReference, RequestDetails theRequestDetails) {
		IIdType id = theReference.getReferenceElement();
		if (!id.hasResourceType() || !id.hasIdPart()) {
			throw new InvalidRequestException(
					Msg.code(6560) + "Each 'reference' parameter must contain a literal reference such as"
							+ " 'Patient/123'; got: " + id.getValue());
		}
		return myDaoRegistry.getResourceDao(id.getResourceType()).read(id, theRequestDetails);
	}

	/**
	 * Resolves a caller-supplied script name to a file inside the configured scripts directory,
	 * rejecting anything that is not a plain name within that directory.
	 */
	private String loadScript(String theScriptName) {
		String scriptsDirectory = myStorageSettings.getJavaScriptExecutionScriptsDirectory();
		if (isBlank(scriptsDirectory)) {
			throw new InvalidRequestException(Msg.code(6562)
					+ "Server-side script execution is not configured (no scripts directory has been set).");
		}
		Path scriptsDir = Paths.get(scriptsDirectory).toAbsolutePath().normalize();

		if (!SCRIPT_NAME_PATTERN.matcher(theScriptName).matches()) {
			throw new InvalidRequestException(Msg.code(6563) + "Invalid script name: " + theScriptName);
		}

		String fileName = theScriptName.endsWith(".js") ? theScriptName : theScriptName + ".js";
		Path scriptPath = scriptsDir.resolve(fileName).normalize();
		// Defense-in-depth: even after name validation, confirm we never escaped the base directory.
		if (!scriptPath.startsWith(scriptsDir)) {
			throw new InvalidRequestException(Msg.code(6564) + "Invalid script name: " + theScriptName);
		}
		if (!Files.isRegularFile(scriptPath)) {
			throw new InvalidRequestException(Msg.code(6565) + "Unknown script: " + theScriptName);
		}

		return FileUtil.loadFileAsString(scriptPath.toFile());
	}

	/** Serializes the input resources into a single JavaScript-parseable JSON array string. */
	private String serializeInput(List<IBaseResource> theInputResources) {
		IParser parser = myFhirContext.newJsonParser();
		return theInputResources.stream()
				.map(parser::encodeResourceToString)
				.collect(Collectors.joining(",", "[", "]"));
	}

	/**
	 * Runs the script on a pooled daemon thread and enforces the configured timeout. If the script
	 * overruns, the GraalJS context is cancelled from the request thread - even a tight CPU-bound
	 * loop is unwound cleanly, freeing the worker. Each context is fresh and isolated and the
	 * sandbox denies all host access, so a cancelled script holds no shared state.
	 *
	 * @return the script output as one JSON string per returned resource
	 */
	private List<String> runScript(String theScript, String theInputJson) {
		long timeoutSeconds = myStorageSettings.getJavaScriptExecutionTimeoutSeconds();

		// No host access: scripts cannot reach Java classes, the filesystem, the network or threads.
		Context context = Context.newBuilder("js").engine(ourEngine).build();
		try {
			Future<List<String>> future = myExecutor.submit(() -> evaluate(context, theScript, theInputJson));
			return future.get(timeoutSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalErrorException(Msg.code(6567) + "Interrupted while waiting for script to complete.", e);
		} catch (TimeoutException e) {
			throw new InvalidRequestException(
					Msg.code(6568) + "JavaScript execution timed out after " + timeoutSeconds + " seconds.");
		} catch (ExecutionException e) {
			// A PolyglotException covers syntax/runtime JS errors as well as the sandbox blocking host
			// access (e.g. an undefined 'Java'). Either way it is caller error -> 400, not 500.
			ourLog.debug("JavaScript execution failed", e.getCause());
			throw new InvalidRequestException(
					Msg.code(6569) + "JavaScript execution failed: "
							+ e.getCause().getMessage(),
					e.getCause());
		} finally {
			// Cancels any in-flight evaluation (unblocking the worker) and releases the context.
			context.close(true);
		}
	}

	/**
	 * Evaluates the script in the given fresh, sandboxed GraalJS context and returns the result as
	 * one JSON string per resource. Runs on the worker thread - all {@link Value} access must
	 * happen here, while the context is alive.
	 */
	private List<String> evaluate(Context theContext, String theScript, String theInputJson) {
		theContext.getBindings("js").putMember("__inputJson", theInputJson);
		theContext.eval(ourParseInputSource);

		Value result = theContext.eval("js", theScript);
		if (result == null || result.isNull()) {
			return List.of();
		}

		Value stringify = theContext.eval(ourStringifySource);
		List<String> outputJsons = new ArrayList<>();
		if (result.hasArrayElements()) {
			for (long i = 0; i < result.getArraySize(); i++) {
				outputJsons.add(stringify.execute(result.getArrayElement(i)).asString());
			}
		} else {
			outputJsons.add(stringify.execute(result).asString());
		}
		return outputJsons;
	}

	/** Parses the script's per-resource JSON output back into FHIR resources. */
	private List<IBaseResource> parseOutputResources(List<String> theOutputJsons) {
		IParser parser = myFhirContext.newJsonParser();
		List<IBaseResource> resources = new ArrayList<>();
		for (String outputJson : theOutputJsons) {
			try {
				resources.add(parser.parseResource(outputJson));
			} catch (Exception e) {
				throw new InvalidRequestException(
						Msg.code(6572) + "Script must return FHIR resource object(s); could not parse script"
								+ " result as a FHIR resource: " + e.getMessage(),
						e);
			}
		}
		return resources;
	}
}
