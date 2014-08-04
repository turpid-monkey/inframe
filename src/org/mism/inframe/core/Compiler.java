package org.mism.inframe.core;

public interface Compiler {

	public abstract Executable compile(Object obj) throws Exception;

}