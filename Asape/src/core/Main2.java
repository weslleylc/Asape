package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
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
import weka.classifiers.functions.SMOreg;
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
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;



public class Main2 {
	
	//public static final String DATABASE = "Acorns.arff,Ageandheight.arff,agecondat.arff,AirPollution.arff,airpullutionfiltersdat.arff,AlcoholandTobacco.arff,anneal.arff,anneal.ORIG.arff,appliancedat.arff,ar1.arff,ar3.arff,ar4.arff,ar5.arff,ar6.arff,arrhythmia.arff,audiology.arff,autos.arff,balance-scale.arff,Balance.arff,batdat.arff,beefrefdat.arff,Billionaires92.arff,Birthrates.arff,Brainsize.arff,breast-cancer.arff,breast-w.arff,BreastCancer.arff,bridges_version1.arff,brinkdat.arff,Calcium.arff,Calories.arff,CancerSurvival.arff,car.arff,carmpgdat.arff,Cars.arff,Cavendish.arff,Cereals.arff,Chromatography.arff,cigcancerdat.arff,Cities.arff,Clouds.arff,cm1_req.arff,cmc.arff,colic.arff,Colleges.arff,Companies.arff,Crawling.arff,credit-a.arff,credit-g.arff,Crews.arff,cuckoodat.arff,cylinder-bands.arff,dealersalesdat.arff,dermatology.arff,diabetes.arff,differencetestdat.arff,distributiondat.arff,DraftLottery.arff,DRPScores.arff,ecoli.arff,EducationalSpending.arff,Educationbyage.arff,Eggs.arff,EgyptianSkulls.arff,Emerald.arff,enrolldat.arff,eradat.arff,EuropeanJobs.arff,FacultySalaries.arff,fbis.wc.arff,Fiber.arff,FishersIris.arff,FishPrices.arff,flags.arff,FleaBeetles.arff,Fridaythe13th.arff,FusionTime.arff,glass.arff,Graduation.arff,haberman.arff,Hearing.arff,HeartValves.arff,Heliumfootball.arff,hepatitis.arff,hittersdat.arff,homedat.arff,HomeRuns.arff,Hotdogs.arff,Hubble.arff,hwfataldat.arff,hypothyroid.arff,IceCream.arff,ICU.arff,inflationdat.arff,InstructorBehavior.arff,ionosphere.arff,iris.arff,jEdit_4.0_4.2.arff,jEdit_4.2_4.3.arff,jm1.arff,kc1-class-level-binary.arff,kc1-class-level-numeric.arff,kc1-class-level-top5.arff,kc1.arff,kc2.arff,kc3.arff,KDDCup09_appetency.arff,KDDCup09_churn.arff,KDDCup09_upselling.arff,KDDCup99.arff,kr-vs-kp.arff,la1s.wc.arff,la2s.wc.arff,labor.arff,LaborForce.arff,laborlawdat.arff,LarynxCancer.arff,letter.arff,leukemia_train.arff,liver-disorders.arff,lunaticsdat.arff,lung-cancer.arff,lymph.arff,magadsdat.arff,mc1.arff,mc2.arff,Medflies.arff,MercuryinBass.arff,mfeat-factors.arff,mfeat-fourier.arff,mfeat-karhunen.arff,mfeat-morphological.arff,mfeat-pixel.arff,mfeat-zernike.arff,Michelson.arff,MilitiamenChests.arff,molecular-biology_promoters.arff,montanadat.arff,mortgagerefusalsdat.arff,mozilla4.arff,mushroom.arff,mw1.arff,nambedat.arff,NATO.arff,NestedModel.arff,new3s.wc.arff,NewJersey.arff,NuclearPlants.arff,nursery.arff,Nurses.arff,nursinghomedat.arff,nycrimedat.arff,oecdat.arff,oh0.wc.arff,oh10.wc.arff,oh15.wc.arff,oh5.wc.arff,ohscal.wc.arff,Oilproduction.arff,OlympicGold.arff,optdigits.arff,page-blocks.arff,Parathion.arff,pc1.arff,pc2.arff,pc3.arff,pc4.arff,pendigits.arff,Pollutants.arff,PopularKids.arff,postoperative-patient-data.arff,Pottery.arff,primary-tumor.arff,Protein.arff,pubexpendat.arff,qbacksalarydat.arff,qqdefects_numeric.arff,quartappliancedat.arff,re0.wc.arff,re1.wc.arff,ReadingTestScores.arff,reduced_2classes.arff,Refinery.arff,segment.arff,Shoppers.arff,shuttle-landing-control.arff,sick.arff,Singers.arff,SmokingandCancer.arff,solar-flare_1.arff,solar-flare_2.arff,sonar.arff,soybean.arff,spambase.arff,spectf_test.arff,spectf_train.arff,spectrometer.arff,spect_test.arff,spect_train.arff,splice.arff,sponge.arff,Stepping.arff,Strollers.arff,studentdat.arff,tae.arff,tastedat.arff,teacherpaydat.arff,Termites.arff,test.arff,tic-tac-toe.arff,timeseriesdat.arff,tr11.wc.arff,tr12.wc.arff,tr21.wc.arff,tr23.wc.arff,tr31.wc.arff,tr41.wc.arff,tr45.wc.arff,train.arff,trains.arff,transformationdat.arff,tumors_C.arff,tvadsdat.arff,Unemployment.arff,USCrime.arff,USTemperatures.arff,vehicle.arff,vehwghtdat.arff,vote.arff,vowel.arff,wagesdat.arff,wap.wc.arff,wasterunupdat.arff,waveform-5000.arff,WildHorses.arff,wine.arff,WorldSeries.arff,yeast.arff,zoo.arff";

