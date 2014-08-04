package org.mism.inframe.core;

/**
 * Represents the interface to a subunit of the compiler. Non-API interface.
 * 
 * @param <T>
 * @param <R>
 */
interface PartHandler<T,R> {
	
	/**
	 * @return type that is handled by this subcompiler.
	 */
	Class<T> getHandledType();
	
	/**
	 * 
	 * @return type that is returned by the created Executable instances.
	 */
	Class<R> getReturnType();
	
	/**
	 * Whether this subcompiler accepts objects of this type.
	 * 
	 * @param o
	 * @return true, if it does.
	 */
	boolean accepts(T o);
	
	/**
	 * Creates an executable that represents the tree branch of the passed object.
	 * 
	 * @param o tree branch
	 * @param host main compiler
	 * @return executable code
	 * @throws Exception
	 */
	Executable createExecutable(T o, Compiler host) throws Exception;

}
