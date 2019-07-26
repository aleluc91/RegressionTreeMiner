package utility;
import data.Data;

public class Statistics {
	
	private static double getSum(Data trainingSet , int begin , int end){
		double sum = 0.0;
		for(int i = begin  ; i <= end ; i++)
			sum += trainingSet.getClassValue(i);
		return sum;
	}
	
	private static double getSumOfSquare(Data trainingSet , int begin , int end){
	   double sumOfSquare = 0.0;
	   for(int i = begin ; i <= end ; i++)
		   sumOfSquare += Math.pow(trainingSet.getClassValue(i) , 2);
	   return sumOfSquare;
	}
	
	public static double getVariance(Data trainingSet , int begin , int end){
		double variance = 0.0;
		int size = end - begin + 1;
		variance = getSumOfSquare(trainingSet , begin , end)
					- ((Math.pow(getSum(trainingSet , begin , end), 2))  / size);
		return variance;
	}
	
	public static double getMean(Data trainingSet , int begin , int end){
		double mean = 0.0;
		int size = end - begin + 1;
		mean = getSum(trainingSet , begin , end) / size;
		return mean;
	}

}
