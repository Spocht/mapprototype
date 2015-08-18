package dev.spocht.spocht.callbacks;

/**
 * Created by edm on 18.08.15.
 */
public interface LocationCallback<R, T> {
    R operate(T t);
}
