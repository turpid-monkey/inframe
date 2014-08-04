package org.mism.inframe.core;

public interface Executable {
	
	public Object execute() throws Exception;
	
	public Class<?> getReturnType();

}
