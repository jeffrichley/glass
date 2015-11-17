package com.infinity.glass.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinity.glass.config.ConfigurationUtils;
import com.infinity.glass.manager.DatasetManager;
import com.infinity.glass.manager.IdentityManager;
import com.infinity.glass.model.UserDatasetBean;
import com.infinity.glass.model.UserIdentity;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/fileupload")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDatasetBean dsb = null;
		try {
			ServletContext context = request.getServletContext();
			IdentityManager uim = ConfigurationUtils.getUserIdentityManager();
			UserIdentity userIdentity = uim.getUserIdentity(request);			
			DatasetManager dsm = ConfigurationUtils.getDatasetManager();

			StringBuilder sb = new StringBuilder("{\"result\": [");
			ByteArrayOutputStream uploadedData = new ByteArrayOutputStream();
			String fileName = "N/A";
			if (request.getHeader("Content-Type") != null
					&& request.getHeader("Content-Type").startsWith("multipart/form-data")) {
				ServletFileUpload upload = new ServletFileUpload();

				FileItemIterator iterator = upload.getItemIterator(request);
				while (iterator.hasNext()) {
					sb.append("{");
					FileItemStream item = iterator.next();
					sb.append("\"fieldName\":\"").append(item.getFieldName()).append("\",");
					if (item.getName() != null) {
						fileName = item.getName();
						dsb = dsm.saveDataset(userIdentity, fileName, item.openStream());	//	new ByteArrayInputStream(uploadedData.toByteArray()));
					} else {
						sb.append("\"value\":\"").append(read(item.openStream())).append("\"");
					}
					sb.append("}");
					if (iterator.hasNext()) {
						sb.append(",");
					}
				}
			} else {
				sb.append("{\"size\":\"" + size(request.getInputStream(), uploadedData) + "\"}");
			}

			sb.append("]");
			sb.append(", \"requestHeaders\": {");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String header = headerNames.nextElement();
				sb.append("\"").append(header).append("\":\"").append(request.getHeader(header)).append("\"");
				if (headerNames.hasMoreElements()) {
					sb.append(",\n");
				}
			}
			sb.append("}}");
			LOGGER.info("Glass uploaded file: " + sb.toString());
			
			response.getWriter().write(dsb.getDatasetId());
			
		} catch (Exception ex) {
			throw new ServletException(ex);
		}		
	}

	protected int size(InputStream stream, OutputStream out) {
		int length = 0;
		try {
			byte[] buffer = new byte[2048];
			int size;
			while ((size = stream.read(buffer)) != -1) {
				System.out.println(new String(buffer));
				length += size;
				out.write(buffer);
				out.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return length;

	}

	protected String read(InputStream stream) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return sb.toString();

	}
	
}