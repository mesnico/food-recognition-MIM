package it.unipi.ing.mim.deep.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import it.unipi.ing.mim.deep.ImgDescriptor;

public class KNNClassifier {
	public static String classify(List<ImgDescriptor> ids){
		HashMap<String,Integer> resultMap=new HashMap<String,Integer>();
		String label;
		int value;
		String predictedClass;
		for(ImgDescriptor descriptor : ids){
			label=descriptor.getName();
			if(!resultMap.containsKey(label))
				resultMap.put(descriptor.getName(),0);
			value=resultMap.get(label);
			resultMap.replace(label,value+1);
		}
		predictedClass=Collections.max(resultMap.entrySet(), (res1, res2) -> res1.getValue() - res2.getValue()).getKey();
		return predictedClass;
		
	}

}
