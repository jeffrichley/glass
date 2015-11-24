package com.infinity.glass.rest.data;

import java.util.List;

import com.infinity.glass.rest.data.DataColumn.Type;

public class UiConfig {
	
	public static class ColumnHeader {
		private final Type type;
		private final String label;
		public ColumnHeader(Type type, String label) {
			this.type = type;
			this.label = label;
		}
		public Type getType() {
			return type;
		}
		public String getLabel() {
			return label;
		}
	}
	
	private List<ColumnHeader> headers;

	public List<ColumnHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<ColumnHeader> headers) {
		this.headers = headers;
	}

}
