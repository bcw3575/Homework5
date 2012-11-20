import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A simulated university.
 * 
 * @author Brian Walker
 * 
 */
public class SimU extends JFrame implements ActionListener {

	public static ImageIcon academicImage = new ImageIcon(
			"AcademicBuilding.jpg");
	public static ImageIcon libraryImage = new ImageIcon("Bookstacks.jpg");
	public static ImageIcon personImage = new ImageIcon("stickPerson.gif");
	public static ImageIcon residenceImage = new ImageIcon("residenceHall.jpg");
	public static ImageIcon administrativeBuilding = new ImageIcon("administrativeBuilding.jpg");
	public static ImageIcon athleticFields = new ImageIcon("athleticFields.jpg");
	public static ImageIcon footballStadium = new ImageIcon("footballStadium.jpg");
	public static ImageIcon greenSpace = new ImageIcon("greenSpace.jpg");
	public static ImageIcon gymnasium = new ImageIcon("gymnasium.jpg");
	public static ImageIcon studentCenter = new ImageIcon("studentCenter.jpg");
	public static ImageIcon water = new ImageIcon("water.jpg");
	public static ImageIcon walkingMan = new ImageIcon("walking-man.gif");
	public static ImageIcon sidewalks = new ImageIcon("sidewalk.jpg");
	public static ImageIcon walkingFaculty = new ImageIcon("walkingFaculty.gif");
	public static ImageIcon backgroundCampus = new ImageIcon("campusbackground.jpg");
	
	private ArrayList<Building> buildings;
	private ArrayList<Person> students;
	private ArrayList<Person> faculty;
	public static JSlider slider;
	public static DrawingPanel drawing;
	public static JButton timerStop;
	public static JButton timerGo;
	public static JPanel sliderPanel;
	public static int studentCounter;
	public static int facultyCounter;
	static javax.swing.Timer timer;



	/**
	 * Creates the window and adds the two panels.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String[] args) {
		SimU window = new SimU();
		
		window.setSize(1150, 700);
		window.setResizable(false);
		window.setTitle("SimU - A Simulated University Game");
		

		JMenuBar menubar  = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menubar.add(fileMenu);
		
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem load = new JMenuItem("Load");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(newGame);
		fileMenu.addSeparator();
		fileMenu.add(load);
		fileMenu.add(save);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		newGame.addActionListener(window.new NewgameListener());
		load.addActionListener(window.new LoadListener());
		save.addActionListener(window.new SaveListener());
		exit.addActionListener(window.new ExitListener());
		window.setJMenuBar(menubar);
		
		JButton timerStop = new JButton();
		JButton timerGo = new JButton();
		timerGo.setText("Timer On");
		timerStop.setText("Timer Off");
		timerGo.addActionListener(window.new TimerGo());
		timerStop.addActionListener(window.new TimerStop());
		
		
		sliderPanel= new JPanel();
			
		
		//slider code
		slider = new JSlider(1,10,1);
		slider.setMajorTickSpacing(1);
		slider.setMinimum(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
//		timer = new javax.swing.Timer(30, window.new DrawingPanel.TimerListener());
		sliderPanel.add(slider);
		sliderPanel.add(timerStop);
		sliderPanel.add(timerGo);

		slider.addChangeListener(window.new SliderListener());
		

		window.buildings = new ArrayList<Building>();
		window.students = new ArrayList<Person>();
		window.faculty = new ArrayList<Person>();
		drawing = new DrawingPanel(window.buildings, window.students, window.faculty);
		ControlPanel controls = new ControlPanel(window.buildings);
		
		JScrollPane scrollPane = new JScrollPane(drawing);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		drawing.add(scrollPane);
		
		
		window.add(scrollPane, BorderLayout.CENTER);
		window.add(controls, BorderLayout.EAST);
		window.add(DrawingPanel.controlTimerPanel, BorderLayout.SOUTH);
		
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

	}
	
	/**
	 * Action Listener for TimerGo.
	 * 
	 * @param event
	 * 		resumes timer
	 */
	public class TimerGo implements ActionListener{

		/**
		 * Action Performed for TimerGo.
		 * 
		 * @param event
		 * 		resumes timer
		 */
		public void actionPerformed(ActionEvent event) {
			
			DrawingPanel.timer.start();

		}
		
	}
	/**
	 * Action Listener for TimerStop.
	 * 
	 * @param event
	 * 		stops timer
	 */	
	public class TimerStop implements ActionListener{

