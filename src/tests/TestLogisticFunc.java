package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// = = = TEST FILE = = =
import main.LogisticFunction;

class TestLogisticFunc
{
	private LogisticFunction logiFunc;
	
	@BeforeEach
	public void setUp()
	{
		// create an 
		logiFunc = new LogisticFunction(1, 2, 3);
	}
	
	@Test
	void testLogiFuncMidPoint()
	{
		double result = logiFunc.logiFunc(3);	// function at midpoint
		assertEquals(0.5, result, 0.0001);		// should be (L / 2)
	}
	
	@Test
	void testLogiFunction_X()
	{
		double result = logiFunc.logiFunc(100);
		assertTrue(result > 0.99 && result <= 1.0);
	}

	@Test
	void testLogiFunction_SubX()
	{
		double result = logiFunc.logiFunc(-100);
		assertTrue(result < 0.01 && result >= 0);
	}
}
