package org.mism.inframe.core.casts;

public class IntegerToDoubleCast implements Cast<Double, Integer> {

	@Override
	public Class<Double> to() {
		return Double.class;
	}

	@Override
	public Class<Integer> from() {
		return Integer.class;
	}

	@Override
	public Double cast(Integer src) {
		return src.doubleValue();
	}

}
