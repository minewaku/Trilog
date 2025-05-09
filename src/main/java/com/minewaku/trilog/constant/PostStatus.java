package com.minewaku.trilog.constant;

public enum PostStatus {
	PUBLIC(0), 
	PRIVATE(1),
	BANNED(2),
	DELETED(3);

	private final int status;

	PostStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
