package com.example.subway;

public class Edge {
	private String id;
	private Vertex source;
	private Vertex destination;
	private int time;
	
	public Edge(String id, Vertex source, Vertex destination, int time) {
		super();
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.time = time;
	}
	
	public String getId() {
		return id;
	}
	public Vertex getSource() {
		return source;
	}
	public Vertex getDestination() {
		return destination;
	}
	public int getTime() {
		return time;
	}
}
