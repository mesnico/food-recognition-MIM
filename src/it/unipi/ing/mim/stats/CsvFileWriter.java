package it.unipi.ing.mim.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "fileName;targetClass;actualClass;avgPrecision";
	private static final String FILE_FINAL_HEADER = "className;classificationPercentage;meanAveragePrecision";
	
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
	
	public void appendHeader(){
		try{
			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
		
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch(IOException e){
			System.out.println("Error appending the header");
			e.printStackTrace();
			close();
		}
	}
	
	public void append(String fileName, int targetClass, int actualClass, float avgPrecision){
		try{
			fileWriter.append(String.valueOf(fileName));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(targetClass));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(actualClass));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(avgPrecision));
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch(IOException e){
			System.out.println("Error writing stats on file ");
			e.printStackTrace();
			close();
		}
	}
	
	public void appendFinalHeader(){
		try{
			//Write the CSV file header
			fileWriter.append(FILE_FINAL_HEADER.toString());
		
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch(IOException e){
			System.out.println("Error appending the header");
			e.printStackTrace();
			close();
		}
	}
	
	public void appendFinalStats(String className, float classificationPercentage,float meanAveragePrecision){
		try{
			fileWriter.append(className);
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(classificationPercentage));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(meanAveragePrecision));
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

