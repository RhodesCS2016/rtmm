package org.rtmm.prac1;

import java.util.LinkedList;

public class ASFMetadata {
	private String title;
	private LinkedList<String> refs;
	
	public ASFMetadata(String title, String[] refs) {
		this.title = title;
		this.refs = new LinkedList<>();
		
		for (String tmp : refs) {
			this.refs.add(tmp);
		}
	}
	
	public ASFMetadata(String title, LinkedList<String> refs) {
		this.title = title;
		this.refs = refs;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the refs
	 */
	public LinkedList<String> getRefs() {
		return refs;
	}	
}
