package ca.uhn.fhir.jpa.model.pkspike;

import jakarta.annotation.Nonnull;

import java.util.Collection;

public class EntityFixture<R extends EntityFixture.IRootEntity<J>,J extends EntityFixture.IJoinEntity<R>> {

	private boolean myNullPartitionSupportFlag = true;

	public static <R extends EntityFixture.IRootEntity<J>,J extends EntityFixture.IJoinEntity<R>> EntityFixture<R,J> build(Class<R> theRootType, Class<J> theJoinType) {
		return new EntityFixture<>(theRootType, theJoinType);
	}

	public static <R extends EntityFixture.IRootEntity<J>,J extends EntityFixture.IJoinEntity<R>> EntityFixture<R,J> buildNoNullPartition(Class<R> theRootType, Class<J> theJoinType) {
		EntityFixture<R, J> entityFixture = new EntityFixture<>(theRootType, theJoinType);
		entityFixture.myNullPartitionSupportFlag = false;
		return entityFixture;
	}

	EntityFixture(Class<R> theRootType, Class<J> theJoinType) {
		myRootType = theRootType;
		myJoinType = theJoinType;
	}

	public J buildJoinEntity() {
		return buildInstance(myJoinType);
	}

	public boolean isSupportNullPartitionId() {
		return myNullPartitionSupportFlag;
	}

	public interface IRootEntity<J> {
		Long getResId();
		void setPartitionId(Integer thePartitionId);
		Integer getPartitionId();
		String getString();
		void setString(String theString);

		Collection<J> getJoins();
	}
	public interface IJoinEntity<P> {
		Long getPid();
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
