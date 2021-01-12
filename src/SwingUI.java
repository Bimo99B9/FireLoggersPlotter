import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;



class SwingUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	JButton openFileButton;
	JButton saveImageButton;
	
	JPanel mainPanel;
	GraphPanel graphPanel;
	
	
	SwingUI()
	{
		openFileButton = new JButton("Open file"); //Add button as an event listener
		openFileButton.addActionListener(this);
		
		saveImageButton = new JButton("Save as PNG"); //Add button as an event listener
		saveImageButton.addActionListener(this);
		
		mainPanel = new JPanel(new BorderLayout(1, 1)); //Create panel and insert buttons
		mainPanel.add(BorderLayout.SOUTH, openFileButton);
		mainPanel.add(BorderLayout.WEST, saveImageButton);
		
		graphPanel = new GraphPanel();
		graphPanel.setPreferredSize(new Dimension(1280, 720)); //Specify layout manager and background color
		graphPanel.setBackground(Color.white);
		
		mainPanel.add(BorderLayout.NORTH, graphPanel);
		
		getContentPane().add(mainPanel); //Add label and button to panel
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if(source.equals(openFileButton))
		{
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
			
			fc.setFileFilter(filter);
			
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				String filename = fc.getSelectedFile().getAbsolutePath();
				graphPanel.doTemperatureGraph(filename);
			}	
		}
		
		else if(source.equals(saveImageButton))
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to save");
			
			int userSelection = fileChooser.showSaveDialog(mainPanel);
			
			if (userSelection == JFileChooser.APPROVE_OPTION) {
			    File fileToSave = fileChooser.getSelectedFile();
			    graphPanel.doWithSelectedDirectory(fileToSave);
			    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
			}
		}
	}
	
	
	
	
	public static void main(String[] args) throws Exception
	{
		SwingUI frame = new SwingUI(); //Create top-level frame
		frame.setTitle("FireLoggers Plotter 🔥");
		
		WindowListener l = new WindowAdapter() //This code lets you close the window
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		};
		
		frame.addWindowListener(l); //This code lets you see the frame
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	
	}
}