package com.luv2code.utility;

public class NotificationDetails {
	private Integer unseenNotsNumber;

	private Integer notificationNumber;

	public NotificationDetails() {

	}
	
	

	public NotificationDetails(Integer unseenNotsNumber, Integer notificationNumber) {
		this.unseenNotsNumber = unseenNotsNumber;
		this.notificationNumber = notificationNumber;
	}



	public Integer getUnseenNotsNumber() {
		return unseenNotsNumber;
	}

	public void setUnseenNotsNumber(Integer unseenNotsNumber) {
		this.unseenNotsNumber = unseenNotsNumber;
	}

	public Integer getNotificationNumber() {
		return notificationNumber;
	}

	public void setNotificationNumber(Integer notificationNumber) {
		this.notificationNumber = notificationNumber;
	}

	

}
