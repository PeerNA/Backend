package cos.peerna.domain;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationType {
	PULL_REQ, PULL_REQ_ACC, FRIEND, FRIEND_ACC, NORMAL;

	@JsonCreator
	public static NotificationType from(String s) {
		return NotificationType.valueOf(s.toUpperCase());
	}
}
