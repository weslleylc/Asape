package core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;


public class ModelGenerator {

    public Instances loadDataset(String path) {
        Instances dataset = null;
        try {
            dataset = DataSource.read(path);
            if (dataset.classIndex() == -1) {
                dataset.setClassIndex(dataset.numAttributes() - 1);
            }
        } catch (Exception ex) {
            Logger.getLogger(ModelGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dataset;
    }

    public Evaluation evaluateModel(Classifier model, Instances traindataset, Instances testdataset) {
        Evaluation eval = null;
        try {
            // Evaluate classifier with test dataset
            eval = new Evaluation(traindataset);
            eval.evaluateModel(model, testdataset);
        } catch (Exception ex) {
            Logger.getLogger(ModelGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eval;
    }

    public void saveModel(Classifier model, String modelpath) {

        try {
            SerializationHelper.write(modelpath, model);
        } catch (Exception ex) {
            Logger.getLogger(ModelGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void safeArff(Instances dataSet, String PATH, String NAME) throws IOException {
		 ArffSaver saver = new ArffSaver();
		 saver.setInstances(dataSet);
		 saver.setFile(new File(PATH+NAME+".arff"));
		 //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
		 saver.writeBatch();
    }
    
    public Instances dataFilter(Filter filter, Instances dataset) {
    	//Normalize dataset
    	Instances datasetnor = null;
        try {
			filter.setInputFormat(dataset);
	        datasetnor = Filter.useFilter(dataset, filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasetnor;    	
    }
    
    public Instances randomData(Instances dataset, int n) {    	
    	dataset.randomize(new Debug.Random(System.currentTimeMillis()));
		return new Instances(dataset, 0,n);	
    }
    
    public Instances[] splitData(Instances dataset,int trainSize, int testSize) {
        //dataset.randomize(new Debug.Random(System.currentTimeMillis()));
    	Instances traindataset = new Instances(dataset, 0, trainSize);
        Instances testdataset = new Instances(dataset, trainSize, testSize);
        Instances[] split= new Instances[2];
        split[0]=traindataset;
        split[1]=testdataset;
		return split;
    	
    }
    

}