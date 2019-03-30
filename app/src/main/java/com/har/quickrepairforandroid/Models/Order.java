package com.har.quickrepairforandroid.Models;

public class Order {

	public enum State { unreceived, received, repairing, paying, finished, reject }

	private long mId;
	private String mApplianceType;
	private String mDetail;
	private State mOrderState;

	private String mCreateDate;
	private String mReceivedDate;
	private String mStartRepairDate;
	private String mEndRepairDate;
	private String mFinishDate;
	private String mRejectDate;

	public Order() {}

	public Order(long id, String createDate, String applianceType) {
		mId = id;
		mApplianceType = applianceType;
		mCreateDate = createDate;
	}

	public long id() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public String applianceType() {
		return mApplianceType;
	}

	public void setApplianceType(String applianceType) {
		mApplianceType = applianceType;
	}

	public String detail() {
		return mDetail;
	}

	public void setDetail(String detail) {
		mDetail = detail;
	}

	public State orderState() {
		return mOrderState;
	}

	public void setOrderState(State orderState) {
		mOrderState = orderState;
	}

	public String createDate() {
		return mCreateDate;
	}

	public void setCreateDate(String createDate) {
		mCreateDate = createDate;
	}

	public String receivedDate() {
		return mReceivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		mReceivedDate = receivedDate;
	}

	public String startRepairDate() {
		return mStartRepairDate;
	}

	public void setStartRepairDate(String startRepairDate) {
		mStartRepairDate = startRepairDate;
	}

	public String endRepairDate() {
		return mEndRepairDate;
	}

	public void setEndRepairDate(String endRepairDate) {
		mEndRepairDate = endRepairDate;
	}

	public String finishDate() {
		return mFinishDate;
	}

	public void setFinishDate(String finishDate) {
		mFinishDate = finishDate;
	}

	public String rejectDate() {
		return mRejectDate;
	}

	public void setRejectDate(String rejectDate) {
		mRejectDate = rejectDate;
	}
}
