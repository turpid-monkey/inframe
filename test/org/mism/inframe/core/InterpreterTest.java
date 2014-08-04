package org.mism.inframe.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mism.inframe.core.literals.IntegerLiteral;

public class InterpreterTest {

	static class Multiplication {
		private Addition a, b;

		public Multiplication(Addition a, Addition b) {
			this.a = a;
			this.b = b;
		}
	}

	static class MultiplicationWithArray {
		private AdditionWithArray[] values;

		public MultiplicationWithArray(AdditionWithArray... values) {
			this.values = values;
		}
	}

	static class Addition {
		private int a, b;

		Addition(int a, int b) {
			this.a = a;
			this.b = b;
		}

	}

	static class AdditionWithArray {
		private int[] values;

		public AdditionWithArray(int... values) {
			this.values = values;
		}

	}

	public static class AdditionPart implements
			InterpreterPart<Addition, Integer> {

		int a;
		int b;

		@Override
		public void bind(Addition obj, InterpreterContext ctx)
				throws InterpreterException {
			ctx.bind("a", obj.a);
			ctx.bind("b", obj.b);
		}

		@Override
		public Integer execute() {
			return a + b;
		}
	}

	public static class AdditionWithArrayPart implements
			InterpreterPart<AdditionWithArray, Integer> {

		int[] values;

		@Override
		public void bind(AdditionWithArray obj, InterpreterContext ctx)
				throws InterpreterException {
			ctx.bind("values", obj.values);
		}

		@Override
		public Integer execute() {
			int res = 0;
			for (int i : values) {
				res += i;
			}
			return res;
		}
	}

	public static class MultiplicationWithArrayPart implements
			InterpreterPart<MultiplicationWithArray, Double> {

		double[] values;

		@Override
		public void bind(MultiplicationWithArray obj, InterpreterContext ctx)
				throws InterpreterException {
			ctx.bind("values", obj.values);
		}

		@Override
		public Double execute() {
			double res = 1;
			for (double i : values) {
				res *= i;
			}
			return res;
		}
	}

	public static class MultiplicationPart implements
			InterpreterPart<Multiplication, Double> {
		Double a, b;

		@Override
		public void bind(Multiplication obj, InterpreterContext ctx)
				throws InterpreterException {
			ctx.bind("a", obj.a);
			ctx.bind("b", obj.b);

		}

		@Override
		public Double execute() {
			return a * b;
		}

	}

	@Test
	public void testInterpreterSimple() throws Exception {

		Compiler comp = new Interpreter(AdditionPart.class,
				IntegerLiteral.class);
		Addition add = new Addition(1, 2);
		Executable ex = comp.compile(add);
		assertEquals(new Integer(3), (Integer) ex.execute());

	}

	@Test
	public void testInterpreterNested() throws Exception {

		Compiler comp = new Interpreter(AdditionPart.class,
				MultiplicationPart.class, IntegerLiteral.class);
		Multiplication mult = new Multiplication(new Addition(1, 2),
				new Addition(5, 10));

		Executable ex = comp.compile(mult);
		assertEquals(new Double(45), (Double) ex.execute());

	}

	@Test
	public void testInterpreterArrays() throws Exception {

		Compiler comp = new Interpreter(AdditionWithArrayPart.class,
				IntegerLiteral.class);
		AdditionWithArray add = new AdditionWithArray(2, 2, 2, 2, 2);

		Executable ex = comp.compile(add);
		assertEquals(10, ex.execute());

	}
	
	@Test
	public void testInterpreterNestedArrays() throws Exception {

		Compiler comp = new Interpreter(MultiplicationWithArrayPart.class,AdditionWithArrayPart.class,
				IntegerLiteral.class);
		MultiplicationWithArray mult = new MultiplicationWithArray(new AdditionWithArray(2, 2, 2, 2, 2), new AdditionWithArray(2,2,2));

		Executable ex = comp.compile(mult);
		assertEquals(60.0, ex.execute());

	}

}
