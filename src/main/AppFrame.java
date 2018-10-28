package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import invoice.NewOrder;

public class AppFrame {
	private static JPanel loginPanel;
	private static JFrame frame;
	
	AppFrame(){
		constructApp();
	}
	
	public static void constructApp() {
		frame = new JFrame();
		frame.setTitle("InVoicee");
		frame.setLayout(new BorderLayout( ));
		
		
		NewOrder loginHandler = new NewOrder();
		loginPanel = loginHandler.getNewOrderPanel();
		
		
		frame.add(loginPanel);
		
		frame.setVisible(true);
		frame.setSize(900, 600);
	}
	
	public static JFrame getParent ( ) {
		return frame;
	}
}
