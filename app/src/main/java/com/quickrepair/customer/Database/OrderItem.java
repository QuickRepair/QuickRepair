package com.quickrepair.customer.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class OrderItem implements Serializable {

	public enum State { unreceived, received, repairing, paying, finished, reject }

	@PrimaryKey
	private long id;
	private String applianceType;
	private String detail;
	private int orderState;
	private long receiver;
	private long committer;

	private String createDate;
	private String receivedDate;
	private String startRepairDate;
	private String endRepairDate;
	private String finishDate;
	private String rejectDate;


	public OrderItem() {}

	@Ignore
	public OrderItem(long receiver, long committer, String type, String description) {
		this.receiver = receiver;
		this.committer = committer;
		this.applianceType = type;
		this.detail = description;
	}

	@Ignore
	public OrderItem(long id, String createDate, String type) {
		this.id = id;
		this.createDate = createDate;
		this.applianceType = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApplianceType() {
		return applianceType;
	}

	public void setApplianceType(String applianceType) {
		this.applianceType = applianceType;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public long getReceiver() {
		return receiver;
	}

	public void setReceiver(long receiver) {
		this.receiver = receiver;
	}

	public long getCommitter() {
		return committer;
	}

	public void setCommitter(long committer) {
		this.committer = committer;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getStartRepairDate() {
		return startRepairDate;
	}

	public void setStartRepairDate(String startRepairDate) {
		this.startRepairDate = startRepairDate;
	}

	public String getEndRepairDate() {
		return endRepairDate;
	}

	public void setEndRepairDate(String endRepairDate) {
		this.endRepairDate = endRepairDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getRejectDate() {
		return rejectDate;
	}

	public void setRejectDate(String rejectDate) {
		this.rejectDate = rejectDate;
	}
}
