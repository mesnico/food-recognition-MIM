package it.unipi.ing.mim.deep.seq;

import it.unipi.ing.mim.deep.DNNExtractor;
import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.tools.FeaturesStorage;
import it.unipi.ing.mim.deep.tools.Output;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SeqImageSearch {

	private List<ImgDescriptor> descriptors;
		
	public void open(File storageFile) throws ClassNotFoundException, IOException {
		descriptors = FeaturesStorage.load(storageFile );
	}
	
	public static List<ImgDescriptor> seqSearch(File img) throws ClassNotFoundException, IOException{
		SeqImageSearch searcher = new SeqImageSearch();
		
		searcher.open(Parameters.STORAGE_FILE);
		
		DNNExtractor extractor = DNNExtractor.getInstance();
		
		float[] features = extractor.extract(img, Parameters.DEEP_LAYER);
		ImgDescriptor query = new ImgDescriptor(features, img.getName(), "");
				
		long time = -System.currentTimeMillis();
		List<ImgDescriptor> res = searcher.search(query, Parameters.K);
		
		return res;
		
	}
	
	public List<ImgDescriptor> search(ImgDescriptor queryF, int k) {
		for (int i=0;i<descriptors.size();i++){
			descriptors.get(i).distance(queryF);
		}

		Collections.sort(descriptors);
		System.out.println(descriptors.size());
		
		return descriptors.subList(0, k);
	}

}
