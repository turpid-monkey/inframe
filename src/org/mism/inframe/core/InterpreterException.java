package org.mism.inframe.core;

/**
 * Interpreter-specific exception type.
 * 
 */
public class InterpreterException extends Exception {

	private static final long serialVersionUID = -7945495202009884777L;

	public InterpreterException(String string) {
		super(string);
	}

	public InterpreterException(String string, Exception e) {
		super(string, e);
	}

}
