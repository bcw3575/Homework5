import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The user can draw academic buildings and such in this panel.
 * 
 * @author Brian Walker
 * 
 */
public class DrawingPanel extends JPanel implements MouseListener,
MouseMotionListener {

	private int lastX = 0;
	private int lastY = 0;

	public static ArrayList<Building> buildings;
	public static ArrayList<Person> persons;
	public static ArrayList<Person> faculty;

	public static javax.swing.Timer timer;
	public static JSlider sliderZoom;
	Calendar calendar;
	public static double scale = 0.5;
	public static int MAX_HEIGHT = 600;
	public static int MAX_WIDTH = 900;
	public static double value = 10;
	
	static final int FPS_MIN = 1;
	static final int FPS_MAX = 10;
	static final int FPS_INIT = 5;
	static JPanel controlTimerPanel;
	

	/**
	 * Sets the size, adds a border, and the mouse listeners.
	 * 
	 * @param builds
	 *            The list of all the buildings.
	 * @param pers
	 *            The list of all the persons.
	 * @param fac
	 * 			  The list of all the faculty.
	 */
	public DrawingPanel(ArrayList<Building> builds, ArrayList<Person> pers, ArrayList<Person> fac) {
		buildings = builds;
		persons = pers;
		faculty = fac;

		//slider zoomer
		sliderZoom = new JSlider(1,5,5);
		sliderZoom.addChangeListener(new SliderZoomListener());
		
		add(sliderZoom);
		
		

		addMouseListener(this);
		addMouseMotionListener(this);

		calendar = Calendar.getInstance();

		timer = new javax.swing.Timer(30, new TimerListener());
		timer.start();
		
		controlTimerPanel = new JPanel();
		controlTimerPanel.setBorder(BorderFactory.createTitledBorder(""));

		JButton startButton = new JButton("Start Timer");
		JButton stopButton = new JButton("Stop Timer");
		startButton.addActionListener(new StartListener());
		stopButton.addActionListener(new StopListener());
		controlTimerPanel.add(startButton);
		controlTimerPanel.add(stopButton);
		controlTimerPanel.add(sliderZoom);
		

		JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, FPS_MIN,
				FPS_MAX, FPS_INIT);
		controlTimerPanel.add(framesPerSecond);
		framesPerSecond.addChangeListener(new SliderListener());

		// Turn on labels at major tick marks.
		framesPerSecond.setMajorTickSpacing(1);
		framesPerSecond.setMinorTickSpacing(1);
		framesPerSecond.setPaintTicks(true);
		framesPerSecond.setPaintLabels(true);

	}

	/**
	 * Draw each building.
	 * 
	 * @param g
	 *            The graphics component of this panel.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	
		Graphics2D g2 = (Graphics2D) g;

		
        int w = 900;
        int h = 600;
        
        // Translate used to make sure scale is centered
       // g2.translate(w/2, h/2);
    	
       // System.err.println(value);
       // AffineTransform backup = g2.getTransform();
        g2.scale(value, value);
        g2.drawImage(SimU.backgroundCampus.getImage(), 0,0,SimU.backgroundCampus.getIconWidth(),
	    		 SimU.backgroundCampus.getIconHeight(),this);
        //super.paint(g);
       // g2.translate(-w/2, -h/2);
        
		
		
		
		for (Building b : buildings) {
			b.draw(g2);
		}
		for (Person p : persons) {
			p.draw(g2);
		}
		for (Person f : faculty) {
			f.draw(g2);
		}
		
		// Translate used to make sure scale is centered
        //g2.translate(w/2, h/2);
        g2.scale(1.0/(value), 1.0/(value));  // Scale back to normal to draw the date
        //g2.translate(-w/2, -h/2);
       // g2.setTransform(backup);

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"E, y-M-d 'at' h:m:s a z");
		String dateAndTime = dateFormatter.format(calendar.getTime());
		g2.drawString(dateAndTime, 200, 50);
		
	     
	}

	/**
	 * Not used.
	 * 
	 * @param theMouseEvent
	 *            The mouse event.
	 */
	public void mouseDragged(MouseEvent theMouseEvent) {
		/* For now, this is left blank on purpose. */
		// No content.
	}

	/**
	 * Not used.
	 * 
	 * @param theMouseEvent
	 *            The mouse event.
	 */
	public void mouseMoved(MouseEvent theMouseEvent) {
		/* For now, this is left blank on purpose. */
		// No content.
	}

	/**
	 * Not used.
	 * 
	 * @param theMouseEvent
	 *            The mouse event.
	 */
	public void mouseClicked(MouseEvent theMouseEvent) {
		/* For now, this is left blank on purpose. */
		// No content.
	}

	/**
	 * Not used.
	 * 
	 * @param theMouseEvent
	 *            The mouse event.
	 */
	public void mouseEntered(MouseEvent theMouseEvent) {
		/* For now, this is left blank on purpose. */
		// No content.
	}

	/**
	 * Not used.
	 * 
	 * @param theMouseEvent
	 *            The mouse event.
	 */
	public void mouseExited(MouseEvent theMouseEvent) {
		/* For now, this is left blank on purpose. */
		// No content.
	}

	/**
	 * Save where the mouse is.
	 * 
	 * @param theMouseEvent
	 *            The mouse event.
	 */
	public void mousePressed(MouseEvent theMouseEvent) {
		lastX = (int)(theMouseEvent.getX()/value);
		lastY = (int)(theMouseEvent.getY()/value);
	}

	/**
	 * When the mouse is released, first figure out the dimensions of the
	 * rectangle that have been drawn. Then figure out what kind of building it
	 * is and create an object of that type. Add it to the list of buildings.
	 * Update the ControlPanel JTextField that contains the area for that kind
	 * of building.
	 * 
	 * If it is the right mouse button, search through the list of objects,
	 * seeing if this point is inside any of those buildings. If so, delete
	 * them. Update the area as needed.
	 * 
	 * @param theMouseEvent
	 *            The mouse event.
	 */
	public void mouseReleased(MouseEvent theMouseEvent) {

		int x = (int)(theMouseEvent.getX()/value);
		int y = (int)(theMouseEvent.getY()/value);

		if (x < lastX) { // swap
			int temp = x;
			x = lastX;
			lastX = temp;
		}
		if (y < lastY) { // swap
			int temp = y;
			y = lastY;
			lastY = temp;
		}

		// check for left mouse click

		if (!theMouseEvent.isMetaDown()) {
			Building building = null;
			Person student = null;
			Person faculty2 = null;



			if (ControlPanel.drawingTool == ControlPanel.academicBuilding) {
				building = new AcademicBuilding(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.library) {
				building = new Library(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.student) {
				student = new Student(x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.faculty){
				faculty2 = new Faculty(x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.residenceHalls) {
				building = new ResidenceHall(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.administrativeBuilding) {
				building = new AdministrativeBuilding(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.gym) {
				building = new Gymnasium(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.studentCenter) {
				building = new StudentCenter(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.greenSpace) {
				building = new GreenSpace(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.water) {
				building = new Water(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.footballStadium) {
				building = new FootballStadium(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.athleticVenue) {
				building = new AthleticFields(lastX, lastY, x, y);
			} else if (ControlPanel.drawingTool == ControlPanel.sidewalks) {
				building = new Sidewalks(lastX, lastY, x, y);
			} 


			if (student != null) {
				persons.add(student);
				SimU.studentCounter++;
			}

			if (faculty2 != null){
				faculty.add(faculty2);
				SimU.facultyCounter++;
			}


			if (building != null && !overlap(lastX,lastY,x,y)) {

				buildings.add(building);

				// recalculate area
				addArea(building);

			}

		}




		else { // right mouse, so delete whatever is under the mouse
			for (int i = 0; i < buildings.size(); i++) {
				Building b = buildings.get(i);
				Rectangle r = new Rectangle(b.getUpperX(), b.getUpperY(),
						b.width(), b.height());
				if (r.contains(x, y)) {
					buildings.remove(i);
					// remove area
					removeArea(b);

				}
			}

		}

		repaint();
	}
	
	/**
	 * 
	 * Determines if 2 Buildings intersect each other.
	 * 
	 * @param lastX
	 * 		lastX of Rectangle
	 * @param lastY
	 * 		lastY of Rectangle
	 * @param x
	 * 		x of Rectangle
	 * @param y
	 * 		y of Rectangle
	 * @return
	 * 		Boolean based on intersection of rectangles.
	 */
	public static boolean overlap(int lastX, int lastY, int x, int y){
		Rectangle r = new Rectangle(lastX, lastY, (x-lastX), (y-lastY));
		for (int i = 0; i < buildings.size(); i++) {
			Building b = buildings.get(i);
			Rectangle existing = new Rectangle(b.getUpperX(), b.getUpperY(), b.width(), b.height());
			Rectangle sidewalk1 = new Rectangle(415, 0, 70, 650);
			Rectangle sidewalk2 = new Rectangle(0, 265, 950, 60);
			if (r.intersects(existing) || r.intersects(sidewalk1) || r.intersects(sidewalk2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the appropriate area text when this building is added.
	 * 
	 * @param building
	 *            The building being added.
	 */
	public void addArea(Building building) {
		String areaString = ControlPanel.areas[ControlPanel.drawingTool]
				.getText();
		int indexOfColon = areaString.indexOf(":");
		String startOfString = areaString.substring(0, indexOfColon + 1);
		String restOfString = areaString.substring(indexOfColon + 1);
		restOfString = restOfString.trim();
		int area = Integer.parseInt(restOfString);
		int additionalArea = building.area();
		area += additionalArea;
		ControlPanel.areas[ControlPanel.drawingTool].setText(startOfString
				+ " " + area);
	}

	/**
	 * Updates the appropriate area text when this building is deleted.
	 * 
	 * @param building
	 *            The building being deleted.
	 */
	public void removeArea(Building building) {
		int lessArea = building.area();

		String areaString = ControlPanel.areas[building.theDrawingTool]
				.getText();
		int indexOfColon = areaString.indexOf(":");
		String startOfString = areaString.substring(0, indexOfColon + 1);
		String restOfString = areaString.substring(indexOfColon + 1);
		restOfString = restOfString.trim();
		int area = Integer.parseInt(restOfString);
		area -= lessArea;
		ControlPanel.areas[building.theDrawingTool].setText(startOfString + " "
				+ area);

	}

	/**
	 * This class updates the date and then updates each person.
	 * 
	 * @author Brian Walker
	 * 
	 */
	public class TimerListener implements ActionListener {

		/**
		 * The listener for the timer.
		 * 
		 * @param theTimerEvent
		 *            The action event.
		 */
		public void actionPerformed(ActionEvent theTimerEvent) {
			calendar.add(Calendar.MINUTE, 1);
			for (Person p : persons) {
				p.update(calendar);
			}
			for (Person p : faculty) {
				p.update(calendar);
			}
			repaint();

		}

	}
	public class SliderZoomListener implements ChangeListener{
		/**
		 * State Changed for slider.
		 * 
		 * @param event
		 * 		changes speed of time
		 */
		public void stateChanged(ChangeEvent event){

		value = sliderZoom.getValue();
//		value = value;
//		Dimension width = drawing.getPreferredSize();
//		width.setSize(value*16, value*9);
//		setPreferredSize(new Dimension(value*9, value*6));
		setPreferredSize(new Dimension((int) (value*900), (int)(value*600)));
		
		repaint();
		revalidate();
		
		
		}
	}
	public class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			int fps = (int) source.getValue();
			if (fps < 5) {
				int delay = 1000 / fps;
				timer.setDelay(delay);
			} else {
				int delay = 100 / fps;
				timer.setDelay(delay);

			}
		}

	}

	/**
	 * When the start button is clicked, the timer starts.
	 * 
	 * @param Button
	 *            listener that starts the timer.
	 */
	private class StartListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			timer.start();

		}

	}

	/**
	 * When the stop button is clicked, the timer stops.
	 * 
	 * @param Button
	 *            listener that stops the timer.
	 */
	private class StopListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			timer.stop();

		}

	}

}
