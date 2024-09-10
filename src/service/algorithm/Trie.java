package service.algorithm;

import java.util.ArrayList;

import service.resource.MyObject;

/**
 * �ֵ���
 * @author wyqaq
 */
public class Trie {
	public TrieNode root;
	
	public Trie() {
		root = null;
	}
	
	/**
	 * �Ӹ��ڵ㿪ʼѰ��
	 * @param str
	 * @return
	 */
	public TrieNode find(String str) {
		int len = str.length(), next;
		TrieNode node = root;
		for (int i = 0; i < len; i++) {
			next = Encoder.charToInt(str.charAt(i));
			if (node.child[next] == null)
				return null;// ����Ϊ�գ��򲻴��ڶ�Ӧ�Ķ��󣬷���null
			node = node.child[next];
		}
		return node;// ���ز��ҵ��Ľ��
	}
	
	/**
	 * ��Ӷ���ͽ��
	 * @param obj
	 */
	public void add(MyObject obj) {
		TrieNode node = root;
		String str = obj.geohashCode;
		int len = str.length(), next;
		for (int i = 0; i < len; i++) {
			next = Encoder.charToInt(str.charAt(i));
			if (node.child[next] == null) { // �Ȳ鿴�ýڵ��Ƿ���ڣ�����������newһ��
				node.child[next] = new TrieNode();
			}
			node = node.child[next];
		}
		node.objs.add(obj.id);
	}
	
	/**
	 * ����
	 * @param objs
	 */
	public void build(ArrayList<MyObject> objs) {
		root = new TrieNode();
		for (MyObject obj : objs) {
			add(obj);
		}
	}
}
