package it.unipi.ing.mim.deep.seq;

import it.unipi.ing.mim.deep.DNNExtractor;
import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.tools.FeaturesStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SeqImageStorage {

	public static void main(String[] args) throws Exception {
				
		SeqImageStorage indexing = new SeqImageStorage();
				
		List<ImgDescriptor> descriptors = indexing.extractFeatures(Parameters.SRC_FOLDER);
		
		FeaturesStorage.store(descriptors, Parameters.STORAGE_FILE);
	}
	
	private List<ImgDescriptor> extractFeatures(File imgFolder){
		List<ImgDescriptor> descs;
		int i;

		try{
		descs = FeaturesStorage.load(Parameters.STORAGE_FILE);
		i = descs.size();
		System.out.println("descriptor:" + descs.size()
				+ " filename:"+ descs.get(descs.size()-1).getId());
		}
		catch (Exception io){
			descs = new ArrayList<ImgDescriptor>();
			i = 0;
			System.out.println("Exception" + io);
		}
		
		//File[] files = imgFolder.listFiles();
		ArrayList<File> filesArr = new ArrayList<File>();
		listf(imgFolder.toString(), filesArr);
		File[] files = filesArr.toArray(new File[filesArr.size()]);
		
		DNNExtractor extractor = DNNExtractor.getInstance();

		for (; i < files.length; i++) {
			System.out.println(i + " - extracting " + files[i].getName());
			try {
				long time = -System.currentTimeMillis();
				float[] features = extractor.extract(files[i], Parameters.DEEP_LAYER);
				time += System.currentTimeMillis();
				System.out.println(time);
				descs.add(new ImgDescriptor(features, files[i].getName(), files[i].getParentFile().getName()));
				if(i%200 == 0)
					FeaturesStorage.store(descs, Parameters.STORAGE_FILE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return descs;	
	}	
	
	public void listf(String directoryName, ArrayList<File> files) {
	    File directory = new File(directoryName);

	    System.out.println("Current directory: " + directoryName);
	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath(), files);
	        }
	    }
	}
}
