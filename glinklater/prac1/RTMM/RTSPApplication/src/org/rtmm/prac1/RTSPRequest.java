package org.rtmm.prac1;

public enum RTSPRequest {
	SETUP("SETUP"), PLAY("PLAY"), PAUSE("PAUSE"), TEARDOWN("TEARDOWN"),
	OPTIONS("OPTIONS"), DESCRIBE("DESCRIBE"), UNKNOWN("UNKNOWN");

	private String desc;

	RTSPRequest(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return desc;
	}
}