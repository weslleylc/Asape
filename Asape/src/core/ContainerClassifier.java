package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

public class ContainerClassifier {
	HashMap<String, Classifier> models = new HashMap<String, Classifier>();
	Instances dataset;
	
	
	void insert(String name,Classifier classifier){
		models.put(name,classifier);	
	}
	
	void remove(String name){
		models.remove(name);		
	}
	
	
	double [] buildModels() throws Exception {
		double[]time=new double[models.keySet().size()];
		int i=0;
		for (String key : models.keySet()) {
				double startTime = System.currentTimeMillis();          
				models.get(key).buildClassifier(this.dataset);
				double stopTime = System.currentTimeMillis();
				time[i] = (stopTime - startTime);
				i++;
			}
		return time;
	}
	
	void insertDataset(Instances dataset) {
		this.dataset=dataset;
	}
	
}
