package it.unipi.ing.mim.deep.tools;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import it.unipi.ing.mim.deep.ImgDescriptor;

public class KNNClassifier {
	
	private float classificationConfidence;
	private String predictedClass;
	
	public boolean isClassificationOk(String queryClass) {
		return this.predictedClass.equals(queryClass);
	}

	//called from the GUI
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
	
	//called during statistics recording
	public String classifyTest(List<ImgDescriptor> ids, int k){
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
		return this.predictedClass;
	}

}
