package org.mism.inframe.core.casts;

import static org.junit.Assert.*;

import org.junit.Test;

public class CastFactoryTest {

	@Test
	public void testFind() throws Exception {
		CastFactory cf = new CastFactory(new IntegerToDoubleCast());
		Cast<Double, Integer> cast = cf.find(Double.class, Integer.class);

		assertEquals(new Double(6), cast.cast(new Integer(6)));
	}

	@Test
	public void testFindFail() throws Exception {
		CastFactory cf = new CastFactory(new IntegerToDoubleCast());
		try {
			cf.find(String.class, Integer.class);
			fail("Exception not thrown");
		} catch (Exception e) {

		}
	}
	
	@Test
	public void testSelfCast() throws Exception {
		CastFactory cf = new CastFactory(new IntegerToDoubleCast());
		Cast<Integer, Integer> dummy = cf.find(Integer.class, Integer.class);
		Integer a = 5;
		Integer b = dummy.cast(a);
		assertTrue(a == b);
	}

}
