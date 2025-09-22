package ca.uhn.fhir.util.monad;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Either3Test {

    @Test
    void equals() {
        var first = Eithers.forLeft3(1);
        var second = Eithers.forLeft3(1);
        assertEquals(first, second);
        assertEquals(second, first);

        var third = Eithers.forRight3(2);
        var fourth = Eithers.forRight3(2);
        assertEquals(third, fourth);
        assertEquals(fourth, third);

        var fifth = Eithers.forLeft3(1);
        var sixth = Eithers.forRight3(1);
        assertNotEquals(fifth, sixth);
        assertNotEquals(sixth, fifth);

        var seventh = Eithers.forMiddle3(2);
        var eighth = Eithers.forMiddle3(2);
        assertEquals(seventh, eighth);
        assertEquals(eighth, seventh);

        assertNotEquals(first, seventh);
        assertNotEquals(third, seventh);
    }

    @Test
    void properties() {
        var one = Eithers.forLeft3(1);
        assertTrue(one.isLeft());
        assertFalse(one.isMiddle());
        assertFalse(one.isRight());
        assertEquals(1, one.leftOrThrow());
        // Either is right-biased, meaning the "get" is for the right element
        assertThrows(IllegalStateException.class, one::rightOrThrow);
        assertThrows(IllegalStateException.class, one::middleOrThrow);
        assertThrows(IllegalStateException.class, one::getOrThrow);

        var two = Eithers.forRight3(1);
        assertFalse(two.isLeft());
        assertFalse(two.isMiddle());
        assertTrue(two.isRight());
        assertEquals(1, two.rightOrThrow());
        assertEquals(1, two.getOrThrow());
        assertThrows(IllegalStateException.class, two::leftOrThrow);
        assertThrows(IllegalStateException.class, two::middleOrThrow);

        var three = Eithers.forMiddle3(1);
        assertFalse(three.isLeft());
        assertTrue(three.isMiddle());
        assertFalse(three.isRight());
        assertEquals(1, three.middleOrThrow());
        assertThrows(IllegalStateException.class, three::leftOrThrow);
        assertThrows(IllegalStateException.class, three::rightOrThrow);
        assertThrows(IllegalStateException.class, three::getOrThrow);
    }

    @Test
    void swap() {
        var one = Eithers.forLeft3(1);
        var two = Eithers.forRight3(1);
        var three = Eithers.forMiddle3(1);
        assertEquals(one, two.swap());
        assertEquals(two, one.swap());
        assertEquals(three, three.swap());
    }

    // The identity law states that applying the right identity function
    // (Eithers.forRight3) should be equivalent to the original either.
    // In other words, reconstructing an Either from a value
    // should equal the original eithers
    @Test
    void rightIdentity() {
        Integer b = 42;

        Either3<String, String, Integer> right = Eithers.forRight3(b);

        assertEquals(right.flatMap(Eithers::forRight3), right);
    }

    // The identity law states that applying flatMap to a "left" either should return the
    // original either. In other words, the left value propagates
    @Test
    void leftIdentity() {
        String a = "42";

        Either3<String, String, Integer> left = Eithers.forLeft3(a);

        assertEquals(left.flatMap(x -> Eithers.forRight3(x + 2)), left);
    }

    // The identity law states that applying flatMap to a "middle" either should return the
    // original either. In other words, the middle value propagates
    @Test
    void middleIdentity() {
        String a = "42";

        Either3<String, String, Integer> left = Eithers.forMiddle3(a);

        assertEquals(left.flatMap(x -> Eithers.forRight3(x + 2)), left);
    }

    // This associativity law states that if you chain together two functions using flatMap,
    // the order in which you apply them should not matter.
    @Test
    void associativity() {
        Function<Integer, Either3<String, String, Double>> f = i -> Eithers.forRight3(i / 2.0);
        Function<Double, Either3<String, String, String>> g = d -> Eithers.forRight3("Result: " + d);

        Either3<String, String, Integer> m = Eithers.forRight3(10);

        assertEquals(m.flatMap(f).flatMap(g), m.flatMap(x -> f.apply(x).flatMap(g)));
    }

    // Map is implemented in terms of "unit" (constructors in Java) and "bind" ("flatMap")
    // IOW, bind(m, x -> unit(f(x)))
    @Test
    void map() {
        Function<Integer, Integer> doubleInt = (Integer x) -> x * 2;
        Either3<String, Double, Integer> e = Eithers.forRight3(3);

        var bound = e.flatMap(x -> Eithers.forRight3(doubleInt.apply(x)));
        var map = e.map(doubleInt);

        assertEquals(bound, map);
        assertEquals(6, map.getOrThrow());

        // But we also need to make sure we preserve left identity;
        Either3<String, Double, Integer> failed = Eithers.forLeft3("Failed either");
        var mapFailed = failed.map(doubleInt);

        assertEquals(failed, mapFailed);

        // And middle identity
        Either3<String, Double, Integer> failed2 = Eithers.forMiddle3(1.5);
        var mapFailed2 = failed2.map(doubleInt);

        assertEquals(failed2, mapFailed2);
    }

    // fold is a reduction operation. Take a left, middle, or right and turn it into
    // a single value.
    @Test
    void fold() {
        var right = Eithers.<String, Double, Integer>forRight3(2);
        var middle = Eithers.<String, Double, Integer>forMiddle3(1.5);
        var left = Eithers.<String, Double, Integer>forLeft3("three");

        Function<Integer, String> foldR = Object::toString;
        Function<Double, String> foldM = Object::toString;
        Function<String, String> foldL = x -> x;

        var x = right.fold(foldL, foldM, foldR);
        assertEquals("2", x);

        var y = left.fold(foldL, foldM, foldR);
        assertEquals("three", y);

        var z = middle.fold(foldL, foldM, foldR);
        assertEquals("1.5", z);
    }

    @Test
    void forEach() {
        var left = Eithers.<String, Double, Integer>forLeft3("three");
        var middle = Eithers.<String, Double, Integer>forMiddle3(1.5);
        var right = Eithers.<String, Double, Integer>forRight3(2);

        boolean[] flags = {false, false};

        right.forEach(x -> flags[1] = true);
        assertTrue(flags[1]);

        // Left for each shouldn't do anything
        left.forEach(x -> flags[0] = true);
        assertFalse(flags[0]);

        // Middle for each shouldn't do anything
        middle.forEach(x -> flags[0] = true);
        assertFalse(flags[0]);
    }

    // "stream" gives you the ability to use the standard Java Stream apis if you need to
    // left Eithers return an empty stream. Right Eithers return a stream of one.
    @Test
    void stream() {
        var left = Eithers.forLeft3(1);
        var middle = Eithers.forMiddle3(1);
        var right = Eithers.forRight3(1);

        assertEquals(0, left.stream().count());
        assertEquals(0, middle.stream().count());
        assertEquals(1, right.stream().count());
    }

    // "optional" gives you the ability to use the standard Java Optional apis if you need to
    // left Eithers return an empty optional. Right Eithers return a filled optional
    @Test
    void optional() {
        var left = Eithers.forLeft3(1);
        var middle = Eithers.forLeft3(1);
        var right = Eithers.forRight3(1);

        assertFalse(left.optional().isPresent());
        assertFalse(middle.optional().isPresent());
        assertTrue(right.optional().isPresent());
    }

    @Test
    void rotate() {
        var left = Eithers.forLeft3(1);
        var leftResult = left.rotate();
        assertTrue(leftResult.isMiddle());
        assertEquals(1, leftResult.middleOrThrow());

        var middle = Eithers.forMiddle3(1);
        var middleResult = middle.rotate();
        assertTrue(middleResult.isRight());
        assertEquals(1, middleResult.rightOrThrow());

        var right = Eithers.forRight3(1);
        var rightResult = right.rotate();
        assertTrue(rightResult.isLeft());
        assertEquals(1, rightResult.leftOrThrow());
    }

    @Test
    void orElse() {
        var left = Eithers.forLeft3(1);
        var middle = Eithers.forMiddle3(1);
        var right = Eithers.forRight3(1);

        assertEquals(2, left.orElse(2));
        assertEquals(2, middle.orElse(2));
        assertEquals(1, right.orElse(2));
    }

    @Test
    void orElseGet() {
        var left = Eithers.forLeft3(1);
        var middle = Eithers.forMiddle3(1);
        var right = Eithers.forRight3(1);

        assertEquals(2, left.orElseGet(() -> 2));
        assertEquals(2, middle.orElseGet(() -> 2));
        assertEquals(1, right.orElseGet(() -> 2));
    }

    @Test
    void peek() {
        var left = Eithers.<Integer, Integer, Integer>forLeft3(1);
        var middle = Eithers.<Integer, Integer, Integer>forMiddle3(1);
        var right = Eithers.<Integer, Integer, Integer>forRight3(1);

        var leftResult = new int[1];
        var middleResult = new int[1];
        var rightResult = new int[1];

        left.peek(x -> leftResult[0] = x);
        middle.peek(x -> middleResult[0] = x);
        right.peek(x -> rightResult[0] = x);

        assertEquals(0, leftResult[0]);
        assertEquals(0, middleResult[0]);
        assertEquals(1, rightResult[0]);
    }
}
