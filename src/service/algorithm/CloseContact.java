package service.algorithm;

import java.util.ArrayList;
import java.util.HashSet;

import service.resource.MyObject;
import ui.App;
import ui.Main;

/**
 * ���������ھ��ܽӹ�ϵ����ͼ
 * @author wyqaq
 */
public class CloseContact {
	public static int[][] NEIGHBORHOOD = {{1, 1}, {1, 0}, {1, -1},
			{0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}
	};
	
	public int[][] contact;
	
	public CloseContact(int objNum) {
		contact = new int[objNum + 5][objNum + 5];
	}
	
	/**
	 * �������㶼�������
	 * �����κ��Ż�
	 * @param app
	 * @param objs
	 * @param objNum
	 * @param stampNum
	 * @param windowLen
	 */
	public void calc(App app, ArrayList<MyObject> objs, int objNum, int stampNum, int windowLen) {
		
		int[][] count = new int[objNum + 5][objNum + 5];
		for (int t = 1; t <= stampNum; t++) {
			Encoder.geohashAll(objs, t);
			
			for (int i = 1; i <= objNum; i++) {
				HashSet<String> neighbor = new HashSet<String>();
				neighbor.add(objs.get(i).geohashCode);
				for (int k = NEIGHBORHOOD.length - 1; k >= 0; k--) {
					neighbor.add(Encoder.geohash(objs.get(i).longitude[t] + NEIGHBORHOOD[k][0] * Encoder.longitudeUnit,
							objs.get(i).latitude[t] + NEIGHBORHOOD[k][1] * Encoder.latitudeUnit));
				}
				for (int j = i + 1; j <= objNum; j++) {
					if (objs.get(i).calcDistance(objs.get(j), t) <= Main.maxDistance) {
						count[i][j]++;
						if (count[i][j] >= windowLen) {
							contact[i][j] = t;
							contact[j][i] = t;
						}
					}
					else count[i][j] = 0;
				}
			}
			
			for (int i = 1; i <= objNum; i++) {
				for (int j = 1; j <= objNum; j++) {
					if (i == j)
						continue;
					if (contact[i][j] == t) {
						objs.get(i).edge.get(t).add(j);
					}
				}
			}
			
			app.proBar.setValue(t);
			app.proBar.paintImmediately(0, 0, app.proBar.getSize().width, app.proBar.getSize().height);
		}
	}
	
	/**
	 * ������geohash����
	 * @param app
	 * @param objs
	 * @param objNum
	 * @param stampNum
	 * @param windowLen
	 */
	public void calcWithGeohash(App app, ArrayList<MyObject> objs, int objNum, int stampNum, int windowLen) {
		
		int[][] count = new int[objNum + 5][objNum + 5];
		
		for (int t = 1; t <= stampNum; t++) {
			Encoder.geohashAll(objs, t);
			
			for (int i = 1; i <= objNum; i++) {
				HashSet<String> neighbor = new HashSet<String>();
				neighbor.add(objs.get(i).geohashCode);
				for (int k = NEIGHBORHOOD.length - 1; k >= 0; k--) {
					neighbor.add(Encoder.geohash(objs.get(i).longitude[t] + NEIGHBORHOOD[k][0] * Encoder.longitudeUnit,
							objs.get(i).latitude[t] + NEIGHBORHOOD[k][1] * Encoder.latitudeUnit));
				}
				for (int j = i + 1; j <= objNum; j++) {
					if (objs.get(i).isClose(objs.get(j), t, neighbor)) {
						count[i][j]++;
						if (count[i][j] >= windowLen) {
							contact[i][j] = t;
							contact[j][i] = t;
						}
					}
					else count[i][j] = 0;
				}
			}
			
			for (int i = 1; i <= objNum; i++) {
				for (int j = 1; j <= objNum; j++) {
					if (i == j)
						continue;
					if (contact[i][j] == t) {
						objs.get(i).edge.get(t).add(j);
					}
				}
			}
			
			app.proBar.setValue(t);
			app.proBar.paintImmediately(0, 0, app.proBar.getSize().width, app.proBar.getSize().height);
		}
	}
	
	/**
	 * ������geohash������ֵ���
	 * @param app
	 * @param objs
	 * @param objNum
	 * @param stampNum
	 * @param windowLen
	 */
	public void calcWithGeohashAndTrie(App app, ArrayList<MyObject> objs, int objNum, int stampNum, int windowLen) {
		
		int[][] count = new int[objNum + 5][objNum + 5];
		
		for (int t = 1; t <= stampNum; t++) {
			Encoder.geohashAll(objs, t);// ���ж���geohash����
			Trie trie = new Trie();
			trie.build(objs);// ��trie��
			
			for (int i = 1; i <= objNum; i++) {
				
				HashSet<String> neighbor = new HashSet<String>();// ���ڸ�������Χ�ڸ��geohashֵ
				neighbor.add(objs.get(i).geohashCode);
				for (int k = NEIGHBORHOOD.length - 1; k >= 0; k--) {
					neighbor.add(Encoder.geohash(
							objs.get(i).longitude[t]
									+ NEIGHBORHOOD[k][0] * Encoder.longitudeUnit,
							objs.get(i).latitude[t]
									+ NEIGHBORHOOD[k][1] * Encoder.latitudeUnit
					));
				}
				// isVisit=true��ʾ���ɳڹ�
				boolean[] isVisit = new boolean[objNum + 5];
				for (String str : neighbor) {
					TrieNode node = trie.find(str);
					if (node != null) {
						for (Integer j : node.objs) {
							if (j > i && objs.get(i).isClose(objs.get(j), t)) {
								count[i][j]++;
								if (count[i][j] >= windowLen) {
									contact[i][j] = t;
									contact[j][i] = t;
								}
								isVisit[j] = true;
							}
						}
					}
				}
				// û�б��ɳڹ��ĵ㣬��count��Ϊ0��ǿ��ʱ�䴰��������
				for (int j = i + 1; j <= objNum; j++) {
					if (!isVisit[j]) {
						count[i][j] = 0;
					}
				}
			}
			// ���߼����ڽӱ�
			for (int i = 1; i <= objNum; i++) {
				for (int j = 1; j <= objNum; j++) {
					if (i == j)
						continue;
					if (contact[i][j] == t) {
						objs.get(i).edge.get(t).add(j);
					}
				}
			}
			
			// ������
			app.proBar.setValue(t);
			app.proBar.paintImmediately(0, 0, app.proBar.getSize().width, app.proBar.getSize().height);
		}
	}
}
