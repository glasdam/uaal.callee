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
		List<Double> ws = asList(1.0, 1.0, 1.0, 1.0, 1.0);
		List<Double> ys = asList(1.0, 4.0, 3.0, 2.0, 1.0);
		List<Double> xs = asList(1.0, 1.0, 1.0, 1.0, 1.0);
		//List<Double> ys = asList(3d, 3d, 3d, 2d, 1d, 4d, 2d, 1d, 4d, 2d, 1d, 4d, 2d, 1d, 3d, 5d, 5d, 5d, 1d, 1d, 1d, 1d, 1d, 1d, 5d, 5d, 5d, 5d, 5d, 5d, 5d, 5d, 5d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d);
		//List<Double> xs = asList(1402923820d, 1402923820d, 1402923820d, 1402927643d, 1402927643d, 1402927643d, 1402927930d, 1402927930d, 1402927930d, 1402928536d, 1402928536d, 1402928536d, 1402929357d, 1402929357d, 1402929357d, 1403011392d, 1403011392d, 1403011392d, 1403011470d, 1403011470d, 1403011470d, 1403011500d, 1403011500d, 1403011500d, 1403012929d, 1403012929d, 1403012929d, 1403012988d, 1403012988d, 1403012988d, 1403013452d, 1403013452d, 1403013452d, 1403013474d, 1403013474d, 1403013474d, 1403013534d, 1403013534d, 1403013534d, 1403013573d, 1403013573d, 1403013573d, 1403013596d, 1403013596d, 1403013596d, 1403013625d, 1403013625d, 1403013625d);
		puts(ws);
		puts(xs);
		puts(ys);
		RegressionFormula rf = create_weighted(xs, ys);
		puts(rf.to_string());
		puts(rf.y(xs.size()));
		puts(calculate(xs, ys));
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
