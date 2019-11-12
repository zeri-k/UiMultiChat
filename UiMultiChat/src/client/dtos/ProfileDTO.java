package client.dtos;

import java.io.Serializable;

public class ProfileDTO implements Serializable {
	private String id, p_name, p_message, img_dup_name;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getP_message() {
		return p_message;
	}

	public void setP_message(String p_message) {
		this.p_message = p_message;
	}

	public String getImg_dup_name() {
		return img_dup_name;
	}

	public void setImg_dup_name(String img_dup_name) {
		this.img_dup_name = img_dup_name;
	}
}
