package ca.uhn.fhir.jpa.model.pkspike;

import jakarta.annotation.Nonnull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class EntityFixture<R extends EntityFixture.IRootEntity,J extends EntityFixture.IJoinEntity> {

	public static <R extends EntityFixture.IRootEntity,J extends EntityFixture.IJoinEntity> EntityFixture<R,J> build(Class<R> theRootType, Class<J> theJoinType) {
		return new EntityFixture<>(theRootType, theJoinType);
	}

	EntityFixture(Class<R> theRootType, Class<J> theJoinType) {
		myRootType = theRootType;
		myJoinType = theJoinType;
	}

	public J buildJoinEntity() {
		return buildInstance(myJoinType);
	}

	public interface IRootEntity<J> {
		Long getResId();
		void setPartitionId(Integer thePartitionId);
		Integer getPartitionId();
		String getString();
		void setString(String theString);

		Collection<J> getJoins();
	}
	public interface IJoinEntity<P extends IRootEntity> {
		void setString(String theString);

		void setParent(P theRoot);

		String getString();

		void setPartitionId(Integer thePartitionId);
		Integer getPartitionId();

		Long getResId();
	}

	public final Class<R> myRootType;
	public final Class<J> myJoinType;

	public R buildRootEntity() {
		return buildInstance(myRootType);
	}

	static <T> @Nonnull T buildInstance(Class<T> theClass)  {
		try {
			return theClass.getDeclaredConstructor().newInstance();
		} catch (Exception theE) {
			throw new RuntimeException(theE);
		}
	}
}
