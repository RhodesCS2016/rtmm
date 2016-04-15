package org.rtmm.prac1;

import java.util.LinkedList;

public class MetadataParser {
	
	public static ASFMetadata parse(String file) {
		String[] lines = file.split("\n");
		String title = "No Title";
		LinkedList<String> refs = new LinkedList<>();
		
		for (String line : lines) {
			if (line.toLowerCase().indexOf("title") > -1) {
				title = line.substring(line.toLowerCase().indexOf("<title>") + "<title>".length(), line.toLowerCase().indexOf("</title>"));
				System.err.println("DEBUG_PARSER (TITLE): " + title);
			}
			if (line.toLowerCase().indexOf("href=\"") > -1) {
				String ref = line.substring(line.toLowerCase().indexOf("href=\"") + "href=\"".length(), line.indexOf("\" />"));
				refs.add(ref);
				System.err.println("DEBUG_PARSER (REF): " + ref);
			}
		}
		
		ASFMetadata meta = new ASFMetadata(title, refs);
		return meta;
	}
}