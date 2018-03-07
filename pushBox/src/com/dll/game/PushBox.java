package com.dll.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;

import com.dll.entity.Point;
import com.dll.game.MainPanel.keyHandler;
import com.dll.util.MapUtils;

public class PushBox {
	public static void main(String[] args) {
		new MainFrame();
	}
}

class MainFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MainPanel mp=new MainPanel();
	
	private int tb=this.getHeight()-this.getContentPane().getHeight();
	private int lr=this.getWidth()-this.getContentPane().getWidth();
	
	public MainFrame(){
		mp.addKeyListener(mp.new keyHandler());
		mp.setPreferredSize(new Dimension(650, 600));
		this.add(mp,BorderLayout.CENTER);
		mp.setFocusable(true);
		this.setVisible(true);
		this.setTitle("推箱子");
		this.setBounds(0,0,650+lr,500+tb);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		validate();
	}
}

class MainPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int width=50;
	int height=50;
	int ownerX=0;  //人物坐标
	int ownerY=0;
	Image[] img=new Image[10];
	int map[][] = null;
	ArrayList<Point> arrayList=new ArrayList<Point>();
	LinkedList<Point> stack=new LinkedList<Point>();
	LinkedList<Point> queue=new LinkedList<Point>();
	
	
	public MainPanel() {
		loadImage(0);
		loadMap();
	}
	
	private void loadMap() {
		try {
			map=MapUtils.loadMap("map/map1.txt");
			arrayList=MapUtils.getDestpos();
			ownerX=MapUtils.getOwner().getX();
			ownerY=MapUtils.getOwner().getY();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void paint(Graphics g) {
		for(int i=0;i<10;i++){
			for(int j=0;j<13;j++){
				g.drawImage(img[map[i][j]], j*width,i*height, width, height, this);
			}
		}
	}
	public void loadImage(int flag){
		if(flag==0){
			for(int i=0;i<10;i++)
				img[i]=Toolkit.getDefaultToolkit().getImage("pic\\"+i+".png");
		}else if(flag==1){
			for(int i=0;i<10;i++)
			img[i]=Toolkit.getDefaultToolkit().getImage("pic\\"+i+".gif");
		}
		repaint();
	}
	
	//0填充  1障碍物  2地面 3箱子  4正确地  5下  6左  7右  8上  9是红
	public void moveUp(){
		boolean flag=true;
		if(map[ownerX-1][ownerY]==1|| 
		map[ownerX-1][ownerY]==3 && map[ownerX-2][ownerY]==1|| 
		map[ownerX-1][ownerY]==3 &&map[ownerX-2][ownerY]==3||
		map[ownerX-1][ownerY]==9 &&map[ownerX-2][ownerY]==9||
		map[ownerX-1][ownerY]==9 && map[ownerX-2][ownerY]==1){
			flag=false;
		}
		else if(map[ownerX-1][ownerY]==4){ //下一步要走的地方为目的地(不带箱子)
			stack.add(new Point(ownerX-1, ownerY)); //保存队列
			for(int i=0;i<stack.size();i++){
				System.out.println("不带箱子stack:"+stack.get(i).getX());
			}
			System.out.println(stack.isEmpty());
			map[ownerX-1][ownerY]=8;                 
			map[ownerX][ownerY]=2;      
			ownerX-=1;//更新坐标
		}else if(map[ownerX-1][ownerY]==3 && map[ownerX-2][ownerY]!=1||map[ownerX-1][ownerY]==9 && map[ownerX-2][ownerY]!=1){//是箱子且箱子上方不是障碍物
			if(map[ownerX-2][ownerY]==4){
				queue.add(new Point(ownerX-2, ownerY));
				map[ownerX-2][ownerY]=9;
			}else
			map[ownerX-2][ownerY]=3;  //3
			map[ownerX-1][ownerY]=8;
			map[ownerX][ownerY]=2;
			ownerX-=1;
		}else{
			map[ownerX][ownerY]=2;		//恢复为草地
			ownerX-=1;//更新坐标
			map[ownerX][ownerY]=8;
		}
		if(flag)
		repaint();	
	}
	public void moveDown(){
		boolean flag=true;
		if(map[ownerX+1][ownerY]==1|| 
		map[ownerX+1][ownerY]==3 && map[ownerX+2][ownerY]==1||
		map[ownerX+1][ownerY]==3 &&map[ownerX+2][ownerY]==3 || 
		map[ownerX+1][ownerY]==9 &&map[ownerX+2][ownerY]==9 || 
		map[ownerX+1][ownerY]==9 && map[ownerX+2][ownerY]==1){ 
			flag=false;
		}
		else if(map[ownerX+1][ownerY]==4){ //如果向下走是目的地
			stack.add(new Point(ownerX+1, ownerY));  //保存该目的地的坐标
			map[ownerX+1][ownerY]=5;
			map[ownerX][ownerY]=2;      //一律恢复为草地      
			ownerX+=1;//更新坐标
		}
		else if(map[ownerX+1][ownerY]==3 && map[ownerX+2][ownerY]!=1 ||map[ownerX+1][ownerY]==9 && map[ownerX+2][ownerY]!=1){//人推箱子
			if(map[ownerX+2][ownerY]==4){  //如果箱子下方是目的地
				queue.add(new Point(ownerX+2, ownerY));
				map[ownerX+2][ownerY]=9;
			}else
			map[ownerX+2][ownerY]=3;  //3   
			map[ownerX+1][ownerY]=5;
			map[ownerX][ownerY]=2;      //恢复为草地      
			ownerX+=1;
		}else{							//最普通的走路
			map[ownerX][ownerY]=2;      //否则恢复为草地      
			ownerX+=1;//更新坐标
			map[ownerX][ownerY]=5;
		}
		if(flag)
		repaint();	
	}
	public void moveLeft(){
		boolean flag=true;
		if(map[ownerX][ownerY-1]==1 ||
		map[ownerX][ownerY-1]==3 && map[ownerX][ownerY-2]==1||
		map[ownerX][ownerY-1]==3 && map[ownerX][ownerY-2]==3 || 
		map[ownerX][ownerY-1]==9 && map[ownerX][ownerY-2]==9|| 
		map[ownerX][ownerY-1]==9 && map[ownerX][ownerY-2]==1){ 
			flag=false;
		}
		else if(map[ownerX][ownerY-1]==4){ //如果向下走是目的地
			stack.add(new Point(ownerX, ownerY-1));  //保存该目的地的坐标
			map[ownerX][ownerY-1]=6;
			map[ownerX][ownerY]=2;      //一律恢复为草地      
			ownerY-=1;//更新坐标
		}else if(map[ownerX][ownerY-1]==3 && map[ownerX][ownerY-2]!=1 ||map[ownerX][ownerY-1]==9 && map[ownerX][ownerY-2]!=1){
			if(map[ownerX][ownerY-2]==4){  //如果箱子下方是目的地
				queue.add(new Point(ownerX, ownerY-2));
				map[ownerX][ownerY-2]=9;
			}else{
				map[ownerX][ownerY-2]=3;//3
			}
			map[ownerX][ownerY-1]=6;
			map[ownerX][ownerY]=2;
			ownerY-=1;
		}else{
			map[ownerX][ownerY]=2;
			ownerY-=1;//更新坐标
			map[ownerX][ownerY]=6;
		}
		if(flag)
		repaint();		
	}
	public void moveRight(){
		boolean flag=true;
		if(map[ownerX][ownerY+1]==1 || 
		map[ownerX][ownerY+1]==3 && map[ownerX][ownerY+2]==1||
		map[ownerX][ownerY+1]==3 &&map[ownerX][ownerY+2]==3 || 
		map[ownerX][ownerY+1]==9 &&map[ownerX][ownerY+2]==9 || 
		map[ownerX][ownerY+1]==9 && map[ownerX][ownerY+2]==1){	
			flag=false;
		}else if(map[ownerX][ownerY+1]==4){
			stack.add(new Point(ownerX, ownerY+1));  //保存该目的地的坐标
			map[ownerX][ownerY+1]=7;
			map[ownerX][ownerY]=2;      //一律恢复为草地      
			ownerY+=1;//更新坐标
		}else if(map[ownerX][ownerY+1]==3 && map[ownerX][ownerY+2]!=1 ||map[ownerX][ownerY+1]==9 && map[ownerX][ownerY+2]!=1){//是箱子且箱子上方不是障碍物
			if(map[ownerX][ownerY+2]==4){  //如果箱子下方是目的地
				queue.add(new Point(ownerX, ownerY+2));
				map[ownerX][ownerY+2]=9;
			}else
			map[ownerX][ownerY+2]=3;  //3
			map[ownerX][ownerY+1]=7;
			map[ownerX][ownerY]=2;
			ownerY+=1;
		}else{
			map[ownerX][ownerY]=2;
			ownerY+=1;//更新坐标
			map[ownerX][ownerY]=7;
		}
		if(flag)
		repaint();		
	}
	
	public class keyHandler extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_UP){
				moveUp();
			}
			else if(e.getKeyCode()==KeyEvent.VK_DOWN){
				moveDown();
			}
			else if(e.getKeyCode()==KeyEvent.VK_LEFT){
				moveLeft();
			}
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
				moveRight();
			}
			if(!stack.isEmpty()){				
				Point p = stack.peek();       //如果值变成了 草地
				System.out.println("peek:"+p);
				if(map[p.getX()][p.getY()]==2){
					System.out.println("出队:"+stack.pop());
					map[p.getX()][p.getY()]=4;
					repaint();
				}
			}
			if(!queue.isEmpty()){
				for(int i=0;i<queue.size();i++){
					if(map[queue.get(i).getX()][queue.get(i).getY()]==2){
						map[queue.get(i).getX()][queue.get(i).getY()]=4;
						queue.remove(i);
					}
				}
				/*if(map[p.getX()][p.getY()]==2){
					System.out.println("出队:"+queue.pop());
					map[p.getX()][p.getY()]=4;
					repaint();
				}*/
			}
			/*for(int i=0;i<10;i++){
				for(int j=0;j<13;j++){
					System.out.print(map[i][j]);
				}
				System.out.println();
			}*/
			winMsg();
		}
	}
	
	public void winMsg() {
		System.out.println(arrayList.size());
		boolean flag=true;
		for(int z=0;z<arrayList.size();z++){
			if(map[arrayList.get(z).getX()][arrayList.get(z).getY()]!=9){
				flag=false;
			}
		}
		if(flag){
			JOptionPane.showMessageDialog(null, "进入下一关");
			goNext();
		}
		
	}
	
	public void goNext(){
		try {
			map=MapUtils.loadMap("map/map2.txt");
			repaint();
			arrayList.clear();
			arrayList=MapUtils.getDestpos();
			ownerX=MapUtils.getOwner().getX();
			ownerY=MapUtils.getOwner().getY();
			stack.clear();
			queue.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

