package it.unipi.ing.mim.deep.tools;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;

public class KNNClassifier {
	
	private float classificationConfidence,queryPrecision, recall,avgPrecision;
	private String predictedClass;
	
	public float getPrecision() {
		return queryPrecision;
	}

	public float getRecall() {
		return recall;
	}
	
	public float getConfidence(){
		return classificationConfidence;
		
	}
	
	public float getAvgPrecision(){
		return avgPrecision;
	}
	
	public boolean isClassificationOk(String queryClass) {
		return this.predictedClass.equals(queryClass);
	}

	public String classify(List<ImgDescriptor> ids, int k){
		LinkedHashMap<String,Integer> resultMap=new LinkedHashMap<String,Integer>();
		String label;
		int value;
		for(int i = 0; i < k; i++){
			ImgDescriptor descriptor = ids.get(i);
			label=descriptor.getName();
			if(!resultMap.containsKey(label))
				resultMap.put(descriptor.getName(),0);
			value=resultMap.get(label);
			resultMap.replace(label,value+1);
		}
		
		this.predictedClass=Collections.max(resultMap.entrySet(), (res1, res2) -> res1.getValue() - res2.getValue()).getKey();
		value = resultMap.get(this.predictedClass);
		this.classificationConfidence = ((float)value/(float)k)*100;
		
		return this.predictedClass + " with confidence: " + this.classificationConfidence + "%" ;
	}
	
	public String classifyTest(List<ImgDescriptor> ids,String queryClass, int k){
		LinkedHashMap<String,Integer> resultMap=new LinkedHashMap<String,Integer>();
		String label;
		int value;
		for(int i = 0; i < k; i++){
			ImgDescriptor descriptor = ids.get(i);
			label=descriptor.getName();
			if(!resultMap.containsKey(label))
				resultMap.put(descriptor.getName(),0);
			value=resultMap.get(label);
			resultMap.replace(label,value+1);
		}
		this.predictedClass=Collections.max(resultMap.entrySet(), (res1, res2) -> res1.getValue() - res2.getValue()).getKey();
		value = resultMap.get(this.predictedClass);
		int count=0;
		for(ImgDescriptor descriptor : ids){
			if(descriptor.getName().equals(queryClass))
				count++;
		}
		float precision = 0;
		float relevant = 0;
		float num = 1;
		for(ImgDescriptor descriptor : ids){
			if(queryClass.equals(descriptor.getName())){
				relevant++;
				precision += (relevant/num);
				num++;
			}
			else
				num++;
		}
		File file = new File(Parameters.SRC_FOLDER.getAbsolutePath() + "/"+ queryClass);
		this.avgPrecision = precision/(float)file.listFiles().length;
		this.queryPrecision=((float)count/(float)Parameters.K_REORDER)*100;
		this.recall=((float)count/(float)Parameters.K_REORDER)*100;
		
		return this.predictedClass; 
	}

}
