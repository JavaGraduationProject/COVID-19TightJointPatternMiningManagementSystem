package service.algorithm;

import java.util.ArrayList;

/**
 * trie�����
 * @author wyqaq
 */
public class TrieNode {
	public ArrayList<Integer> objs;// �ý���Ӧ�Ķ����OID
	public TrieNode[] child;// ����
	
	public TrieNode() {
		objs = new ArrayList<Integer>();
		child = new TrieNode[Encoder.BASE_LENGTH];
		for (int i = 0; i < Encoder.BASE_LENGTH; i++)
			child[i] = null;
	}
}
