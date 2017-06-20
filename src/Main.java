import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import org.joml.Vector3f;






public class Main extends JFrame {
	
	final static float G = (float) 6.67E-11;
	private static final long serialVersionUID = 1L;
	public JPanel topPanel;	
	public JSpinner spinnerX;
	public JSpinner spinnerY;
	public Timer timer;
	public boolean ifRunning=false;
	public static int positionX;
	public static int positionY;
	public RenderedPlanet canvas;
	public static boolean ifLagrangian=false;
	public static List<Planet> planets;
	public static List<Planet> lagrangianPoints;
	

	
	
	public Main() throws HeadlessException {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Lagrange points");
		
		topPanel = new JPanel(new GridLayout(1,  0));
		
		JButton startStopButton = new JButton("Start/Stop");
		startStopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ifRunning){
					timer.stop();
					ifRunning = false;
				} 
				else {
					timer.start();
					ifRunning = true;
				}
			}
		});
		
		JButton resetButton = new JButton("Show me Lagrangian points");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				ifRunning = false;
				canvas.clear();
				planets.clear();
				drawLagrange();
				timer.setInitialDelay(50);
				timer.start();
			}
		});
		
		
		
		
		
		
		JLabel spinnerLabel1 = new JLabel("Set 0X position");
		SpinnerNumberModel ballNumberModel1 = new SpinnerNumberModel(300, 1, 1280, 1);
		spinnerX = new JSpinner(ballNumberModel1);
		spinnerX.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				positionX = (int) spinnerX.getValue();	
			}
		});
		
		JLabel spinnerLabel2 = new JLabel("Set 0Y position");
		SpinnerNumberModel ballNumberModel = new SpinnerNumberModel(300, 1, 720, 1);
		spinnerY = new JSpinner(ballNumberModel);
		spinnerY.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				positionY = (int) spinnerY.getValue();	
			}
		});
		
		
		
		
		
		
		
		
		Container content = this.getContentPane();
		this.setBackground(new Color(121, 117, 117));
		
		canvas = new RenderedPlanet();
		canvas.setMinimumSize(new Dimension(1280, 720));
		
		content.add(topPanel, BorderLayout.NORTH);
		topPanel.add(startStopButton);
		topPanel.add(resetButton);
		topPanel.add(spinnerLabel1);
		topPanel.add(spinnerX);
		topPanel.add(spinnerLabel2);
		topPanel.add(spinnerY);
		content.add(canvas, BorderLayout.CENTER); 
		
		System.out.println(canvas.getSize());
		
		planets = new ArrayList<Planet>();
		lagrangianPoints = new ArrayList<Planet>();
		
		drawAll();
		
		
		
		timer = new Timer(4, new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				
				
				if (canvas != null && planets != null) {
					canvas.clear();
					Force force=new Force(planets);
					force.calculate();
					for(int ii=0;ii<5;ii++){
						if(ifLagrangian==true)lagrangianPoints.get(ii).setLagrangian(ii, planets.get(0), planets.get(1));
					}
					for (Planet planet : planets) canvas.drawPlanet(planet);
					for(int ii=0;ii<5;ii++){
						if(ifLagrangian==true)canvas.drawPlanet(lagrangianPoints.get(ii));
					}
					force.changeStatement();
					
				} else {
				}
				
			}
			
		});
		
		timer.setInitialDelay(200);
		
		timer.start();
		
	}
	
	
	public static void drawAll (){
		Planet tmp1 = new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (300*(4*Math.pow(10, 8))),0f),new Vector3f(30f,0f,0f),(float) (7*Math.pow(10, 24)));
		Planet tmp2 = new Planet(new Vector3f((float) (400*(4*Math.pow(10, 8))),(float) (200*(4*Math.pow(10, 8))),0f),new Vector3f(40f,22f,0f),(float) (6*Math.pow(10, 22)));
		Planet tmp3 = new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (600*(4*Math.pow(10, 8))),0f),new Vector3f(-40f,5f,0f),(float) (6*Math.pow(10, 24)));
		Planet tmp4 = new Planet(new Vector3f((float) (1000*(4*Math.pow(10, 8))),(float) (350*(4*Math.pow(10, 8))),0f),new Vector3f(-10f,-10f,0f),(float) (6*Math.pow(10, 23)));
		Planet tmp5 = new Planet(new Vector3f((float) (450*(4*Math.pow(10, 8))),(float) (250*(4*Math.pow(10, 8))),0f),new Vector3f(-10f,40f,0f),(float) (6*Math.pow(10, 23)));
		
		planets.add(tmp1);
		planets.add(tmp2);
		planets.add(tmp3);
		planets.add(tmp4);
		planets.add(tmp5);

	}
	
	public static void drawLagrange (){
		float trueX=(float) (positionX*(4*Math.pow(10, 8)));
		float trueY=(float) (positionY*(4*Math.pow(10, 8)));
		float sinAlpha=(float) ((600-positionX)/Math.sqrt((Math.pow(positionX-600, 2)+Math.pow(positionY-350, 2))));
		float cosAlpha=(float) ((positionY-350)/Math.sqrt((Math.pow(positionX-600, 2)+Math.pow(positionY-350, 2))));
		float speed=(float) Math.sqrt(G*7*Math.pow(10, 24)/(Math.sqrt(Math.pow(positionX-600, 2)+Math.pow(positionY-350, 2))*4*Math.pow(10, 8)));


		System.out.println("prepared velocity: "+speed);
		
		Planet tmp1 = new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (350*(4*Math.pow(10, 8))),0f),new Vector3f(0f,0f,0f),(float) (7*Math.pow(10, 24)));
		Planet tmp2 = new Planet(new Vector3f(trueX,trueY,0f),new Vector3f(-speed*cosAlpha,-speed*sinAlpha,0f),(float) (7*Math.pow(10, 22)));
		
		planets.add(tmp1);
		planets.add(tmp2);
		planets.get(1).setColor();
		Planet lagrangian1= new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (450*(4*Math.pow(10, 8))),0f),new Vector3f(0f,0f,0f),(float) (7*Math.pow(10, 23)));
		Planet lagrangian2= new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (450*(4*Math.pow(10, 8))),0f),new Vector3f(0f,0f,0f),(float) (7*Math.pow(10, 23)));
		Planet lagrangian3= new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (450*(4*Math.pow(10, 8))),0f),new Vector3f(0f,0f,0f),(float) (7*Math.pow(10, 23)));
		Planet lagrangian4= new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (450*(4*Math.pow(10, 8))),0f),new Vector3f(0f,0f,0f),(float) (7*Math.pow(10, 23)));
		Planet lagrangian5= new Planet(new Vector3f((float) (600*(4*Math.pow(10, 8))),(float) (450*(4*Math.pow(10, 8))),0f),new Vector3f(0f,0f,0f),(float) (7*Math.pow(10, 23)));
		lagrangian1.setColor(255,0,0);
		lagrangian2.setColor(255,0,0);
		lagrangian3.setColor(255,0,0);
		lagrangian4.setColor(255,0,0);
		lagrangian5.setColor(255,0,0);
		lagrangianPoints.add(lagrangian1);
		lagrangianPoints.add(lagrangian2);
		lagrangianPoints.add(lagrangian3);
		lagrangianPoints.add(lagrangian4);
		lagrangianPoints.add(lagrangian5);
		ifLagrangian=true;
		

	}
	
	
	

	public static void main(String[] args) {
		positionX=300;
		positionY=300;
		Main window = new Main();
		window.setSize(1280, 800);
		System.out.println(window.getSize());
		window.setVisible(true);
	}
	
}
