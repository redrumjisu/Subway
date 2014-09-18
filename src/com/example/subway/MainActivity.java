package com.example.subway;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText editText1;
	EditText editText2;
	TextView textView1;
	
	Button button1;
	
	ProgressDialog progressDialog;
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				testExcute(msg.arg1, msg.arg2);
			} else if (msg.what == 1) {
				String result = "";
				int time = 0;
				for (Vertex vertex : path) {
					result += vertex.toString() + " ";
				}
				textView1.setText(result);
				dismissProgress();
			}
		}
		
	};
	
	private void showProgress() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Wait...");
		progressDialog.setMessage("Please wait while searching...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
		progressDialog.show();
		
	}
	
	private void dismissProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editText1 = (EditText) findViewById(R.id.editText1);
		editText2 = (EditText) findViewById(R.id.editText2);
		textView1 = (TextView) findViewById(R.id.textView1);
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgress();
				int start = -1;
				int end = -1;
				try {
					start = nodes.indexOf(new Vertex(editText1.getText().toString(), editText1.getText().toString()));
					end = nodes.indexOf(new Vertex(editText2.getText().toString(), editText2.getText().toString()));
				} catch (Exception e) {
					return;
				}
				if (start < 0 || end < 0) {
					return;
				}
				Log.d("test", start + " " + end);
				handler.sendMessage(handler.obtainMessage(0, start, end));

			}
		});
		
		initStation();
		initGraph();
		

	}

	private List<Vertex> nodes;
	private List<Edge> edges;
	LinkedList<Vertex> path;

	public void testExcute(int start, int end) {

		// Lets check from location Loc_1 to Loc_10
		Graph graph = new Graph(nodes, edges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(nodes.get(start));
		path = dijkstra.getPath(nodes.get(end));
		handler.sendEmptyMessage(1);
		// assertNotNull(path);
		// assertTrue(path.size() > 0);

		// for (Vertex vertex : path) {
		// System.out.println(vertex);
		// }

	}

	private void initStation() {
		nodes = new ArrayList<Vertex>();
		try {
			AssetManager assetManager = this.getAssets();
			InputStream is = assetManager.open("station.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			while ((line = reader.readLine()) != null) {
				Log.d("test", line);
				String input[] = line.split(" ");
				nodes.add(new Vertex(input[1], input[1]));
			}
		} catch (Exception e) {
			
		}
	}

	public void initGraph() {
		edges = new ArrayList<Edge>();
		try {
			AssetManager assetManager = this.getAssets();
			InputStream is = assetManager.open("subway.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			while ((line = reader.readLine()) != null) {
				Log.d("test", line);
				String input[] = line.split(" ");
				int sourceLocNo = Integer.parseInt(input[0]) - 1;
				int destLocNo = Integer.parseInt(input[1]) - 1;
				int duration = Integer.parseInt(input[2]);
				addLane("", sourceLocNo, destLocNo, duration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);
	}

	public static final int MAX_STATION = 398;

	// class Graph {
	// Edge graph[];
	// String station[];
	// int route[];
	//
	// Stack<Integer> s, r;
	//
	// public Graph() {
	// graph = new Edge[MAX_STATION];
	// station = new String[MAX_STATION];
	// route = new int[MAX_STATION];
	// }
	//
	// public void subway(Context context) {
	// try {
	// AssetManager assetManager = context.getAssets();
	// InputStream is = assetManager.open("subway.txt");
	// InputStreamReader isr = new InputStreamReader(is);
	// BufferedReader reader = new BufferedReader(isr);
	// String line = null;
	// while ((line = reader.readLine()) != null) {
	// Log.d("test", line);
	// String input[] = line.split(" ");
	// int t1 = Integer.parseInt(input[0]) - 1;
	// int t2 = Integer.parseInt(input[1]) - 1;
	// int time = Integer.parseInt(input[2]);
	// String s1 = input[3];
	// String s2 = input[4];
	//
	// Edge temp = new Edge();
	// temp.id = t2;
	// temp.time = time;
	// if (s1.equals(s2)) {
	// temp.transfer = 1;
	// } else {
	// temp.transfer = 0;
	// }
	//
	// Edge p = graph[t1];
	// if (p != null) {
	// while (graph[p.next] != null) {
	// p = graph[p.next];
	// }
	// p.next = temp.id;
	// }
	// temp = new Edge();
	// temp.id = t1;
	// temp.time = time;
	// if (s1.equals(s2)) {
	// temp.transfer = 1;
	// } else {
	// temp.transfer = 0;
	// }
	//
	// p = graph[t2];
	// if (p != null) {
	// while (graph[p.next] != null) {
	// p = graph[p.next];
	// }
	// p.next = temp.id;
	// }
	// station[t1] = s1;
	// station[t2] = s2;
	// }
	// reader.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// public int choose() {
	// int min = 99999;
	// int min_transfer = 9999;
	// int pos = -1;
	//
	// for (int i = 0; i < MAX_STATION; i++) {
	// if (route[i] < 0) {
	// if (graph[i].time < min) {
	// min = graph[i].time;
	// min_transfer = graph[i].transfer;
	// pos = i;
	// } else if (graph[i].time == min) {
	// if (graph[i].transfer < min_transfer) {
	// min = graph[i].time;
	// min_transfer = graph[i].transfer;
	// pos = i;
	// }
	// }
	// }
	// }
	// return pos;
	//
	// }
	//
	// public void dijkstra(int start) {
	// int minpos;
	//
	// for (int i = 0; i < MAX_STATION; i++) {
	// route[i] = -1;
	// graph[i] = new Edge();
	// graph[i].id = -1;
	// graph[i].time = 99999;
	// graph[i].transfer = 9999;
	// }
	//
	// for (Edge p = graph[graph[start].next]; p != null; p = graph[p.next]) {
	// if (station[start].equals(station[p.id])) {
	// graph[p.id].time = 0;
	// graph[p.id].transfer = 0;
	// } else {
	// graph[p.id].time = p.time;
	// graph[p.id].transfer = p.transfer;
	// }
	// }
	//
	// for (int i=0; i < MAX_STATION; i++) {
	// if (station[start].equals(station[i])) {
	// for (Edge p = graph[graph[i].next]; p != null; p=graph[p.next]) {
	// if (station[i].equals(station[p.id])) {
	// graph[p.id].time = 0;
	// graph[p.id].transfer = 0;
	// }
	// }
	// }
	// }
	//
	// route[start] = 0;
	//
	// graph[start].time = 0;
	// graph[start].transfer = 0;
	//
	// for (int i = 0, prev = start; i < MAX_STATION - 2; i++) {
	// minpos = choose();
	// route[minpos] = 0;
	// for (Edge p = graph[graph[minpos].next]; p != null; p=graph[p.next]) {
	// if (graph[minpos].time + p.time < graph[p.id].time) {
	// graph[p.id].time = graph[minpos].time + p.time;
	// graph[p.id].transfer = graph[minpos].transfer + p.transfer;
	// } else if (graph[minpos].time + p.time == graph[p.id].time) {
	// if (graph[minpos].transfer + p.transfer < graph[p.id].transfer) {
	// graph[p.id].time = graph[minpos].time + p.time;
	// graph[p.id].transfer = graph[minpos].transfer + p.transfer;
	// }
	// }
	// }
	// }
	// }
	//
	// public boolean findRoute(int start, int end) {
	// s.push(start);
	//
	// if(start == end) {
	// return true;
	// }
	//
	// for (Edge p = graph[graph[start].next]; p != null; p=graph[p.next]) {
	// if (graph[start].time + p.time == graph[p.id].time &&
	// graph[start].transfer + p.transfer == graph[p.id].transfer) {
	// if (!findRoute(p.id, end)) {
	// s.pop();
	// } else {
	// return true;
	// }
	// }
	// }
	// return false;
	// }
	//
	// public String result(int start, int end) {
	// String result = station[start] + " -> " + station[end] + "\n";
	//
	// dijkstra(start);
	//
	// int min = graph[end].time;
	// for (int i = 0; i < MAX_STATION; i++) {
	// if (station[end] == station[i]) {
	// if (graph[i].transfer < min) {
	// min = graph[i].transfer;
	// end = i;
	// }
	// }
	// }
	//
	// findRoute(start, end);
	// while(!s.isEmpty()) {
	// r.push(s.pop());
	// }
	// int v = 0, prev = -1;
	// for (; !r.empty(); v++) {
	// if (prev != -1 && station[prev].equals(station[r.peek()])) {
	// if (!station[r.peek()].equals(station[start])) {
	// result += "(환승)";
	// }
	// v--;
	// prev = r.pop();
	// } else {
	// if (v != 0) {
	// result += " -> ";
	// } else {
	// result += " ";
	// }
	//
	// result += station[r.peek()];
	// prev = r.pop();
	// }
	// }
	// result += "\n\t-->" + (v-1) + "개역 지남" + "\n\t-->" + graph[end].time +
	// "분 걸리고 " + graph[end].transfer + "번 환승";
	//
	// return result;
	// }
	//
	// }
	//
	// class Edge {
	// int id;
	// int time;
	// int transfer;
	// int next;
	// }
}
