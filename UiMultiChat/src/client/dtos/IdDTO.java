package client.dtos;

import java.io.Serializable;

public class IdDTO implements Serializable{
	private String myId, fId;

	public String getMyId() {
		return myId;
	}

	public void setMyId(String myId) {
		this.myId = myId;
	}

	public String getfId() {
		return fId;
	}

	public void setfId(String fId) {
		this.fId = fId;
	}
}
