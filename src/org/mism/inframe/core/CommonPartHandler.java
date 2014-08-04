package org.mism.inframe.core;

import java.lang.reflect.Method;

class CommonPartHandler<TargetType, ReturnType> implements
		PartHandler<TargetType, ReturnType> {

	Class<? extends InterpreterPart<TargetType, ReturnType>> cls;
	Class<ReturnType> returnType;
	Class<TargetType> targetType;
	Compiler host;

	@SuppressWarnings("unchecked")
	public CommonPartHandler(
			Class<? extends InterpreterPart<TargetType, ReturnType>> cls,
			Compiler host) throws Exception {
		this.cls = cls;
		for (Method m : cls.getMethods()) {
			if (m.getName().equals("execute")) {
				returnType = (Class<ReturnType>) cls.getMethod("execute")
						.getReturnType();
			} else if (m.getName().equals("bind")
					&& !m.getParameterTypes()[0].equals(Object.class)) {

				targetType = (Class<TargetType>) m.getParameterTypes()[0];
			}
			if (returnType!=null && targetType!=null) break;
		}
	}

	@Override
	public Class<TargetType> getHandledType() {
		return targetType;
	}

	@Override
	public Class<ReturnType> getReturnType() {
		return returnType;
	}

	@Override
	public boolean accepts(TargetType o) {
		// TODO allow for several Handlers per handled type
		return true;
	}

	@Override
	public Executable createExecutable(TargetType o, Compiler host)
			throws Exception {
		return new CommonExecutableContext(cls.newInstance(), o, this, host);
	}

}
