package com.infinity.glass.rest.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import com.infinity.glass.rest.data.DataColumn.Type;

public class DataProvider {

	public static final String WEB_INF_DATA_XTRACT_CSV = "/WEB-INF/data-xtract.csv";
	
	public MatrixData getMatrixData(ServletContext context) {
		return getMatrixData(context, WEB_INF_DATA_XTRACT_CSV);
	}
	
	public List<String> getHeaderData(ServletContext context) {
		return getHeaderData(context, WEB_INF_DATA_XTRACT_CSV);
	}
	
	public List<String> getHeaderData(ServletContext context, String dataFileName) {
		List<String> headers = new ArrayList<String>();
		
		LineNumberReader headersin = null;
		
		try {
			InputStream stream = context.getResourceAsStream(dataFileName);
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
		MatrixData data = new MatrixData();
		
		LineNumberReader headersin = null;
		String[] headers = null;
		try {
			InputStream stream = context.getResourceAsStream(dataFileName);
			headersin = new LineNumberReader(new InputStreamReader(stream));
			
			String headerLine = headersin.readLine();
			String line = headersin.readLine();
			String[] values = line.split(",");
			headers = headerLine.split(",");
			
			Set<String> unknownHeaders = new HashSet<String>(Arrays.asList(headers));

			while (line != null && !unknownHeaders.isEmpty()) {
				for (int i = 0; i < headers.length; i++) {
					String header = headers[i];
					Type type = Type.LABEL;
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
				
				line = headersin.readLine();
				if (line != null) {
					values = line.split(",");
				}
			}
			
			for (String unknownHeader : unknownHeaders) {
				data.addHeading(unknownHeader, Type.LABEL);
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Unable to read the csv file", e);
		} finally {
			if (headersin != null) {
				try {
					headersin.close();
				} catch (IOException e) {
					throw new RuntimeException("Unable to read the csv file", e);
				}
			}
		}
			
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
		
		LineNumberReader in = null;
		try {
			InputStream stream = context.getResourceAsStream(dataFileName);
			in = new LineNumberReader(new InputStreamReader(stream));
			String line = in.readLine();
			line = in.readLine();
			String[] values = line.split(",");
			while (line != null) {
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
		
		return data;
	}
	
}
