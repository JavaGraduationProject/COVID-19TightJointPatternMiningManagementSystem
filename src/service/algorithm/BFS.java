package service.algorithm;

import java.util.ArrayList;

import service.resource.MyObject;
import ui.Main;

public class BFS {
	
	// ��δ��Ϊ�ܽӵı�ǣ�����Ϊ��������+5
	public static int UNVISITED_FLAG = 4000 + 5;
	
	// �ܽӶ���
	public static int[] rank;
	
	/**
	 * ��sourceΪ���bfs
	 * @param c
	 * @param objs
	 * @param objNum
	 * @param stampNum
	 */
	public static void bfs(CloseContact c, ArrayList<MyObject> objs, int objNum, int stampNum) {
		
		rank = new int[objNum + 5];
		ArrayList<Integer> que = new ArrayList<Integer>();
		
		for (int i = 1; i <= objNum; i++)
			rank[i] = UNVISITED_FLAG;
		for (Integer s : Main.source) {
			rank[s] = 0;
			objs.get(s).timeBecomeCC = 0;
			que.add(s);
		}
		
		for (int t = 1; t <= stampNum; t++) {
			ArrayList<Integer> tempQue = new ArrayList<Integer>();
			for (Integer u : que) {
				ArrayList<Integer> edges = objs.get(u).edge.get(t);
				for (Integer v : edges) {
					if (rank[u] + 1 < rank[v]) {
						if (rank[v] == UNVISITED_FLAG)
							tempQue.add(v);
						rank[v] = rank[u] + 1;
						objs.get(v).timeBecomeCC = t;
						objs.get(v).idFrom = u;
					}
				}
			}
			
			que.addAll(tempQue);
			tempQue = null;
			// ��֦�������˶���Ϊ�ܽӾ���ǰ�˳�
//			if (que.size() == objNum)
//				break;
		}
		
		for (int i = 1; i <= objNum; i++)
			objs.get(i).rank = rank[i];
	}
}
