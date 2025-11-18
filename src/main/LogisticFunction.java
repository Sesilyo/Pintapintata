/*
 * [] File_name:   LogisticFunction.java
 * [] Description: - The logistic function and its logic
 * []			     are stored here.
 * []			   - The purposes of this is to solve for the acceleration
 * []				 and the maintenance of speed after acceleration of the
 * []				 brush/pen when it is long pressed
 * [] Author:	   @Seth
 * 
 */

package main;

/*
 * The Logistic function is defined as:
 * 
 * f(x) = L / {1 + [e ^ -k(x - x0)]},
 * 
 * where:
 * 		e  : Euler's number
 * 		L  : maximum value
 * 		k  : growth rate
 * 		x0 : x value of the midPoint
 */

public class LogisticFunction
{
	double maxVal;			 // L
	double growthRate;		 // k
	double midPoint;		 // x0
	
	// = = = CONSTRUCTOR = = =
	public LogisticFunction(double maxVal, double growthRate, double midPoint)
	{
		this.maxVal = maxVal;
		this.growthRate = growthRate;
		this.midPoint   = midPoint;
	}
	
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
}