	//public static final String DATABASE = "Acorns.arff,Ageandheight.arff,agecondat.arff,AirPollution.arff,airpullutionfiltersdat.arff,AlcoholandTobacco.arff,appliancedat.arff,batdat.arff,beefrefdat.arff,Birthrates.arff,Brainsize.arff,BreastCancer.arff,brinkdat.arff,Calcium.arff,carmpgdat.arff,Cars.arff,Cavendish.arff,Cereals.arff,Chromatography.arff,cigcancerdat.arff,Cities.arff,Clouds.arff,Colleges.arff,Crawling.arff,Crews.arff,cuckoodat.arff,dealersalesdat.arff,differencetestdat.arff,distributiondat.arff,DraftLottery.arff,DRPScores.arff,EducationalSpending.arff,Educationbyage.arff,EgyptianSkulls.arff,Emerald.arff,enrolldat.arff,eradat.arff,EuropeanJobs.arff,FacultySalaries.arff,FishersIris.arff,FishPrices.arff,Graduation.arff,Hearing.arff,HeartValves.arff,Heliumfootball.arff,hittersdat.arff,homedat.arff,HomeRuns.arff,Hotdogs.arff,Hubble.arff,hwfataldat.arff,IceCream.arff,ICU.arff,inflationdat.arff,InstructorBehavior.arff,kc1-class-level-numeric.arff,LaborForce.arff,laborlawdat.arff,lunaticsdat.arff,magadsdat.arff,Medflies.arff,MercuryinBass.arff,MilitiamenChests.arff,montanadat.arff,mortgagerefusalsdat.arff,nambedat.arff,NestedModel.arff,NewJersey.arff,NuclearPlants.arff,Nurses.arff,nursinghomedat.arff,nycrimedat.arff,oecdat.arff,Oilproduction.arff,OlympicGold.arff,Parathion.arff,Pollutants.arff,PopularKids.arff,Protein.arff,qbacksalarydat.arff,qqdefects_numeric.arff,quartappliancedat.arff,ReadingTestScores.arff,Shoppers.arff,Singers.arff,SmokingandCancer.arff,spectf_train.arff,Stepping.arff,Strollers.arff,studentdat.arff,tastedat.arff,teacherpaydat.arff,Termites.arff,timeseriesdat.arff,transformationdat.arff,tvadsdat.arff,Unemployment.arff,USCrime.arff,USTemperatures.arff,vehwghtdat.arff,wagesdat.arff,wasterunupdat.arff,WildHorses.arff,wine.arff,WorldSeries.arff";
	public static final String DATABASE = "Acorns.arff";

