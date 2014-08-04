package org.mism.inframe.core;

/**
 * Will be passed to custom sub-interpreters.
 * 
 * @author Ray Shark
 *
 */
public interface InterpreterContext {
	
	/**
	 * Allows the interpreter to traverse the object graph during compile time, binds
	 * the variable of the sub-interpreter to the given child element.
	 * 
	 * @param variable should be a member variable of the InterpreterPart implementation.
	 * @param subLeaf
	 * @throws InterpreterException
	 */
	void bind(String variable, Object subLeaf) throws InterpreterException;
	
	/**
	 * TODO. Idea is to ask the interpreter whether a given child element is constant, so as 
	 * to perform compile time optimizations.
	 * 
	 * @param subLeaf
	 * @return
	 */
	boolean isConstant(Object subLeaf);
	
	/**
	 * If a child element is constant, it can be computed during compile time.
	 * 
	 * @param subLeaf
	 * @return
	 * @throws InterpreterException
	 */
	Object preEvaluate(Object subLeaf) throws InterpreterException;

}
