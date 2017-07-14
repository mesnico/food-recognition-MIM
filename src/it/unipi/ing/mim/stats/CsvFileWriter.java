package it.unipi.ing.mim.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String CONFUSION_MATRIX_HEADER = "fileName;targetClass;actualClass";
	private static final String CLASSIFICATION_SUMMARY_HEADER = "className;classificationPercentage";
	
	FileWriter fileWriter = null;
	
	public CsvFileWriter(String fileName){
		
		try{
			fileWriter = new FileWriter(fileName);
			
		} catch(IOException e){
			System.out.println("Error opening the file "+fileName);
			e.printStackTrace();
			close();
		}
	}
	
	public void appendClassificationSummaryHeader(){
		try{
			//Write the CSV file header
			fileWriter.append(CLASSIFICATION_SUMMARY_HEADER.toString());
		
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch(IOException e){
			System.out.println("Error appending the header");
			e.printStackTrace();
			close();
		}
	}
	
	public void appendConfusionMatrix(String fileName, int targetClass, int actualClass){
		try{
			fileWriter.append(String.valueOf(fileName));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(targetClass));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(actualClass));
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			fileWriter.flush();
		} catch(IOException e){
			System.out.println("Error writing stats on file ");
			e.printStackTrace();
			close();
		}
	}
	
	public void appendConfusionMatrixHeader(){
		try{
			//Write the CSV file header
			fileWriter.append(CONFUSION_MATRIX_HEADER.toString());
		
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch(IOException e){
			System.out.println("Error appending the header");
			e.printStackTrace();
			close();
		}
	}
	
	public void appendClassificationSummary(String className, float classificationPercentage){
		try{
			fileWriter.append(className);
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(classificationPercentage));
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			fileWriter.flush();
		} catch(IOException e){
			System.out.println("Error writing stats on file ");
			e.printStackTrace();
			close();
		}
	}
	
	public void close(){
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
		}
	}
}

