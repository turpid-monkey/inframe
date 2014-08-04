package org.mism.inframe.core.casts;

import java.util.HashMap;
import java.util.Map;

/**
 * Source for casts between literal types.
 */
public class CastFactory {

	/**
	 * The simplest cast is no cast.
	 */
	@SuppressWarnings("rawtypes")
	static final class SelfCast implements Cast {
		@Override
		public Class<?> to() {
			return null;
		}

		@Override
		public Class<?> from() {
			return null;
		}

		@Override
		public Object cast(Object src) {
			return src;
		}

	}

	final SelfCast dummy = new SelfCast();
	final Map<Class<?>, Map<Class<?>, Cast<?, ?>>> castMap;

	/**
	 * Initialize a new cast factory.
	 *  
	 * @param casts Casts that are available from this instance.
	 */
	public CastFactory(Cast<?, ?>... casts) {
		this.castMap = initMap(casts);
	}

	private static Map<Class<?>, Map<Class<?>, Cast<?, ?>>> initMap(
			Cast<?, ?>[] casts) {
		Map<Class<?>, Map<Class<?>, Cast<?, ?>>> map = new HashMap<>();
		for (Cast<?, ?> cast : casts) {
			if (!map.containsKey(cast.from())) {
				map.put(cast.from(), new HashMap<Class<?>, Cast<?, ?>>());
			}
			map.get(cast.from()).put(cast.to(), cast);
		}
		return map;
	}

	/**
	 * In order to use the JVMs magic primitive to complex type handling, mask
	 * the primitives as complex types.
	 * 
	 * @param cls
	 * @return
	 */
	private static Class<?> maskPrimitive(Class<?> cls) {
		if (!cls.isPrimitive())
			return cls;
		String name = cls.getName();

		switch (name) {
		case "int":
			return Integer.class;
		case "long":
			return Long.class;
		case "double":
			return Double.class;
		case "float":
			return Float.class;
		case "short":
			return Short.class;
		case "boolean":
			return Boolean.class;
		case "byte":
			return Byte.class;
		case "char":
			return Character.class;

		}
		throw new AssertionError(
				"Error in above switch statement, not all primitive types covered");
	}

	@SuppressWarnings("unchecked")
	public <T, S> Cast<T, S> find(Class<T> to, Class<S> src) throws Exception {
		to = (Class<T>) maskPrimitive(to);
		src = (Class<S>) maskPrimitive(src);
		if (to.equals(src))
			return dummy;
		if (castMap.containsKey(src)) {
			if (castMap.get(src).containsKey(to)) {
				return (Cast<T, S>) castMap.get(src).get(to);
			}
		}
		throw new Exception("No cast from " + src.getName() + " to "
				+ to.getName() + " found.");

	}
	
	/**
	 * We do not singleton.
	 */
	public static CastFactory getInstance()
	{
		throw new RuntimeException("Dude, get your own instance.");
	}

}
