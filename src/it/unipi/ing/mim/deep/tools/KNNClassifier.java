package it.unipi.ing.mim.deep.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;

public class KNNClassifier {
	
	private float precision, recall;
	private String predictedClass;
	
	public float getPrecision() {
		return precision;
	}

	public float getRecall() {
		return recall;
	}
	
	public boolean isClassificationOk(String queryClass) {
		return this.predictedClass.equals(queryClass);
	}

	public String classify(List<ImgDescriptor> ids){
		HashMap<String,Integer> resultMap=new HashMap<String,Integer>();
		String label;
		int value;
		for(ImgDescriptor descriptor : ids){
			label=descriptor.getName();
			if(!resultMap.containsKey(label))
				resultMap.put(descriptor.getName(),0);
			value=resultMap.get(label);
			resultMap.replace(label,value+1);
		}
		this.predictedClass=Collections.max(resultMap.entrySet(), (res1, res2) -> res1.getValue() - res2.getValue()).getKey();
		value = resultMap.get(this.predictedClass);
		this.precision = ((float)value/(float)Parameters.K_REORDER)*100;
		//TODO
		this.recall = ((float)value/(float)Parameters.K_REORDER)*100;
		//Average precision
		float avgPrecision=0, precision = 0;
		float relevant = 0;
		float num = 1;
		for(ImgDescriptor descriptor : ids){
			if(this.predictedClass.toString().equals(descriptor.getName().toString())){
				relevant++;
				precision += (relevant/num);
				num++;
			}
			else
				num++;
		}
		avgPrecision = precision/relevant;
		
		return this.predictedClass + " at: " + this.precision + "%" + ", avg precision:" + avgPrecision;
	}

}
