package core;


import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import org.github.jamm.MemoryMeter;

import weka.attributeSelection.PrincipalComponents;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.ComplementNaiveBayes;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LDA;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFClassifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.misc.HyperPipes;
import weka.classifiers.misc.VFI;
import weka.classifiers.rules.ConjunctiveRule;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;



public class Main {
	
	public static final String DATABASE = "anneal.arff,anneal.ORIG.arff,arrhythmia.arff,audiology.arff,autos.arff,balance-scale.arff,breast-cancer.arff,breast-w.arff,bridges_version1.arff,car.arff,cmc.arff,colic.arff,credit-a.arff,credit-g.arff,dermatology.arff,diabetes.arff,ecoli.arff,flags.arff,glass.arff,haberman.arff,hepatitis.arff,hypothyroid.arff,ionosphere.arff,iris.arff,kr-vs-kp.arff,labor.arff,letter.arff,liver-disorders.arff,lung-cancer.arff,lymph.arff,mfeat-factors.arff,molecular-biology_promoters.arff,mushroom.arff,nursery.arff,optdigits.arff,page-blocks.arff,pendigits.arff,postoperative-patient-data.arff,primary-tumor.arff,segment.arff,shuttle-landing-control.arff,sick.arff,solar-flare_1.arff,solar-flare_2.arff,sonar.arff,soybean.arff,spambase.arff,sponge.arff,tae.arff,tic-tac-toe.arff,trains.arff,vehicle.arff,vote.arff,vowel.arff,waveform-5000.arff,zoo.arff";
	
	public static final String DATASETPATH = "C:\\Users\\wesll\\eclipse-workspace\\Asape\\arff\\";

	public static final String MODElPATH = "C:\\Users\\wesll\\eclipse-workspace\\Asape\\models\\";
    double numPrecision = 0.01; // Default value
    

    public static void main(String[] args) throws Exception {
    	
    	
        ContainerClassifier container=new ContainerClassifier();
        
        MemoryMeter meter = new MemoryMeter();

        
        double startTime,stopTime,elapsedTime;

        
        
        
        // build classifier with train dataset 
        
        //Statiscal classifier
        container.insert("NaiveBayes",new NaiveBayes());
        container.insert("BayesNet",new BayesNet());
        
        //tree        
        container.insert("RandomTree",new RandomTree());
        container.insert("JR48",new J48());
        container.insert("RandomForest",new RandomForest());
        
        //rules
        container.insert("OneR",new OneR());
        container.insert("ConjunctiveRule",new ConjunctiveRule());
        
        //instance based
        
        container.insert("KNN",new IBk());
        container.insert("AttributeSelectedClassifier",new AttributeSelectedClassifier());
        container.insert("KStar",new KStar());
        
        //linear
        container.insert("SimpleLogistic",new SimpleLogistic()); 
        container.insert("Logistic",new Logistic()); 
        	
        
        //SVM
        container.insert("SVM",new SMO());
        
        //perceptron
        container.insert("MLP",new MultilayerPerceptron());
        container.insert("RBF",new RBFClassifier());

    	//ensemble 
        container.insert("AdaBoostM1",new AdaBoostM1());
        container.insert("LogitBoost",new LogitBoost());
        
        //Misc
        container.insert("VFI",new VFI());


    	String DATASETS[]=DATABASE.split(",");
    	for (String key : container.models.keySet()) {
			System.out.println("Algorithm:"+key);
        	}
    	
    	for (String DATASET : DATASETS) {
    		String Name=DATASET.replaceAll(".arff", "");
    		Instances dataset = mg.loadDataset(DATASETPATH+DATASET);


    		if(dataset.numAttributes()>100) {
    			PrincipalComponents pca = new PrincipalComponents();
        		pca.buildEvaluator(dataset);
        		dataset=pca.transformedData(dataset);
        		
    		}
    		
            dataset=mg.dataFilter(new Normalize(), dataset);
            dataset=mg.randomData(dataset,dataset.numInstances());
            int size =dataset.numInstances();
            
            // divide dataset to train dataset 70% and test dataset 30%
            int trainSize = (int) Math.round(size* 0.7);
            int testSize = dataset.numInstances() - trainSize;
            
            System.out.println("Dataset: "+Name+" Train: "+trainSize+" Test: "+testSize);
            
            Instances[] split=mg.splitData(dataset, trainSize, testSize);
            container.insertDataset(split[0]);       
            
            double [] time=container.buildModels();
            
            
            for (String key : container.models.keySet()) {
                mg.saveModel(container.models.get(key), MODElPATH+Name+"_"+key);
            }
            
            double sizedata =new File(DATASETPATH+DATASET).length();
            //PrintWriter writer = new PrintWriter(MODElPATH+Name+".txt", "UTF-8");
            
            int numeric=0;
            int nominal=0;
            int string=0;
            int date=0;
            int relational=0;
            
            for (int i=0;i<split[0].numAttributes();i++) {
            	if(split[0].attribute(i).isNumeric()) {
            		numeric++;
            	}
            	if(split[0].attribute(i).isNominal()) {
            		nominal++;
            	}
            	if(split[0].attribute(i).isString()) {
            		string++;
            	}
            	if(split[0].attribute(i).isDate()) {
            		date++;
            	}
            	if(split[0].attribute(i).isRelationValued()) {
            		relational++;
            	}
            }
            int i=0;
            for (String key : container.models.keySet()) {
    			System.out.println("Algorithm:"+key);
    			//System.out.println("Instances in train:"+split[0].numInstances()+" number of Features"+split[0].numAttributes());


                //load dataset
                Classifier cls = (Classifier) weka.core.SerializationHelper.read(MODElPATH+Name+"_"+key);
                double sizemodel = new File(MODElPATH+Name+"_"+key).length();
                
                System.out.println("Memory2: "+f.getDescription()+"B");

                
                startTime = System.currentTimeMillis();

    			// Evaluate classifier with test dataset
                
                CpuTimes previous =monitor.cpuTimes();
               
                Evaluation eval  = mg.evaluateModel(cls, split[0], split[1]); 

                stopTime = System.currentTimeMillis();
                elapsedTime = (stopTime - startTime);

                double memory=meter.measureDeep(cls);

                
                writer.println(split[0].numInstances()+","+split[0].numAttributes()+","+sizemodel+","+sizedata+","+numeric+","+nominal+","+string+","+date+","+relational+","+time[i]+","+(double)elapsedTime+","+(double)elapsedTime/split[1].numInstances()+","+memory+","+(1-eval.errorRate()));                
                i++;
            }
            //writer.close();           

		}
    	System.out.println("-------Finished------");

    }

}

