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
 * f(x) = L / 1 + [e ^ -k(x - x0)],
 * 
 * where:
 * 		e  : Euler's number
 * 		L  : maximum value
 * 		k  : growth rate
 * 		x0 : x value of the midPoint
 */
public class LogisticFunction
{
	final double e = Math.E;	// Euler's number
	double maxVal  = 0;			// L
	double growthRate = 0;		// k
	double midPoint   = 0;		// x0
	
	
	public double LogiFunc(double x)
	{
		double y;
		double exponentTerm;
		
		exponentTerm = - (growthRate) * (x - midPoint);
		y = Math.pow(e, exponentTerm);
		y += 1;
		y = maxVal / y;
		return y;
	}
	
}
