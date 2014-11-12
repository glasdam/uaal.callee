package dk.medware.rehab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public class ProgressionCalculator {

	public static class RegressionFormula {
		public double beta, alpha;

		public double y(double x){
			return x * beta + alpha;
		}

		public String to_string(){
			return "y(x) = "+beta+"x+"+alpha;
		}
	}

	public static void main(String[] args) {
		puts("hej");
//		List<Double> xs_t = asList(1400328341.0, 1401797141.0, 1402401941.0, 1402833941.0, 1403006741.0);
		//List<Double> ws = create_weights(xs_t);
		List<Double> xs = asList(1415225416.0,1415225416.0,1415225416.0,1415225430.0,1415225430.0,1415225430.0,1415225452.0,1415225452.0,1415225452.0,1415225473.0,1415225473.0,1415225473.0,1415225490.0,1415225490.0,1415225490.0,1415225506.0,1415225506.0,1415225506.0,1415225518.0,1415225518.0,1415225518.0,1415225532.0,1415225532.0,1415225532.0,1415225545.0,1415225545.0,1415225545.0,1415225569.0,1415225569.0,1415225569.0,1415225582.0,1415225582.0,1415225582.0);
		List<Double> ys = asList(1.0,2.0,1.0, 1.0,2.0,2.0, 2d,2d,3d, 3d,2d,3d, 3d,3d,3d, 3d,3d,4d, 4d,4d,4d, 4d,4d,4d, 4d,5d,4d, 4d,5d,5d, 5d,5d,5d);
//		List<Double> ws = asList(1.0, 1.0, 1.0, 1.0, 1.0);
//		List<Double> ys = asList(1.0, 4.0, 3.0, 2.0, 1.0);
//		List<Double> xs = asList(1.0, 1.0, 1.0, 1.0, 1.0);
		//List<Double> ys = asList(3d, 3d, 3d, 2d, 1d, 4d, 2d, 1d, 4d, 2d, 1d, 4d, 2d, 1d, 3d, 5d, 5d, 5d, 1d, 1d, 1d, 1d, 1d, 1d, 5d, 5d, 5d, 5d, 5d, 5d, 5d, 5d, 5d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d);
		//List<Double> xs = asList(1402923820d, 1402923820d, 1402923820d, 1402927643d, 1402927643d, 1402927643d, 1402927930d, 1402927930d, 1402927930d, 1402928536d, 1402928536d, 1402928536d, 1402929357d, 1402929357d, 1402929357d, 1403011392d, 1403011392d, 1403011392d, 1403011470d, 1403011470d, 1403011470d, 1403011500d, 1403011500d, 1403011500d, 1403012929d, 1403012929d, 1403012929d, 1403012988d, 1403012988d, 1403012988d, 1403013452d, 1403013452d, 1403013452d, 1403013474d, 1403013474d, 1403013474d, 1403013534d, 1403013534d, 1403013534d, 1403013573d, 1403013573d, 1403013573d, 1403013596d, 1403013596d, 1403013596d, 1403013625d, 1403013625d, 1403013625d);
//		puts(ws);
		puts(xs);
		puts(adjust_x(xs));
		puts(calculate_simple(xs, ys));
//		puts(ys);
//		RegressionFormula rf = create_weighted(xs, ys);
//		puts(rf.to_string());
//		puts(rf.y(xs.size()));
//		puts(calculate(xs, ys));
	}

	public static List<Double> calculate_simple(List<Double> xs, List<Double> ys){
		xs = adjust_x(xs);
    System.out.println("xs: "+xs);
    System.out.println("ys: "+ys);
		Double mean_x = mean(xs);
		Double mean_y = mean(ys);

		Double numerator = 0d;
		for( int i = 0; i < xs.size(); i++){
      numerator += ((xs.get(i) - mean_x) * (ys.get(i) - mean_y));
    }

    Double denominator = 0d;
    for(int i = 0; i < xs.size(); i++){
      denominator += Math.pow(xs.get(i) - mean_x, 2);
    }

    Double slope = (numerator / denominator);

    Double intercept = mean_y - (slope * mean_x);
    List<Double> results = new ArrayList<Double>();
    System.out.println("y(x) = "+slope+"x + "+intercept);
    Double max = Collections.max(xs);
    Double estimate = intercept+slope*max;
    System.out.println("y("+max+") = " + estimate);
    Double recommendation = recommendation(estimate, slope);
    results.add(slope);
    results.add(intercept);
    results.add(estimate);
    results.add(recommendation);
		return results;
	}

	public static List<Double> adjust_x(List<Double> xs){
		Double min = Collections.min(xs);
		List<Double> new_xs = new ArrayList<Double>(xs.size());
		for (Double old_x : xs) { 
  		new_xs.add(old_x-min); 
		}
		return new_xs;
	}

	public static Double mean(List<Double> values){
		Double sum = 0d;
		for(int i = 0; i < values.size(); i++){
			sum  += values.get(i);
		}
		return sum / values.size();
	}
	
	public static List<Double> calculate(List<Double> xs, List<Double> ys){
		RegressionFormula rf = create_weighted(xs, ys);
		List<Double> results = new ArrayList<Double>();
		results.add(rf.y(xs.size()));
		results.add(rf.beta);
		results.add(recommendation(results.get(0), results.get(1)));
		return results;
	}
	
	public static double recommendation(double estimate, double slope){
		if(estimate >= 4 && slope >= 0)
			return 1.0;
		if(estimate <= 2 && slope < 0 )
			return -1.0;
		return 0;	
	}

	public static void puts(Object arg){
		System.out.println(arg);
	}

	public static RegressionFormula create_weighted(List<Double> xs, List<Double> ys){
		return create_weighted(xs, ys, create_weights(xs));
	}
	
	public static RegressionFormula create_weighted(List<Double> xs, List<Double> ys, List<Double> ws){
		Set<Double> xset = new HashSet<Double>(xs);
		if(xset.size() <= 1){
		    RegressionFormula regression_formula = new ProgressionCalculator.RegressionFormula();
		    regression_formula.beta = 0;
		    regression_formula.alpha = 3;
		    return regression_formula;			
		}
		xs = new ArrayList<Double>();
		for(int i = 0; i < ys.size(); i++)
			xs.add((double)i+1);
		List<Double> ws_tmp = new ArrayList<Double>();
		double ws_sum = 0;
		for(double i : ws)
			ws_sum += i;
		for(double weight : ws)
			ws_tmp.add(weight * ws.size()/ws_sum);
		ws = ws_tmp;
		double sumx = 0, sumy = 0, sumxx = 0, sumxy = 0;
		for(int i = 0; i < (xs.size()); i++){
			sumx  += (ws.get(i) * xs.get(i));
		  sumy  += ws.get(i) * ys.get(i);
				      sumxx += ws.get(i) * Math.pow(xs.get(i) , 2);
//				      sumyy += ws.get(i) * ys.get(i) * ys.get(i);
				      sumxy += ws.get(i) * xs.get(i) * ys.get(i);
		}
    double sumSqDevx = sumxx - sumx * sumx / ys.size();
    double slope = 0, intercept = 0;
    if(sumSqDevx != 0){
      double sumSqDevxy = sumxy - sumx * sumy / ys.size();
      slope      = sumSqDevxy / sumSqDevx;
      intercept  = (sumy - slope * sumx) / ys.size();
    } else {
      puts("Can't fit line when x values are all equal");
      sumxx = sumSqDevx = 0;
    }
    RegressionFormula regression_formula = new ProgressionCalculator.RegressionFormula();
    regression_formula.beta = slope;
    regression_formula.alpha = intercept;
    return regression_formula;
	}
	
	public static double TAU = 1144453.0;

	public static List<Double> create_weights(List<Double> xs){
		List<Double> weights = new ArrayList<Double>(); 
		if(xs.size() == 0)
			return weights;
		double max = Collections.max(xs);
		for(double value: xs){
			double weight = Math.pow(Math.E, -(max - value)/TAU);
			weights.add(weight);
		}
		return weights;
	}

}
