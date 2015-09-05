package dev.spocht.spocht.location;

/**
 * Created by edm on 18.08.15.
 */
public interface LocationCallback<R, T> {
    R operate(T t);
}
