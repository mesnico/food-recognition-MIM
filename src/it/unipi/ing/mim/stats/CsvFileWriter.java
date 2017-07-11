package it.unipi.ing.mim.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "fileName,classificationOk,precision,recall,avgPrecision";
	
	FileWriter fileWriter = null;
	
	public CsvFileWriter(String fileName){
		
		try{
			fileWriter = new FileWriter(fileName);
			
			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch(IOException e){
			System.out.println("Error opening the file "+fileName);
			e.printStackTrace();
			close();
		}
	}
	
	public void append(String fileName, boolean ok, float precision, float recall){
		try{
			fileWriter.append(String.valueOf(fileName));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(ok));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(precision));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(recall));
			fileWriter.append(NEW_LINE_SEPARATOR);
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

