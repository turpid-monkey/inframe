package org.mism.inframe.core.casts;

/**
 * Casts from one type to another.
 * @author Ray Shark
 *
 * @param <T> target type
 * @param <S> source type
 */
public interface Cast<T,S> {
	
	Class<T> to();
	Class<S> from();
	T cast (S src);

}
