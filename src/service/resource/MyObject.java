package service.resource;

import java.util.ArrayList;
import java.util.HashSet;

import service.algorithm.BFS;
import ui.Main;

public class MyObject implements Comparable<MyObject>, Cloneable {
	
	public static double EARTH_RADIUS = 6371.393 * 1000;// ����뾶����λ����
	
	// OID�뾭γ��
	public int id;
	public double[] longitude;
	public double[] latitude;
	
	public int rank;// �ܽӶ���
	public int timeBecomeCC;// ��Ϊ�ܽӵ�ʱ�䣨��Ͷȵ��ܽӣ�
	public int idFrom;// �ܽ���Դ����һ�ȣ�
	
	public ArrayList< ArrayList<Integer> > edge;// �߼�
	
	public String geohashCode;// ��ǰʱ��t�ľ�γ�������Ӧ�Ĺ�ϣֵ
	
	// ͶӰ����ͼ�ϵ�ƽ�����꣬��������ͶӰת��
	public double[] x;
	public double[] y;
	
	public MyObject(int id) {
		this.id = id;
		longitude = new double[Main.MAX_TIME_STAMP + 5];
		latitude = new double[Main.MAX_TIME_STAMP + 5];
		rank = BFS.UNVISITED_FLAG;
		edge = new ArrayList< ArrayList<Integer> >();
		for (int i = 0; i <= Main.MAX_TIME_STAMP; i++)
			edge.add( new ArrayList<Integer>() );
		timeBecomeCC = Main.MAX_TIME_STAMP + 5;
		idFrom = BFS.UNVISITED_FLAG;
		geohashCode = "";
		x = new double[Main.MAX_TIME_STAMP + 5];
		y = new double[Main.MAX_TIME_STAMP + 5];
//		geo = new String[Main.MAX_TIME_STAMP + 5];
	}
	
	public MyObject(int id, int rank) {
		this.id = id;
		this.rank = rank;
	}
	
	@Override
	public int compareTo(MyObject o) {
		if (this.rank == o.rank) {
			return this.id < o.id ? (-1) : 1;
		}
		return this.rank < o.rank ? (-1) : 1;
	}
	
	@Override
	public MyObject clone() throws CloneNotSupportedException {
		MyObject obj = null;
		try {
			obj = (MyObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * ��������������ʱ���timeʱ�ľ���
	 * @param obj
	 * @param time
	 * @return
	 * ��λ����
	 */
	public double calcDistance(MyObject obj, int time) {
		double lng1 = Math.toRadians(this.longitude[time]);
		double lat1 = Math.toRadians(this.latitude[time]);
		double lng2 = Math.toRadians(obj.longitude[time]);
		double lat2 = Math.toRadians(obj.latitude[time]);
		
		double a = Math.sin((lat1 - lat2) / 2.0);
		double b = Math.sin((lng1 - lng2) / 2.0);
		double c = Math.sqrt(a * a + Math.cos(lat1) * Math.cos(lat2) * b * b);
		double dis = 2.0 * Math.asin(c) * EARTH_RADIUS;
		return dis;
	}
	
	/**
	 * �ж�������������Ƿ�С�ڵ��ھ�����ֵ
	 * ֱ�ӱȽϾ���
	 * @param o
	 * @param time
	 * @return
	 * true������ֵ��
	 * false����ֵ��
	 */
	public boolean isClose(MyObject o, int time) {
		if (this.calcDistance(o, time) <= Main.maxDistance)
			return true;
		return false;
	}
	
	/**
	 * �ж�������������Ƿ�С�ڵ��ھ�����ֵ
	 * �ȼ���ϣֵ�Ƿ���neighbor�У��پ����Ƿ�������
	 * @param o
	 * @param time
	 * @param neighbor
	 * @return
	 * true������ֵ��
	 * false����ֵ��
	 */
	public boolean isClose(MyObject o, int time, HashSet<String> neighbor) {
		if (!neighbor.contains(o.geohashCode))
			return false;
		if (this.calcDistance(o, time) <= Main.maxDistance)
			return true;
		return false;
	}
}
