package client.dtos;

import java.io.Serializable;

public class LoginDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id, pw;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
}
