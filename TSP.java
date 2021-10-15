package project;

import java.util.LinkedList;
import java.util.Collections;

import java.awt.Font;
import java.awt.Color;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;

public class TSP {
    //size of frame
    private static final int width_window = 1000;
    private static final int height_window = 540;
    public static void main (String args[]) {
        Window window = new Window(width_window, height_window, "The Traveling Salesman Project");
    }

}
    

class Window extends JFrame{
    //main attributes of designing window
    JFrame windowFrame;
    JPanel rightPanel, bottomPanel;
    JScrollPane sp;
    //inner componenets 
    Map map;
    JLabel infoMain;
    JTextArea gpsAdd, route;
    Button buttonGo, home;
    Font font; 
    //travelling salesman algorithm
    Tsp tsp;

    Window(int width, int height, String title) {   	 
        windowFrame = new JFrame();
        windowFrame.setBounds(0, 0, width, height);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        windowFrame.setTitle(title);
        windowFrame.setResizable(false);
        windowFrame.setLayout(null);

        map = new Map (700, 400);

        infoPanel();
        windowFrame.add(rightPanel);

        resultPanel();
        windowFrame.add(bottomPanel);
        windowFrame.add(map.getCanvas());
        windowFrame.setVisible(true);
    }

    private void infoPanel() {   	 
    	rightPanel = new JPanel();
    	rightPanel.setBackground(Color.LIGHT_GRAY);
    	rightPanel.setBounds(700, 0, 300, 400);
    	rightPanel.setLayout(null);
        setContentPane(rightPanel);

        final String userInfo = "<html>Enter Orders</html>";
    	infoMain = new JLabel(userInfo);
    	infoMain.setBounds(95, 10, 175, 40);
    	infoMain.setFont(new Font("SansSerif", Font.BOLD, 16));
    	infoMain.setFocusable(false);
    	infoMain.setForeground(Color.RED);

        gpsAdd = new JTextArea();
        gpsAdd.setLineWrap(true);
        gpsAdd.setWrapStyleWord(true);
    	gpsAdd.setBounds(5, 50, 275, 250);
    	gpsAdd.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	gpsAdd.setFocusable(true);

        //create scrollbar for textArea
        sp = new JScrollPane(gpsAdd);
        sp.setPreferredSize(new Dimension(275, 250));
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    	buttonGo = new Button("LETS GO!");
    	buttonGo.setBounds(5, 315, 275, 80);
    	buttonGo.setBackground(Color.GREEN);
    	buttonGo.setFocusable(true);
    	buttonGo.setFont(new Font("SansSerif", Font.BOLD, 24));
        buttonGo.addActionListener(new ActionListener() {
            //run method when "LETS GO!" button is pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = gpsAdd.getText();
                tsp = new Tsp(result);
                route.setText(tsp.getResult());
            }
        });
        
        //add pannels and create inner attributes 
    	rightPanel.add(infoMain);
    	rightPanel.add(buttonGo);
        rightPanel.add(gpsAdd);
        rightPanel.add(sp);

    	infoMain.setVisible(true);
        gpsAdd.setVisible(true);
        sp.setVisible(true);
    	buttonGo.setVisible(true);
    	rightPanel.setVisible(true);
    }
    private void resultPanel() {  
    	bottomPanel = new JPanel();
    	bottomPanel.setBounds(0, 400, 1000, 140);
    	bottomPanel.setBackground(Color.LIGHT_GRAY);
    	bottomPanel.setLayout(null);
   	 
    	route = new JTextArea();
    	route.setBounds(5, 10, 790, 80);
    	route.setFont(new Font("SansSerif", Font.PLAIN, 14));
        route.setLineWrap(true);
        route.setWrapStyleWord(true);
        route.setEditable(false);

    	home = new Button("RETURN HOME");
    	home.setBounds(805, 10, 175, 80);
    	home.setBackground(Color.ORANGE);
    	home.setFont(new Font("SansSerif", Font.BOLD, 22));
        home.addActionListener(new ActionListener() {
            //button clears textArea
            @Override
            public void actionPerformed(ActionEvent e) {
            	route.setText("");
                gpsAdd.setText("");
            }
    	});
    	
        //add pannels and create inner attributes 
    	bottomPanel.add(home);
    	bottomPanel.add(route);
   	 
    	bottomPanel.setVisible(true);
    	route.setVisible(true);
    	home.setVisible(true);
    }
}

class Map{
    
    private Point area; 
    private Canvas canvas;
    
    Map(int x, int y) {
        area = new Point(x, y); 

        ImageIcon map = new ImageIcon("map.png");
        Image mapImage = map.getImage();
        Image modMap = mapImage.getScaledInstance(x-5, y-5, java.awt.Image.SCALE_SMOOTH);
        map = new ImageIcon(modMap);

        canvas = new Canvas();
        canvas.setSize(area.x, area.y);
        canvas.setLocation(new Point(0, 0));
        canvas.setBounds(0, 0, area.x, area.y);
        canvas.setBackground(Color.CYAN);

        canvas.setVisible(true);

    }
    Canvas getCanvas() {
        return canvas;
    }
}

