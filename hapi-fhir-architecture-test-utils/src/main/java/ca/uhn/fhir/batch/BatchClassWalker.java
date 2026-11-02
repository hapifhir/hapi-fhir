package ca.uhn.fhir.batch;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class BatchClassWalker {

	private static final Logger ourLog = LoggerFactory.getLogger(BatchClassWalker.class);

	/**
	 * This is the list of root classes we care about.
	 * If a "new SystemRequestDetails() is created from anything
	 * originating from these classes, then it's something we want to flag.
	 */
	private static final Class<?>[] ROOT_CLASSES =
			new Class[] {IJobStepWorker.class, IReductionStepWorker.class, IJobParametersValidator.class};

	/**
	 * The (default for hapi-fhir) list of classes
	 * where we allow "new SystemRequestDetails" to be created.
	 * Set these in "stopsAt".
	 *
	 * If adding more to this list, comment why.
	 */
	public static final Class<?>[] DEFAULT_CLASSES_TO_OMIT = new Class[] {
		// this is the place our newRequestDetails exists - so we omit it
		IJobStepExecutionServices.class,
		// these are noisy and create systemrequestdetails internally by design
		DaoRegistry.class,
		IFhirResourceDao.class,
		IJobCoordinator.class,
		IResourceChangeListenerCache.class
	};

	private final JavaClasses myClasses;
	private String myRootPackage;
	private final Set<String> myStopTypes = new HashSet<>();

	public BatchClassWalker(JavaClasses theClasses) {
		myClasses = theClasses;
	}

	/**
	 * Packages to narrow the scope by.
	 * We will consider only classes that have their roots in this
	 * package (prefix)
	 */
	public BatchClassWalker rootsIn(String thePackage) {
		myRootPackage = thePackage;
		return this;
	}

	public BatchClassWalker stopAt(Class<?>... theTypes) {
		/* add names to myStopTypes */
		for (Class<?> clazz : theTypes) {
			myStopTypes.add(clazz.getName());
		}
		return this;
	}

	public List<CallChain> findSystemRequestDetailsCreation() {
		List<CallChain> violations = new ArrayList<>();

		JavaClass srd = myClasses.get(SystemRequestDetails.class);

		// get all constructor calls
		List<JavaCall<?>> sites = new ArrayList<>(srd.getConstructorCallsToSelf());

		// walk callers backwards from each site until we hit a batch step
		for (JavaCall<?> site : sites) {
			// already visited + path back to the site
			Map<JavaCodeUnit, JavaCodeUnit> calledBy = new HashMap<>();
			Deque<JavaCodeUnit> queue = new ArrayDeque<>();
			calledBy.put(site.getOrigin(), null);
			queue.add(site.getOrigin());
			while (!queue.isEmpty()) {
				JavaCodeUnit unit = queue.pop();
				if (isRootClass(unit.getOwner())) {

					violations.add(new CallChain(
							pathTo(unit, calledBy), site.getSourceCodeLocation().toString()));
					// one flag per chain is sufficient
					break;
				}

				if (isStopType(unit.getOwner())) {
					// skip
					continue;
				}

				// add all the other callers
				for (JavaCodeUnit caller : callersOf(unit)) {
					if (!calledBy.containsKey(caller)) {
						calledBy.put(caller, unit);
						queue.add(caller);
					}
				}
			}
		}
		return violations;
	}

	private boolean isStopType(JavaClass theClass) {
		return myStopTypes.stream().anyMatch(theClass::isAssignableTo);
	}

	private boolean isRootClass(JavaClass theClass) {
		if (theClass.isInterface()
				|| (myRootPackage != null && !theClass.getPackageName().startsWith(myRootPackage))) {
			return false;
		}
		return Arrays.stream(ROOT_CLASSES).anyMatch(theClass::isAssignableTo);
	}

	private List<String> pathTo(JavaCodeUnit theUnit, Map<JavaCodeUnit, JavaCodeUnit> theCameFrom) {
		LinkedList<String> path = new LinkedList<>();
		for (JavaCodeUnit u = theUnit; u != null; u = theCameFrom.get(u)) {
			path.addFirst(u.getFullName());
		}
		return path;
	}

	/**
	 * Collect callers of a method/code unit.
	 * These will be iterated on for more calls checks.
	 */
	private Set<JavaCodeUnit> callersOf(JavaCodeUnit theUnit) {
		Set<JavaCodeUnit> result = new HashSet<>();
		theUnit.getAccessesToSelf().forEach(a -> result.add(a.getOrigin()));
		if (theUnit instanceof JavaMethod method) {
			String[] params = method.getRawParameterTypes().stream()
					.map(JavaClass::getName)
					.toArray(String[]::new);
			Stream.concat(
							method.getOwner().getAllRawSuperclasses().stream(),
							method.getOwner().getAllRawInterfaces().stream())
					.forEach(sup -> sup.tryGetMethod(method.getName(), params)
							.ifPresent(m -> m.getAccessesToSelf().forEach(a -> result.add(a.getOrigin()))));
		}
		return result;
	}

	public record CallChain(List<String> Methods, String CreationSite) {}
}
