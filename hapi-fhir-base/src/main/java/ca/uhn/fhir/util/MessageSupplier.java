package ca.uhn.fhir.util;

import java.util.function.Supplier;

/**
 * This is used to allow lazy parameter initialization for SLF4j - Hopefully
 * a future version will allow lambda params
 */
public class MessageSupplier {
    private Supplier<?> supplier;

    public MessageSupplier(Supplier<?> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return supplier.get().toString();
    }

    public static MessageSupplier msg(Supplier<?> supplier) {
        return new MessageSupplier(supplier);
    }
}
