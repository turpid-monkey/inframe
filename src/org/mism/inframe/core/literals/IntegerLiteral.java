package org.mism.inframe.core.literals;

import org.mism.inframe.core.InterpreterContext;
import org.mism.inframe.core.InterpreterException;
import org.mism.inframe.core.InterpreterPart;

public class IntegerLiteral implements InterpreterPart<Integer, Integer> {

	Integer value;
	
	@Override
	public void bind(Integer obj, InterpreterContext ctx)
			throws InterpreterException {
		value = obj;
	}

	@Override
	public Integer execute() {
		return value;
	}

}
