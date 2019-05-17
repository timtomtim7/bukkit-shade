package blue.sparse.bshade.command.util;

public interface TriPredicate<A, B, C> {
	boolean test(A a, B b, C c);
}
