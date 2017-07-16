package it.unipi.ing.mim.stats;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.seq.SeqImageSearch;
import it.unipi.ing.mim.img.lucene.LucImageSearch;

public class CollectMAPStatistics {

	//directory containing all the images used for testing the system
	private static final String PROBE_DIRECTORY = "data/test";
	private static final String STATS_DIRECTORY = "stats/";
		
	public static void main(String[] args) throws Exception {
		int totalImagesTested = 0;
		float[] precision=new float[Parameters.K_REORDER];
		float[] recall=new float[Parameters.K_REORDER];
		File srcFolder = new File(PROBE_DIRECTORY);
		File[] folders = srcFolder.listFiles();
		//Data structure to save Class and Id 
		HashMap<String,Integer> resultMap=new HashMap<String,Integer>();
		String label;
		int value=1;
		for(File folderName : folders){
			label=folderName.getName();
			resultMap.put(label,value++);
		}
		
		//create a directory with the current timestamp
		File currentDir = new File(STATS_DIRECTORY +"/"+ System.currentTimeMillis());
		currentDir.mkdirs();
		File globalDir = new File(currentDir.getAbsolutePath() + "/MAP");
        if (!globalDir.exists()){
        	globalDir.mkdir();
        }
        
        //initialize the writer needed to write the overall classes statistics
        CsvFileWriter csvMAPWriter=new CsvFileWriter(globalDir.getAbsolutePath() +"/MAPStats.csv");
        csvMAPWriter.appendMAPHeader();
        
		for (File imgFolder: folders) {
			/*if(!imgFolder.getName().equals("pizza"))
				continue;*/
			File[] imgFiles = imgFolder.listFiles();
			
			//initialize the writer to write the measurements for the single class
			float averagePrecisionSum=0;
			int count=1;
			for (File imgFile: imgFiles) {
				//Collect statistics for the current probe image
				long starttime = System.currentTimeMillis();
				LucImageSearch l = new LucImageSearch(Parameters.PIVOTS_FILE, Parameters.TOP_K_QUERY);
	            List<ImgDescriptor> foundImages = l.recognizeImage(imgFile);
	            System.out.println(foundImages.size());
	            
	            System.out.println("["+imgFolder.getName()+"]: ("+count+"/"+imgFiles.length+") - overall search time for "+imgFile.getName()+": "+(System.currentTimeMillis() - starttime)+"ms");
	            
	            //record statistics
	            float precisionSum = 0;
	   			float relevant = 0;
	   			int num = 1;
	   			for(ImgDescriptor descriptor : foundImages){
	   				if(imgFolder.getName().equals(descriptor.getName())){
	   					relevant++;
	   					precisionSum += (relevant/num);
	   					precision[(num-1)]+=relevant/num;
	   					recall[(num-1)]+=relevant/1000;
		   				//System.out.println("precision per k="+num+" : " + relevant/num);
		   				//System.out.println("Recall per k="+num+" : " + relevant/1000);
	   					num++;
		   			}
		   			else{
		   				precision[(num-1)]+=relevant/num;
		   				recall[(num-1)]+=relevant/1000;
		   				//System.out.println("precision per k="+num+" : "+relevant/num);
		   				//System.out.println("Recall per k="+num+" : " + relevant/1000);
		   				num++;
		   			}
	   			}
	   			File classFolder=new File(Parameters.SRC_FOLDER.getAbsolutePath()+"/"+imgFolder.getName());
	   			float averagePrecision=precisionSum/classFolder.listFiles().length;
	   			System.out.flush();
	   			System.out.println(averagePrecision);
	            averagePrecisionSum+=averagePrecision;
	            totalImagesTested++;
	            count++;
	   		}
            
			//calculates the MAP for the current class
			float meanAveragePrecision=averagePrecisionSum/(float)imgFiles.length;
			
			//outputs the MAP on file
			csvMAPWriter.appendMAPStats(imgFolder.getName(),meanAveragePrecision);

		}
		
		//calculates overall precision and recall for each k
		CsvFileWriter csvPrecRecWriter=new CsvFileWriter(globalDir.getAbsolutePath() +"/PrecisionRecallStats.csv");
		csvPrecRecWriter.appendPrecisionRecallHeader();
		
		for(int i=0;i<Parameters.K_REORDER;i++){
			precision[i]=precision[i]/(float)totalImagesTested;
			recall[i]=recall[i]/(float)totalImagesTested;
			csvPrecRecWriter.appendPrecisionRecall((i+1),recall[i],precision[i]);
		}
		
	}

}