class Tsp {
    
    private double north;
    private double west;
    private String navigation;
    private static int order;
    private static String address;
    private static int time;
    private static double northCor;
    private static double westCor;
    

    public Tsp (String result) {
        //create starting position apache
        city apache = new city();
        LinkedList <city> allOrders = new LinkedList<city>();
        //add to list
        allOrders.add(apache);
 
        String [] inputs = result.split("\\n");
        String temp;
        for(int i = 0; i < inputs.length; i++) {
            temp = inputs[i];
            String [] inpTemp = temp.split(",");
            order = Integer.parseInt(inpTemp[0]);
            address = inpTemp[1];
            time = Integer.parseInt(inpTemp[2]);
            northCor = Double.parseDouble(inpTemp[3]);
            westCor = Double.parseDouble(inpTemp[4]);
            city location = new city(order, address, time, northCor, westCor);
            allOrders.add(location);
        }
        
        //set attributes to apache location
        north = apache.getNorthGPS();
        west = apache.getWestGPS();
        city [] cityArray = new city [allOrders.size()-1];
        //add apache at the start of the array and then remove from list
        //pos variable keeps count of the order
        int pos = 0;
        //cityArray[pos] = allOrders.get(0);
        allOrders.remove(0);
        //setting distances in each class
        double [] tempArr;
        while(allOrders.size() != 0) {
            for(int j = 0; j < allOrders.size(); j++) {
                double distTemp = calDistance(allOrders.get(j).getNorthGPS(), allOrders.get(j).getWestGPS(), north, west);
                allOrders.get(j).setDistance(distTemp);
            }
            tempArr = new double [allOrders.size()];
            for(int x = 0; x < allOrders.size(); x++) { tempArr[x] = allOrders.get(x).getDistance(); }
            double tempDist = 0.0;
            boolean swapped;
            int n = allOrders.size();
            for(int a = 0; a < n-1; a++) {
                swapped = false;
                for(int b = 0; b < n-a-1; b ++) {
                    if(tempArr[b] > tempArr[b+1]) {
                        tempDist = tempArr[b];
                        tempArr[b] = tempArr[b+1];
                        tempArr[b+1] = tempDist;
                        Collections.swap(allOrders, b, b+1);
                        swapped = true;
                    }
                }
                if(swapped == false) { break; }
            } 
            //update north and west values
            north = allOrders.get(0).getNorthGPS();
            west = allOrders.get(0).getWestGPS();
            //add top city class to array
            cityArray[pos] = allOrders.get(0);
            allOrders.remove(0);
            pos++;
        }
        String tempResult = "";
        for(int p = 0; p < cityArray.length; p++) {  tempResult += cityArray[p].getOrder() + ", "; } 
        tempResult = tempResult.substring(0, tempResult.length()-2);
        this.navigation = tempResult;
    }

    public String getResult() { return this.navigation; }

    public static double calDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        
        //haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat/2), 2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(dlon/2), 2);
        double c = 2*Math.asin(Math.sqrt(a));
        
        //radius of earth
        double r = 6371;
        
        return (c*r);
    }
}

class city {
    //variables
    private int order;
    private String locationName;
    private int time;
    private double timeR;
    private double northGPS;
    private double westGPS;
    private double distance;
    //main structure
    public city() {
        this.order = 0;
        this.locationName = "Appache Pizza Maynooth";
        this.time = 0;
        this.timeR = 0.0;
        this.northGPS = 53.38197;
        this.westGPS = -6.59274;
        this.distance = 0.0;
    }
    //user inputs
    public city(int o, String name, int t, double x, double y) {
        this.order = o;
        this.locationName = name;
        this.time = t;
        this.timeR = 0.0;
        this.northGPS = x;
        this.westGPS = y;
        this.distance = 0.0;
    }
    //getters 
    public int getOrder() {  return this.order; }
    public String getLocationName() { return this.locationName; }
    public int getTime() { return this.time; }
    public double getTimeR() { return this.timeR; }
    public double getNorthGPS() { return this.northGPS; }
    public double getWestGPS() { return this.westGPS; }
    public double getDistance() { return this.distance; }
    //setters
    public void setOrder(int o) { this.order = o; }
    public void setLocationName(String name) { this.locationName = name; }
    public void setTime(int t) { this.time = t; }
    public void setTimeR(double r) { this.timeR = r; }
    public void setNorthGPS(double x) { this.northGPS = x; }
    public void setWestGPS(double y) { this.westGPS = y; }
    public void setDistance(double d) { this.distance = d; }
}