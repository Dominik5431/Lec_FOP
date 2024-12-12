package campus.data.query.csv.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Kim Berninger
 * @author ...
 * @version 1.0.2
 */
public class CSVFileAdapter implements CSVAdapter {
    private static final String DELIMITER = ";";

    private final File csvFile;

    public CSVFileAdapter(File csvFile) throws IOException {
        this.csvFile = csvFile;
    }

    @Override
    public void setHeaders(String... headers) throws IOException {
        // TODO H2 Implementieren Sie diese Methode
    	Arrays.stream(headers).forEach(str -> {
    										if (str.contains(";")) {
    											throw new CSVFormatException("Illegal symbol: ;");
    										};
    									});
//    	if (csvFile != null) {
//    		csvFile.createNewFile();
//    	} else {
//    		csvFile = new File("csvFile.txt"); 
//    		//Wie hier sonst neu erzeugen? Final verbietet dies.
//    	}
    	csvFile.createNewFile();
    	FileWriter out = new FileWriter(csvFile);
    	try (BufferedWriter writer = new BufferedWriter(out)) {
    		String text = headers[0];
        	for (int i = 1; i < headers.length; i++) {
        		text = text.concat(DELIMITER);
        		text = text.concat(headers[i]);
        	}
        	writer.write(text + "\n");
    	} catch (Exception e) {
    		throw new IOException(e.getMessage());
    	}
    }
   

    @Override
    public String[] getHeaders() throws IOException {
        // TODO H2 Implementieren Sie diese Methode
    	FileReader in = new FileReader(csvFile);
    	String[] result = null;
    	try (BufferedReader reader = new BufferedReader(in)) {
    		String head = reader.readLine();
    		if (head == null) {
    			throw new CSVFormatException("CSV file may not be empty");
    		} else {
    			result = head.split(";", -1); //Hier nochmal schauen -> nicht so
    		}
    	}
    	for (int i = 0; i< result.length; i++) {
    		result[i] = result[i].strip();
    	}
    	in.close();
        return result;
    }

    @Override
    public Stream<String[]> readRows() throws IOException {
        // TODO H2 Implementieren Sie diese Methode
    	FileReader in = new FileReader(csvFile);
    	List<String> zeilen = new ArrayList<String>();
    	try (BufferedReader reader = new BufferedReader(in)) {
    		String head = reader.readLine();
    		String zeile;
    		while ((zeile = reader.readLine()) != null) {
    			zeilen.add(zeile);
    		}
    	}
    	List<String[]> read = zeilen.stream().map(zeil -> zeil.split(";", -1)).collect(Collectors.toList());
    	for (int i = 0; i<read.size(); i++) {
    		for (int j = 0; j<read.get(i).length; j++) {
    			read.get(i)[j] = read.get(i)[j].strip();
    		}
    	}
    	in.close();
        return read.stream();
    }

    @Override
    public void writeRows(Stream<String[]> rows) throws IOException {
        // TODO H2 Implementieren Sie diese Methode
    	String[] head = getHeaders();
    	int anzahl = head.length;
    	List<String[]> zeilenList = rows.peek(strA -> {for (String str : strA) {
    							if (str.contains(";") ) {
    								throw new CSVFormatException("Illegal symbol: ;");
    							}
    						};
    						if (strA.length != anzahl) {
    							throw new CSVFormatException("Inconsistent row width: " + strA.length);
    						}
    					}).collect(Collectors.toList());; 		
    	FileWriter out = new FileWriter(csvFile);
    	try (BufferedWriter writer = new BufferedWriter(out)) {
    		zeilenList.add(0, head);
    		List<String> zeilen = zeilenList.stream().map(strA -> {String result = ""; 
    										for (String str : strA) {
    											result = result.concat(str);
    											result = result.concat(";");
    										}
    										if (result.length()>0)
    											result = result.substring(0, result.length() - 1);
    										return result;}
    								).collect(Collectors.toList());
    		for (String line: zeilen) {
    			writer.write(line + "\n");
    		}	
    	}
    	out.close();
    }

    @Override
    public void appendRow(String... row) throws IOException {
        // TODO H2 Implementieren Sie diese Methode
    	String[] head = getHeaders();
    	int anzahl = head.length;
    	if (row.length != anzahl) {
			throw new CSVFormatException("Inconsistent row width: " + row.length);
		}
    	Arrays.stream(row).forEach(str -> {
    							if (str.contains(";") ) {
    								throw new CSVFormatException("Illegal symbol: ;");
    							}
    							
    					}); 
    	FileWriter out = new FileWriter(csvFile, true);
    	String result = "";
    	try (BufferedWriter writer = new BufferedWriter(out)) {
    		for (String str : row) {
				result = result.concat(str);
				result = result.concat(";");
			}
    		if (result.length()>0) 
    			result = result.substring(0, result.length() - 1);
    		writer.append(result +"\n");
    	}    
    	out.close();
    }
}
