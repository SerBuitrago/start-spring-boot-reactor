package co.com.reator.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
	
	private List<String> comments;
	
	public Comment(){
		this(new ArrayList<>());
	}
	
	public void add(String comment) {
		this.comments.add(comment);
	}
}
