package com.infinity.glass.rest.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.infinity.glass.config.GlassConfiguration;
import com.infinity.glass.config.GlassConfigurationFactory;
import com.infinity.glass.rest.data.DataColumn.Type;

public class DataProvider {
	
	public static final String WEB_INF_DATA_XTRACT_CSV = "/WEB-INF/data-xtract.csv";
	
	public List<String> getHeaderData(String dataFileName) {
		List<String> headers = new ArrayList<String>();
		
		LineNumberReader headersin = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(dataFileName));
//			InputStream stream = context.getResourceAsStream(dataFileName);
			headersin = new LineNumberReader(reader);
			String headerLine = headersin.readLine();
			headers.addAll(Arrays.asList(headerLine.split("\\|")));
			headers.removeAll(getGlassConfig().getIgnoredFeatures());
		} catch (IOException e) {
			throw new RuntimeException("Unable to read " + dataFileName, e);
		} finally {
			if (headersin != null) {
				try {
					headersin.close();
				} catch (IOException e) {
				}
			}
		}
	
		return headers;
	}
	
//	public List<>
	
	protected GlassConfiguration getGlassConfig() {
		return GlassConfigurationFactory.getConfiguration();
	}
	
	protected String getCachedDataColumnFileName(String header, String dataId) {
		final String cacheDir = getGlassConfig().getCacheDirectory();
		return cacheDir + "data-column-" + dataId + "-" + header + ".glass.cache";
	}
	
	protected Gson getGson() {
		return new GsonBuilder().registerTypeAdapter(DataColumn.class, new JsonDeserializer<DataColumn<?>>() {

			@Override
			public DataColumn<?> deserialize(JsonElement element, java.lang.reflect.Type type, JsonDeserializationContext deserializationContext)
					throws JsonParseException {
				final JsonObject wrapper = (JsonObject) element;
				final Type columnType = Type.valueOf(wrapper.get("type").getAsString());
				final java.lang.reflect.Type concreteType = getClass(columnType);
				return deserializationContext.deserialize(element, concreteType);
			}
			
			protected java.lang.reflect.Type getClass(Type columnType) {
				switch (columnType) {
				case LABEL:
					return StringDataColumn.class;
				case NUMERIC:
					return DoubleDataColumn.class;
				default:
					throw new IllegalStateException("cannot deserialize data column for type: " + columnType);
				}
			}
		}).create();
	}
	
	// move all the boilerplate out into an executor
	public DataColumn<?> getDataColumn(String header, String dataId) {
		final StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		DataColumn<?> dataColumn = null;
		try {
			reader = new BufferedReader(new FileReader(getCachedDataColumnFileName(header, dataId)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			dataColumn = getGson().fromJson(sb.toString(), DataColumn.class);
		} catch (IOException e) {
		 	throw new RuntimeException("could not load data column [id:" + dataId + ", header:" + header + "]");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataColumn;
	}
	
	@SuppressWarnings("unchecked")
	public MatrixData getMatrixData(String dataFileName) {
		final MatrixData data = new MatrixData();
		LineNumberReader headersin = null;
		String[] headers = null;
		
		final Set<String> ignoredFeatures = getGlassConfig().getIgnoredFeatures();
		final Set<Integer> ignoredFeatureIndices = new HashSet<Integer>(ignoredFeatures.size());
		
		BufferedReader reader = null;
		File dataFile = new File(dataFileName);
		try {
			reader = new BufferedReader(new FileReader(dataFile));
			headersin = new LineNumberReader(reader);
			
			String headerLine = headersin.readLine();
			headers = headerLine.split("\\|", -1);
			
			Set<String> unknownHeaders = new HashSet<String>(Arrays.asList(headers));

			String line = null;
			while ((line = headersin.readLine()) != null && !unknownHeaders.isEmpty()) {
				final String[] values = line.split("\\|", -1);
				for (int i = 0; i < headers.length; i++) {
					String header = headers[i];
					if (ignoredFeatures.contains(header)) {
						ignoredFeatureIndices.add(Integer.valueOf(i));
					} else {
						// the feature isn't ignored, process it 
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
				}
			}
			
			for (String unknownHeader : unknownHeaders) {
				// TODO:(SEAN) - lots of checks for this - consolidate somehow
				if (!ignoredFeatures.contains(unknownHeader)) {
					data.addHeading(unknownHeader, Type.LABEL);
				}
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
		
		LineNumberReader in = null;
		try {
			reader = new BufferedReader(new FileReader(dataFile));
			in = new LineNumberReader(reader);
			
			// read in the headers one time
			String line = in.readLine();
			// read the actual matrix data (rows below the header row)
			while ((line = in.readLine()) != null) {
				final String[] values = line.split("\\|", -1);
				
				for (int i = 0; i < values.length && i < headers.length; i++) {
					if (!ignoredFeatureIndices.contains(i)) {
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
