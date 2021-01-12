import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class GraphPanel extends JPanel
{
	public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
	} // Method to copy an array and remove an element, given its index.
	
	private static final long serialVersionUID = 2L;
	
	String fileName; // File to open to get the data to be plotted
	
	
	public GraphPanel()
	{
		super();
		fileName = null;
	}
	
	
	
	int mar = 60;
	int[] coordinates = {100, 0};
	
	private int getMax()
	{
		int max =- Integer.MAX_VALUE;
		for(int i = 0; i < coordinates.length; i++)
		{
			if(coordinates[i] > max)
				max = coordinates[i];
		}
		return max;
	}
	
	private double getMax_Y(String[] str)
	{
		double max =- Integer.MAX_VALUE;
		for(int i = 0; i < str.length; i++)
		{
			
			if(Double.parseDouble(str[i]) > max)
				max = Double.parseDouble(str[i]);
		}
		return max;
	}
	
	public void doWithSelectedDirectory(File selectedFile) 
	{
			try {
				if(!selectedFile.getName().endsWith(".png"))
					selectedFile = new File(selectedFile.getAbsolutePath()+".png");
				save(selectedFile);
				JOptionPane.showMessageDialog(GraphPanel.this, "Image successfully saved", "Information", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(GraphPanel.this, "An error occured during saving.", "Error", JOptionPane.ERROR_MESSAGE);
			}
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//Rectangle r = g.getClipBounds();
		//g.drawRect(r.x+1, r.y+1, r.width-2, r.height-1);
		
		if(fileName != null)
		{
			File file = new File(fileName);
			
			FileInputStream fis = null;
			
			try {
				fis = new FileInputStream(file);
			}
			
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			
			byte[] byteArray = new byte[(int)file.length()];
			
			try {
				fis.read(byteArray);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
			String data = new String(byteArray);
			String[] stringArray = data.split("\r\n");
			
			// System.out.println("Number of lines in the file are: "+stringArray.length);
			// System.out.println("Before arraycopy: " + Arrays.toString(stringArray));
			
			int index = 0;
			System.arraycopy(stringArray, index + 1, stringArray, index, stringArray.length - index - 1); // Remove first line (not useful)
			// System.out.println("After arraycopy: " + Arrays.toString(stringArray));
			
			String time_x[] = new String[stringArray.length]; // Create a new array to store the hours (x axis -> time)
			String values_y[] = new String[stringArray.length]; // Create a new array to store the values (y axis -> values)
			
			for(int i = 0; i < stringArray.length; i++)
			{
				String line = stringArray[i]; // Each line of the time axis array.
				String[] splittedline = line.split(",");
				String splittedline_withcalendar = splittedline[1];
				
				String[] splittedline_withcalendar_butsplitted = splittedline_withcalendar.split(" ");
				String splittedline_onlyhour = splittedline_withcalendar_butsplitted[1];
				time_x[i] = splittedline_onlyhour;
			}
			System.out.println(Arrays.toString(time_x));
			
			for(int i = 0; i < stringArray.length; i++)
			{
				String line = stringArray[i]; // Each line of the time axis array.
				String[] splittedline = line.split(",");
				String splittedline_withvalues = splittedline[2];
				values_y[i] = splittedline_withvalues;
			}
			System.out.println(Arrays.toString(values_y));
			
	
			Graphics2D g1 = (Graphics2D) g;
			
			g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // I don't know what is this. 
			// It looks better with this line of code lol
			
			// if(GraphPanel == black)
			
			g1.setPaint(Color.BLACK); // If you don't use this line of code, when you use save image the axes are not visible.
			
			int width = getWidth(); // Get res of the program.
			int height = getHeight();
			
			double a = (double) (width-2*mar)/values_y.length;
			double b = (double) (height-2*mar)/getMax_Y(values_y);
			// double b = (double) (height-2*mar)/values_y.length;
	
			
			// oooh, it was antialiasing
			
			//// Plot axes (y)
			int size = 13;
			Font f = new Font("Arial", Font.PLAIN, size);
			g1.setFont(f);
			g1.setPaint(Color.BLACK);
			FontMetrics fm = g.getFontMetrics(f);
			
			// 0
			g1.drawString("0", mar-size, height-mar);
			
			double y1_axis = getMax_Y(values_y)/4;
			double y2_axis = getMax_Y(values_y)/4*2;
			double y3_axis = getMax_Y(values_y)/4*3;
			double values_axis[] = {y1_axis, y2_axis, y3_axis, getMax_Y(values_y)};
			
			for(int i = 0; i<4; i++)
			{
				Rectangle2D bounds = fm.getStringBounds(Double.toString(values_axis[i]), g);
				int Iheight = (int)bounds.getHeight();
				int Ilength = (int)bounds.getWidth();
				int Middle_y = (int) Iheight/2;
				g1.setPaint(Color.BLACK);
				g1.drawString(Double.toString(values_axis[i]), mar-Ilength*11/10, (int) (height - mar - values_axis[i]*b + Middle_y));
				
				// For the ticks in the time axis.
				Stroke stroke_useless_lines = new BasicStroke(1.2f);
				g1.setStroke(stroke_useless_lines);
				g1.drawLine(mar, (int) (height - mar - values_axis[i]*b), mar+10, (int) (height - mar - values_axis[i]*b));
				
				
				// Horizontal lines
				float[] dashingPattern = {25f, 15f};
				Stroke stroke1 = new BasicStroke(0.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern, 2.0f);
				g1.setStroke(stroke1);
				g1.setPaint(Color.GRAY);
				g1.drawLine(mar, (int) (height - mar - values_axis[i]*b), width-mar, (int) (height - mar - values_axis[i]*b));
			}
			
			//// Plot axes (x)
			for(int i = 0; i < time_x.length; i++)
			{
				if (i%120 == 0)
				{
					FontMetrics fm2 = g.getFontMetrics(f);
					Rectangle2D bounds = fm2.getStringBounds(time_x[i], g);
					int Ilength = (int)bounds.getWidth();
					

					int Middle_x = (int) Ilength/2;
					int axis_x = (int) (mar + i*a - Middle_x);

					// For the ticks in the time axis.
					Stroke stroke_useless_lines = new BasicStroke(1.2f);
					g1.setStroke(stroke_useless_lines);

					if (i != 0)
						g1.drawLine(axis_x+Middle_x, height-mar, axis_x+Middle_x, height-mar-10);
					
					// For the vertical lines
					float[] dashingPattern = {25f, 15f};
					Stroke stroke1 = new BasicStroke(0.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern, 2.0f);
					g1.setStroke(stroke1);
					g1.setPaint(Color.GRAY);
					g1.drawLine(axis_x+Middle_x, height-mar, axis_x+Middle_x, mar);

					g1.setPaint(Color.BLACK);
					g1.drawString(time_x[i], axis_x, height-mar*5/7);
				}
			}
			

			
			Stroke stroke = new BasicStroke(1.8f);
			g1.setStroke(stroke);
			for(int i = 1; i < values_y.length; i++)
			{
				double value = Double.parseDouble(values_y[i-1]);
				double value2 = Double.parseDouble(values_y[i]);
				double x1 = mar + (i-1)*a;
				double y1 = height - mar - value*b;
				double x2 = mar + i*a;
				double y2 = height - mar - value2*b;
				
				// Solution #1 to negative values.
				if(y1 > (height-mar)) {
					y1 = height-mar;
				}
				if(y2 > (height-mar)) {
					y2 = height-mar;
				}
				
				// Colours in RGB
				if (value<50) {
                    g1.setPaint(new Color(240,205,3));
                }
                else if (value<100){
                    g1.setPaint(new Color(240,182,3));
                }
                else if (value<150){ 
                    g1.setPaint(new Color(240,153,3));
                }
                else if (value<200){
                    g1.setPaint(new Color(240,126,3));
                }
                else if (value<250){
                    g1.setPaint(new Color(240,90,3));
                }
                else if (value<300){
                    g1.setPaint(new Color(240,61,3));
                }
                else if (value<350){
                    g1.setPaint(new Color(240,25,3));
                }
                else if (value<400){
                    g1.setPaint(new Color(186,0,3));
                }
				
				
				g1.draw(new Line2D.Double(x1, y1, x2, y2));
				// g1.fill(new Ellipse2D.Double(x1, y1, 2, 2));
			}
			int scale_key = 22;
			int b2 = (int) (width/scale_key);
			int a2 = (int) ((600/100) * b2);
			
			
			// Plot key
			Toolkit t =Toolkit.getDefaultToolkit();
			Image key = t.getImage("key.png");
			g.drawImage(key, mar, 0, a2, b2, this);
			
			
			// Axes plotted at the final so the overwrite every line
			Stroke finalstroke = new BasicStroke(1f);
			g1.setStroke(finalstroke);
			g1.setPaint(Color.BLACK);
			g1.draw(new Line2D.Double(mar, mar, mar, height-mar)); 
			g1.draw(new Line2D.Double(mar, height-mar, width-mar, height-mar));
			
			double x = (double) (width-2*mar)/(coordinates.length - 1);
			double scale = (double) (height-2*mar)/getMax(); 
			
			g1.setPaint(Color.RED);
			
			for(int i = 0; i < coordinates.length; i++)
			{
				double x1 = mar+i*x;
				double y1 = height - mar - scale * coordinates[i];
				g1.fill(new Ellipse2D.Double(x1-2, y1-2, 4, 4));
			}
			
		}
	}
	
	public void save(File image) throws IOException{
		BufferedImage paintImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = paintImage.getGraphics();
		this.paintComponent(g);
		ImageIO.write(paintImage, "PNG", image);
	}
	
	public void doTemperatureGraph(String filename)
	{
		this.fileName = filename;
		this.repaint();
	}
}