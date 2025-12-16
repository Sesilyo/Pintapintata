package main;

/**
 * Implements the Logistic function (Sigmoid's function).
 * The Logistic function is defined as:
 * 
 * f(x) = L / {1 + [e ^ -k(x - x0)]},
 * 
 * where:
 * 		e  : Euler's number
 * 		L  : maximum value
 * 		k  : growth rate
 * 		x0 : x value of the midPoint
 * 
 * This calls can be used to model S-shaped curves often found
 * in animation easing.
 */
public class LogisticFunction
{
	double maxVal;			 // L
	double growthRate;		 // k
	double midPoint;		 // x0
	
	
	/**
	 * Constructor.
	 * 
	 * @param maxVal	 - L,  maximum y value
	 * @param growthRate - k,  steepness of the curve
	 * @param midPoint	 - x0, x value at which growth is fastest
	 */
	public LogisticFunction(double maxVal, double growthRate, double midPoint)
	{
		this.maxVal = maxVal;
		this.growthRate = growthRate;
		this.midPoint   = midPoint;
	}
	
	
	/**
	 * Calculates the output (y-value) of the logistic function for a given input-x.
	 * 
	 * @param x
	 * @return
	 */
	public double logiFunc(double x)
	{
		double y;				// the output
		double exponentTerm;	// the exponential term in the divisor/denominator
		double denominator;
		
		exponentTerm = - (growthRate) * (x - midPoint);
		// Math.exp(x) is equivalent to e ^ x
		// e ^ -k(x - x0)
		denominator = Math.exp(exponentTerm);
		denominator += 1;
		y = maxVal / denominator;	// L / y
		return y;
	}
	
	
	/**
	 * Returns maximum value (L) used to configure this function instance.
	 * @return upper asymptote value.
	 */
	public double getMaxVal() { return this.maxVal;}
}
