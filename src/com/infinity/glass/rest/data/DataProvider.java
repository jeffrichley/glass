package com.infinity.glass.rest.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinity.glass.rest.data.DataColumn.Type;

public class DataProvider {
	private static Logger LOGGER = LoggerFactory.getLogger(DataProvider.class);
	
	public static final String WEB_INF_DATA_XTRACT_CSV = "/WEB-INF/data-xtract.csv";
	
	public MatrixData getMatrixData(ServletContext context) {
		return getMatrixData(context, WEB_INF_DATA_XTRACT_CSV);
	}
	
	public List<String> getHeaderData(ServletContext context) {
		return getHeaderData(context, WEB_INF_DATA_XTRACT_CSV);
	}
	
	public List<String> getHeaderData(ServletContext context, String dataFileName) {
		List<String> headers = new ArrayList<String>();
		InputStream stream = context.getResourceAsStream(dataFileName);
		if(stream == null) {
			throw new IllegalArgumentException("Resource not found: " + dataFileName);
		}
		headers.addAll(getHeaderData(stream, dataFileName));
		return headers;
	}
	
	public List<String> getHeaderData(File file) {
		List<String> headers = new ArrayList<String>();
		final String dataFileName = file.getName();
		try {
			InputStream stream = new FileInputStream(file);
			headers.addAll(getHeaderData(stream, dataFileName));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found: " + dataFileName);
		}
		return headers;
	}
	
	private List<String> getHeaderData(InputStream stream, final String dataFileName) {
		List<String> headers = new ArrayList<String>();
		LineNumberReader headersin = null;
		try {
			headersin = new LineNumberReader(new InputStreamReader(stream));
			String headerLine = headersin.readLine();
			headers.addAll(Arrays.asList(headerLine.split(",")));
		} catch (IOException e) {
			throw new RuntimeException("Unable to read " + dataFileName, e);
		} finally {
			try {
				headersin.close();
			} catch (IOException e) {
			}
		}
	
		return headers;
	}
	
	public MatrixData getMatrixData(ServletContext context, String dataFileName) {
		InputStream stream = context.getResourceAsStream(dataFileName);
		if(stream == null) {
			throw new IllegalArgumentException("Resource not found: " + dataFileName);
		}
		return getMatrixData(stream, dataFileName);
	}

	public MatrixData getMatrixData(File file) {
		final String dataFileName = file.getName();
		try {
			InputStream stream = new FileInputStream(file);
			return getMatrixData(stream, dataFileName);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found: " + dataFileName);
		}
	}
	
	private MatrixData getMatrixData(InputStream stream, String dataFileName) {
		
		// Split on the comma only if that comma has zero, or an even number of quotes ahead of it.
		// Thank you stackoverflow.com
        final String otherThanQuote = " [^\"] ";
        final String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        final String regex = String.format("(?x) "+ // enable comments, ignore white spaces
                ",                         "+ // match a comma
                "(?=                       "+ // start positive look ahead
                "  (                       "+ //   start group 1
                "    %s*                   "+ //     match 'otherThanQuote' zero or more times
                "    %s                    "+ //     match 'quotedString'
                "  )*                      "+ //   end group 1 and repeat it zero or more times
                "  %s*                     "+ //   match 'otherThanQuote'
                "  $                       "+ // match the end of the string
                ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);

		MatrixData data = new MatrixData();
		LineNumberReader in = null;
		String[] headers = null;
		List<String> lines = new ArrayList<String>();
		//	Read and buffer the input file
		Set<String> unknownHeaders = null;
		try {
			in = new LineNumberReader(new InputStreamReader(stream));
			String headerLine = in.readLine();
			headers = headerLine.split(regex, -1);
			// Force all header columns to have a value
			for(int i = 0; i < headers.length; i++) {
				if(headers[1] == null || headers[i].trim().length() == 0) {
					headers[i] = String.format("column_%02d", i);
				}
			}
			unknownHeaders = new HashSet<String>(Arrays.asList(headers));
			String line = in.readLine();
			while (line != null) {
				lines.add(line);
				line = in.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to read the csv file", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException("Unable to read the csv file", e);
				}
			}
		}

		Iterator<String> iter = lines.iterator();
		while (iter.hasNext() && !unknownHeaders.isEmpty()) {
			String line = iter.next();
			String[] values = line.split(regex, -1);
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				Type type = Type.LABEL;
				if(i >= values.length) {
					LOGGER.error("ArrayIndexOutOfBounds");
				}
				
				if (values.length <= i) {
					System.out.println(values.length +"***"+line+"***");
				}
				
				
				String value = values[i];
				if (unknownHeaders.contains(header) && value != null && value.trim().length() > 0) { 
					try {
						Double.parseDouble(value);
						type = Type.NUMERIC;
					} catch (NumberFormatException nfe) { ; /* swallowing the exception is ok here */ }
					data.addHeading(header, type);
					unknownHeaders.remove(header);
				}
			}
		}

		for (String unknownHeader : unknownHeaders) {
			data.addHeading(unknownHeader, Type.LABEL);
		}
		
		String[] values;
        for(String line : lines) {
				values = line.split(regex, -1);
				
				for (int i = 0; i < values.length; i++) {
					DataColumn<?> column = data.getDataColumn(headers[i]);
					if (column.getType() == Type.LABEL) {
						DataColumn<String> stringColumn = (DataColumn<String>) column;
						stringColumn.addRow(values[i]);
					} else {
						Double value = null;
						DataColumn<Double> doubleColumn = (DataColumn<Double>) column;
						try {
							value = Double.parseDouble(values[i]);
						} catch (NumberFormatException nfe)  { ; /* swallowing the exception is ok here */ }
						doubleColumn.addRow(value);
					}
				}
			}
		
		return data;
	}
	
}
