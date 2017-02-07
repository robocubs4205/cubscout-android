package com.robocubs4205.util;

/**
 * Created by trevor on 2/6/17.
 */
public class Pair<T, U> {
    private final T left;
    private final U right;

    private Pair(T left, U right) {

        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    public T left() {
        return left;
    }

    public U right() {
        return right;
    }

    public <L> Pair<L, U> mapLeft(Function<T, L> func) {
        return new Pair<>(func.apply(left), right);
    }

    public <L> Pair<L, U> mapLeft(Function2<T, U, L> func) {
        return new Pair<>(func.apply(left, right), right);
    }

    public <R> Pair<T, R> mapRight(Function<U, R> func) {
        return new Pair<>(left, func.apply(right));
    }

    public <R> Pair<T, R> mapRight(Function2<T, U, R> func) {
        return new Pair<>(left, func.apply(left, right));
    }

    public <V> V reduce(Function2<T, U, V> func) {
        return func.apply(left, right);
    }
}
