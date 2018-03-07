package com.dll.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.dll.entity.Point;
/*
 * 把地图文件对应的数据转化成二维数组
 */
public class MapUtils {
	private static int map[][]=new int[10][13];
	private static ArrayList<Point> arrayList=new ArrayList<Point>();
	/*public static void main(String[] args) {
		String fileName="E:\\Workspaces\\MyEclipse 10\\Box\\map\\map1.txt";
		try {
			int[][] loadMap = MapUtils.loadMap(fileName);
			for(int i=0;i<10;i++){
				for(int j=0;j<13;j++){
					System.out.println("i:"+i+"j:"+j+" "+loadMap[i][j]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	/*
	 * 地图文件的读取
	 */
	public static int[][] loadMap(String fileName) throws IOException{
		FileInputStream fi=new FileInputStream(fileName);
		byte buf[]=new byte[1024];
		char[] arr=null;
		while(fi.read(buf)!=-1){
			arr=new String(buf).toCharArray();
		}
		fi.close();
		return writeToArr(arr);
		
	}
	
	private static int[][] writeToArr(char[] arr){ 
		int count=0;
		for(int i=0;i<10;i++){
			for(int j=0;j<13;j++){
					if(arr[13*i+j+count]==13){
						count+=2;
					}
					map[i][j]=arr[13*i+j+count]-'0';
			}
		}
		return map;
	}
	public static Point getOwner(){
		for(int i=0;i<10;i++){
			for(int j=0;j<13;j++){
				if(map[i][j]==5){
					return new Point(i,j);
				}
			}
		}
		return null;
	}
	
	public static ArrayList<Point> getDestpos(){
		for(int i=0;i<10;i++){
			for(int j=0;j<13;j++){
				if(map[i][j]==4){
					arrayList.add(new Point(i, j));
				}
			}
		}
		return arrayList;
	}
}
