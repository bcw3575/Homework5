import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Contains radio buttons to select what you want to draw and some JTextFields
 * displaying how much area is contained by each building type.
 * 
 * @author Brian Walker
 * 
 */
public class ControlPanel extends JPanel implements ItemListener {

	public static final int academicBuilding = 0;
	public static final int administrativeBuilding = 1;
	public static final int gym = 2;
	public static final int studentCenter = 3;
	public static final int library = 4;
	public static final int greenSpace = 5;
	public static final int water = 6;
	public static final int footballStadium = 7;
	public static final int athleticVenue = 8;
	public static final int residenceHalls = 9;
	public static final int sidewalks = 10;
	public static final int student = 11;
	public static final int faculty = 12;

	public static final String[] toolStrings = { "Academic", "Administrative",
			"Gymnasium", "Student Center", "Library", "Green Space", "Water",
			"Football Stadium", "Athletic Fields", "Residence Halls",
			"Sidewalks", "Student", "Faculty" };

	public static int drawingTool;
	public static int numberOfTools = toolStrings.length;

	public JRadioButton[] tools;
	public static JTextField[] areas;
	ArrayList<Building> buildings;
	

	/**
	 * Add the radio buttons and the JTextFields.
	 * 
	 * @param builds
	 *            The list of buildings.
	 */
	public ControlPanel(ArrayList<Building> builds) {
		buildings = builds;
		setPreferredSize(new Dimension(200, 600));
		setBorder(BorderFactory.createTitledBorder("Controls"));
		setLayout(new GridLayout(2, 1));

		JPanel toolPanel = new JPanel();
		toolPanel.setBorder(BorderFactory.createTitledBorder("Drawing Tools"));
		ButtonGroup toolGroup = new ButtonGroup();
		toolPanel.setLayout(new GridLayout(numberOfTools, 1));
		tools = new JRadioButton[numberOfTools];
		for (int i = 0; i < toolStrings.length; i++) {
			tools[i] = new JRadioButton(toolStrings[i]);
			tools[i].addItemListener(this);
			toolPanel.add(tools[i]);
			toolGroup.add(tools[i]);

		}
		tools[0].setSelected(true);

		add(toolPanel);

		JPanel areaPanel = new JPanel();
		areaPanel.setLayout(new GridLayout(numberOfTools, 1));
		areaPanel.setBorder(BorderFactory.createTitledBorder("Areas"));
		areas = new JTextField[numberOfTools];
		for (int i = 0; i < toolStrings.length; i++) {
			areas[i] = new JTextField(toolStrings[i] + ": 0");
			areas[i].setEditable(false);
			areaPanel.add(areas[i]);
		}
		add(areaPanel);

	}

	/**
	 * When a radio button is selected, update "drawingTool" variable
	 * appropriately.
	 * 
	 * @param radioButtonEvent
	 *            The item event associated with the radio button.
	 * 
	 */
	public void itemStateChanged(ItemEvent radioButtonEvent) {
		JRadioButton whichButton = (JRadioButton) radioButtonEvent.getSource();

		if (whichButton.isSelected()) {
			String text = whichButton.getText();
			
			for (int i = 0; i < toolStrings.length; i++) {
				if (text.equalsIgnoreCase(toolStrings[i])) {
					drawingTool = i;
					return;
				}
			}
		}

	}
}