	public static final String DATASETPATH = "C:\\Users\\wesll\\eclipse-workspace\\Asape\\arff\\";

    public static final String MODElPATH = "C:\\Users\\wesll\\eclipse-workspace\\Asape\\models\\";
    double numPrecision = 0.01; // Default value

    
    public static void main(String[] args) throws Exception {
    	
    	ModelGenerator mg = new ModelGenerator();
         
        ContainerClassifier container=new ContainerClassifier();
        
        MemoryMeter meter = new MemoryMeter();
        
        double startTime,stopTime,elapsedTime;


        

        // build classifier with train dataset 
        
        //Statiscal classifier
        
        //tree        
        container.insert("RandomTree",new RandomTree());
        container.insert("RandomForest",new RandomForest());
        container.insert("REPTree",new REPTree());

        //rules
        container.insert("ConjunctiveRule",new ConjunctiveRule());
        
        //instance based        
        container.insert("KNN",new IBk());
        container.insert("KStar",new KStar());
        
        //linear
        container.insert("LinearRegression",new LinearRegression()); 


        
        //SVM
        container.insert("SMOreg",new SMOreg());
        
        //perceptron
        container.insert("MLP",new MultilayerPerceptron());
     
        container.insert("M5P",new M5P());

    	String DATASETS[]=DATABASE.split(",");
    	
    	for (String key : container.models.keySet()) {
		System.out.println(key);}
    	
    	for (String DATASET : DATASETS) {
    		String Name=DATASET.replaceAll(".arff", "");
    		Instances dataset = mg.loadDataset(DATASETPATH+DATASET);
    		/*
    		if(dataset.numAttributes()>100) {
    			PrincipalComponents pca = new PrincipalComponents();
        		pca.buildEvaluator(dataset);
        		dataset=pca.transformedData(dataset);
        		
    		}*/
    		
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
            PrintWriter writer = new PrintWriter(MODElPATH+Name+".txt", "UTF-8");
            
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
    			//load dataset
                Classifier cls = (Classifier) weka.core.SerializationHelper.read(MODElPATH+Name+"_"+key);
                double sizemodel = new File(MODElPATH+Name+"_"+key).length();
                
                //System.out.println("Size of the model: "+sizemodel+"B");
                
    			// Evaluate classifier with test dataset
                startTime = System.currentTimeMillis();
                Evaluation eval  = mg.evaluateModel(cls, split[0], split[1]);  
                
                stopTime = System.currentTimeMillis();
                elapsedTime = (stopTime - startTime);
                System.out.println("Time im milisseconds on evaluation: " + elapsedTime+"ms");
                
                double memory=meter.measureDeep(cls);
                System.out.println("RAM used for the model: "+memory+"B");            
                System.out.println("RMSE: "+eval.rootMeanSquaredError()+"\n");



                
                writer.println(split[0].numInstances()+","+split[0].numAttributes()+","+sizemodel+","+sizedata+","+numeric+","+nominal+","+string+","+date+","+relational+","+time[i]+","+(double)elapsedTime+","+(double)elapsedTime/split[1].numInstances()+","+memory+","+ eval.correlationCoefficient()+','+eval.meanAbsoluteError()+','+eval.rootMeanSquaredError()+','+eval.relativeAbsoluteError()+','+eval.rootRelativeSquaredError());                
                i++;
            }
            writer.close();           

		}
    	System.out.println("-------Finished------");

    }

}