		/**
		 * Action Performed for TimerStop.
		 * 
		 * @param event
		 * 		stops timer
		 */
		public void actionPerformed(ActionEvent event) {
			
			DrawingPanel.timer.stop();
		}
		
	}	
	
	
	/**
	 * Change Listener for slider.
	 * 
	 * @param event
	 * 		changes speed of time
	 */
	public class SliderListener implements ChangeListener{
		/**
		 * State Changed for slider.
		 * 
		 * @param e
		 * 		changes speed of time
		 */
		public void stateChanged(ChangeEvent e){

//		JSlider source = (JSlider) e.getSource();
//		int fps = (int) source.getValue();
//		if (fps < 5) {
//			int delay = 1000 / fps;
//			timer.setDelay(delay);
//		}
//		else{
//			int delay = 100/fps;
//			timer.setDelay(delay);
//		}
//			
		int value = slider.getValue();
		DrawingPanel.timer.setDelay(10-value);
//		System.err.println(DrawingPanel.timer);
		
		}
	}
	
	
	/**
	 * Action Listener for "New Game".
	 *
	 * @param event
	 *		Clears buildings, persons, and faculty.
	 *
	 */
	public class NewgameListener implements ActionListener{
		/**
		 * Action Performed for "New Game".
		 *
		 * @param event
		 *		Clears buildings, persons, and faculty.
		 *
		 */
		public void actionPerformed(ActionEvent event){
			DrawingPanel.buildings.clear();
			DrawingPanel.persons.clear();
			DrawingPanel.faculty.clear();
			repaint();
		}
	}
	
	
	/**
	 * Action Listener for "Load".
	 *
	 * @param event
	 *		Ignored.
	 *
	 */
	public class LoadListener implements ActionListener{
		/**
		 * Action Performed for "Load".
		 *
		 * @param event
		 *		Ignored.
		 *
		 */
		public void actionPerformed(ActionEvent event){
			
//			ObjectInputStream ois = new ObjectInputStream (new FileInputStream(readFromThisFile));
//			
//			buildings.clear();
//			buildings = (ArrayList<Buildings>) ois.readObject();
//			
//			students.clear();
//			students (ArrayList<Person>) ois.readObject();
//			drawing.buildings = buildings;
//			drawing.persons = persons;
//			repaint();
			
			
//			JFileChooser chooser = new JFileCHooser("./");
//			int choice = chooser.showOpenDialog(null);
//			if (choice == JFileChooser.APPROVE_OPTION);
//			File readFromThisFile =  
//			Scanner scan = new Scanner(readFromThisFile);

			
		}
	}
	
	
	
	/**
	 * Action Listener for "Save".
	 *
	 * @param event
	 *		Ignored.
	 *
	 */
	public class SaveListener implements ActionListener{
		/**
		 * Action Performed for "Save".
		 *
		 * @param event
		 *		Ignored.
		 *
		 */
		public void actionPerformed(ActionEvent event){
//			
		
//			FileChooser chooser = new JFileChooser(./);
//			int choice = chooser.showSaveDIalog(null);
//			if (choice == JFileChooser.APPROVE_OPTION){
//				File writeToThisFIle = chooser.getSelectedFile();
//				ObjectOutputStream oos = new ObjectOutputStream();
//				
//			catch (Exception e){
//				
//			}
			
			
//			try {
//				JFileChooser chooser = new JFileChooser("./");
//				int choice = chooser.showSaveDialog(null);
//				if (choice == JFileChooser.APPROVE_OPTION) {
//					File writeToThisFile = chooser.getSelectedFile();
//					PrintWriter pw = new PrintWriter(writeToThisFile);
//					
			
			
//					//write buildings
//					pw.println(buildings.size();
//					for (int i = 0; i < buildings.size(); i++){
//						pw.println(buildings.get(i));
//					}
//					//write people
//					pw.println(students.size());
//					for (int i = 0; i < students.size(); i++) {
//						pw.println(students.get(i));
//					}
//					pw.close();
//				}
//			}
//			catch (Exception e){
//				
//			}
			
		}
	}
	
	
	/**
	 * Action Listener for "Exit".
	 *
	 * @param event
	 *		Exit Program.
	 *
	 */
	public class ExitListener implements ActionListener{
		/**
		 * Action Performed for "Exit".
		 *
		 * @param event
		 *		Exit program.
		 *
		 */
		public void actionPerformed(ActionEvent event){
			System.exit(0);
		}
	}


	/**
	 * Action Listener.
	 *
	 * @param e
	 *		Ignored.
	 *
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
