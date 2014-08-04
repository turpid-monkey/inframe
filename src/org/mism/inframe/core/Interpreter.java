package org.mism.inframe.core;

import java.util.ArrayList;
import java.util.List;

public class Interpreter implements Compiler {

	private List<PartHandler<?, ?>> parts;

	public Interpreter(Class<? extends InterpreterPart>... compilerParts) throws Exception {
		initFrom(compilerParts);
	}

	private void initFrom(
			Class<? extends InterpreterPart>[] compilerParts) throws Exception {
		parts = new ArrayList<PartHandler<?, ?>>();
		for (Class part : compilerParts)
		{
			parts.add(new CommonPartHandler<>(part, this));
		}
	}

	@Override
	public Executable compile(Object obj) throws Exception {
		for (PartHandler part : parts) {
			if (obj.getClass().equals(part.getHandledType())) {
				if (part.accepts(obj)) {
						return part.createExecutable(obj, this);
				}
			}
		}
		throw new InterpreterException("Object type unknown to compiler.");
	}
}
