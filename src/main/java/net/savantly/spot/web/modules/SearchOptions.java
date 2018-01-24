package net.savantly.spot.web.modules;

public class SearchOptions {
	private int limit;
	private String text;
	private String field;
	
	public SearchOptions() { }
	
	public SearchOptions(int limit, String text, String field) {
		super();
		this.limit = limit;
		this.text = text;
		this.field = field;
	}

	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
}
