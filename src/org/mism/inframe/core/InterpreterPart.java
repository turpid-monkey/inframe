package org.mism.inframe.core;

/**
 * Implement this interface to create a sub-interpreter for every leaf of your object graph.
 * 
 * @author Ray Shark
 *
 * @param <TargetType> type that is handled by this sub-interpreter
 * @param <ReturnType> result type
 */
public interface InterpreterPart<TargetType, ReturnType> {
	
	/**
	 * Method will be called from interpreter during compilation. Use the context
	 * object to register all relevant child elements of the passed object, and to map
	 * them to literal types. 
	 * 
	 * @param obj
	 * @param ctx
	 * @throws InterpreterException
	 */
	void bind(TargetType obj, InterpreterContext ctx) throws InterpreterException;
	
	/**
	 * Perform a computation.
	 * @return
	 */
	ReturnType execute();
	
}
