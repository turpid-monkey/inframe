package org.mism.inframe.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.mism.inframe.core.casts.Cast;
import org.mism.inframe.core.casts.CastFactory;
import org.mism.inframe.core.casts.IntegerToDoubleCast;

class CommonExecutableContext implements InterpreterContext, Executable {

	final static CastFactory casts = new CastFactory(new IntegerToDoubleCast());

	interface Binding {
		void bind() throws Exception;
	}
	/**
	 * Several cargo cult finals with the vague hope the JIT compiler removes
	 * the reflection code.
	 */
	final class FieldBinding implements Binding {

		final Executable source;
		final Field target;
		final Cast cast;

		public FieldBinding(Field target, Executable source) throws Exception {
			this.source = source;
			this.target = target;
			target.setAccessible(true);
			cast = casts.find(target.getType(), source.getReturnType());
		}

		public void bind() throws Exception {
			target.set(hosted, cast.cast(source.execute()));
		}
	}
	
	final class ArrayBinding implements Binding {
		final List<Executable> source;
		final Field target;
		final Cast cast;
		final Object array;
		
		public ArrayBinding(Field target, List<Executable> execs) throws Exception
		{
			this.target = target;
			this.source = execs;
			this.array = Array.newInstance(target.getType().getComponentType(), execs.size());
			cast = casts.find(target.getType().getComponentType(), execs.get(0).getReturnType());
			target.set(hosted, array);
		}
		
		public void bind() throws Exception
		{
			for (int i = 0; i < source.size(); i++)
			{
				Array.set(array, i, cast.cast(source.get(i).execute()));
			}
		}
	}

	List<Binding> bindings = new ArrayList<Binding>();
	final InterpreterPart hosted;
	PartHandler host;
	Compiler interpreter;

	public CommonExecutableContext(InterpreterPart hosted, Object obj,
			PartHandler host, Compiler interpreter) throws Exception {
		this.interpreter = interpreter;
		this.hosted = hosted;
		this.host = host;
		hosted.bind(obj, this);
	}

	@Override
	public Object execute() throws Exception {
		for (Binding b : bindings) {
			b.bind();
		}
		return hosted.execute();
	}

	@Override
	public Class<?> getReturnType() {
		return host.getReturnType();
	}

	@Override
	public void bind(String variable, Object subLeaf)
			throws InterpreterException {
		try {
			Field field = hosted.getClass().getDeclaredField(variable);
			if (subLeaf.getClass().isArray()) {
				List<Executable> execs = new ArrayList<>();
				int size = Array.getLength(subLeaf);
				for (int i=0; i<size; i++)
				{
					execs.add(interpreter.compile(Array.get(subLeaf, i)));
				}
				bindings.add(new ArrayBinding(field, execs));
			} else {
				Executable exec = interpreter.compile(subLeaf);
				bindings.add(new FieldBinding(field, exec));
			}
		} catch (Exception t) {
			throw new InterpreterException(
					"Initialization of binding to field " + variable
							+ " for class " + host.getHandledType()
							+ " failed.", t);
		}

	}

	@Override
	public boolean isConstant(Object subLeaf) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object preEvaluate(Object subLeaf) throws InterpreterException {
		return hosted.execute();
	}
}
