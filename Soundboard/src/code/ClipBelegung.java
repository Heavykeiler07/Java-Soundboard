package code;

import java.io.Serializable;

public class ClipBelegung implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String[] paths;
	
	public ClipBelegung(String[] paths) {
		this.paths = paths;
	}
	
	public String[] getPaths() {
		return paths;
	}
	
	
	
}
