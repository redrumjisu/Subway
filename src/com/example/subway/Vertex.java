package com.example.subway;

public class Vertex {
	private String id;
	private String name;
	private boolean transfer;
	
	public Vertex(String id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.transfer = false;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isTransfer() {
		return transfer;
	}
	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		Vertex other = (Vertex) o;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
