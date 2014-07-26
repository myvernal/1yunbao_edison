package com.maogousoft.logisticsmobile.driver.model;
/**
 * 我的好友：组界面
 * @author ybxiang
 *
 */
public class FriendsGroup {
	private String groupName;
	private String groupNumber;

	public FriendsGroup() {
		super();
	}

	public FriendsGroup(String groupName, String groupNumber) {
		super();
		this.groupName = groupName;
		this.groupNumber = groupNumber;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	@Override
	public String toString() {
		return "FriendsGroup [groupName=" + groupName + ", groupNumber="
				+ groupNumber + "]";
	}

}
