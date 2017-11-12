package ca.uhn.fhir.jpa.cqf.ruler.omtk;

import org.opencds.cqf.cql.runtime.Code;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Bryn on 4/24/2017.
 */
public class OmtkDataWrapper implements Iterable<Object> {

    // TODO: Lifecycle management.... assuming GC for now
    private ResultSet rs;

    public OmtkDataWrapper(ResultSet rs) {
        if (rs == null) {
            throw new IllegalArgumentException("rs is null");
        }

        this.rs = rs;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Object> iterator() {
        return new OmtkDataWrapperIterator();
    }

    private class OmtkDataWrapperIterator implements Iterator<Object> {
        public OmtkDataWrapperIterator() {
            try {
                hasNext = rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        private boolean hasNext = false;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return hasNext;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Object next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }

            Object result = getRowData();
            try {
                hasNext = rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            return result;
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private Object getRowData() {
            try {
                java.sql.ResultSetMetaData metadata = rs.getMetaData();

                OmtkRow row = new OmtkRow();

                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    if (metadata.getColumnName(i).endsWith("_RXCUI")) {
                        row.setValue(metadata.getColumnName(i), new Code().withCode(((Integer)rs.getInt(i)).toString())
                                .withSystem(OmtkDataProvider.RXNORM));
                    }
                    row.setValue(metadata.getColumnName(i), rs.getObject(i));
                }

                return row;
            }
            catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}

