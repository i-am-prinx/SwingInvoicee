package invoice;


import java.util.Random;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;


import javax.swing.Box;
import javax.swing.JTable;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.test.annotations.WrapToTest;
import java.io.File;
import java.io.IOException;

import main.AppFrame;
import net.miginfocom.swing.MigLayout;


public class NewOrder {
		
	private Random generator = new Random( 200 );
	
	private PdfDocument pdf;
	
	private JTable table, vTable;
	private JTextField itemQtyInput;
	private JComboBox <String> itemRating;
	private JComboBox <String> itemCategory;
	private DefaultTableModel tableModel, vModel;
	private JTextArea clientAddFld, itemDescInput;
	private JDialog insertPane, searchPane, viewItemPane;
	private JTextField clientNfld, clientPhnFld, searchFld;
	private JScrollPane cScrollPane, rScrollPane, vScrollPane;
	private JPanel newOrderPanel, iPanel, subPanel, sPanel, vPanel;
	private JScrollPane addressScrollPane, tableScrollPane, descScrollPane;
	private JLabel itemName, itemDesc, itemRate, itemQty, searchLbl, sRsltLbl;
	private JLabel clientNLbl, clientAddLbl, clientPhnLbl, title, id, noticeLbl;
	private JButton addBtn, insertBtn, searchBtn, findBtn, viewBtn, genInvoiceBtn;
	
	
	private Connection conn;
	private Statement query;
	private ResultSet result;
	private PreparedStatement stmt;
	
	
	private String[] itemCat = { 
		"shoes", 	"sleeves",  "ties", 	"shorts", 
		"trousers", "belts", 	"cliffs", 	"socks"
	};
	
	
	private String[] iRating = { "low", "medium", "high" };
	private int itemRealPrice, salesId, searchSalesId;
	private String s_id, c_name, c_num, c_add, i_id, i_cat, i_desc, i_rate, i_qty, i_price;
	
	
	public JPanel getNewOrderPanel() {
		newOrderPanel = new JPanel();
		newOrderPanel.setLayout(new BorderLayout());
		
		
		
		
		table = new JTable();
		tableModel = new DefaultTableModel(
			new Object[]{
				"Item ID", "Name", "Description", "Rate", "Quantity", "Price"
			}, 0
		);
		
		table.setModel(tableModel);
		tableScrollPane = new JScrollPane(table);
		
		
		
		
		connectDb();
		
		
		
		noticeLbl = new JLabel(
			"use the buttons below to perform corresponding actions"
		);
			
		
		addBtn = new JButton("add item");
		System.out.printf("%20s\t-- %20s\t-- %20s\n", "Name", "Phone", "Address");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				receiveItem();
			}
		});
		
		searchBtn = new JButton("search item");
		searchBtn.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e ) {
				searchItem( );
			}
		});
		
		
		
		
		subPanel = new JPanel(new MigLayout("al center center, wrap, gapy 20"));
		
		subPanel.add(noticeLbl);
		subPanel.add( addBtn, "split 2, gapleft 55");
		subPanel.add(searchBtn, "wrap, gapleft 20, gapbottom 20");
		
		
		
		newOrderPanel.add(tableScrollPane, BorderLayout.CENTER);
		newOrderPanel.add(subPanel, BorderLayout.SOUTH);
		
		
		
		return newOrderPanel;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void receiveItem( ) {
		insertPane = new JDialog(
			AppFrame.getParent(), "Insert Item", true 
		);
		
		insertPane.pack();
		
		
		itemName = new JLabel("Name");
		itemCategory = new JComboBox( itemCat );
		cScrollPane = new JScrollPane(itemCategory);
		
		
		itemDesc = new JLabel("Description");
		itemDescInput = new JTextArea(5, 20);
		
		
		itemRate = new JLabel("Rate");
		itemRating = new JComboBox(iRating);
		rScrollPane = new JScrollPane(itemRating);
		
		
		itemQty = new JLabel("Quantity");
		itemQtyInput = new JTextField(10);
	
		
		
		
		insertBtn = new JButton("insert item");
		insertBtn.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e ) {
				saveItem( );
			}
		});
		
		
		id = new JLabel("Sales ID:              " + salesId);
		clientNLbl = new JLabel("Client Name: ");
		title = new JLabel("Sales Order Creation");
		clientPhnLbl = new JLabel("Client Phone: ");
		clientAddLbl = new JLabel("Client Address: ");
		
		clientNfld = new JTextField(20);
		clientPhnFld = new JTextField(20);
		clientAddFld = new JTextArea(3, 40);
		addressScrollPane = new JScrollPane(clientAddFld);
		
		
		
		
		iPanel = new JPanel( );
		iPanel.setLayout(new MigLayout());
		
		iPanel.add(id, "wrap");
		iPanel.add(clientNLbl, "split 2");
		iPanel.add(clientNfld, "gapleft 18, wrap");
		
		iPanel.add(clientPhnLbl, "split 2");
		iPanel.add(clientPhnFld, "gapleft 17, wrap");
		
		iPanel.add(clientAddLbl, "split 2");
		iPanel.add(addressScrollPane, "wrap, gapbottom 30");
		iPanel.add(
			new JLabel("Item Details"), 
			"gapleft 200, gapbottom 20, wrap"
		);
		
		
		iPanel.add(itemName, "split 2");
		iPanel.add(cScrollPane, "gapleft 35, wrap, gapbottom 5");
		
		iPanel.add(itemDesc, "split 2, span 2 2");
		descScrollPane = new JScrollPane( itemDescInput );
		iPanel.add(descScrollPane, "grow");
		
		
		iPanel.add(itemRate, "gapleft 20, gaptop 10, split 2");
		iPanel.add(rScrollPane, "wrap, gapleft 30");
		
		iPanel.add(itemQty, "split 2, gapleft 20");
		iPanel.add(itemQtyInput, "wrap, gapleft 10");
		
		
		iPanel.add(insertBtn, "gaptop 30, gapbottom 20, gapleft 312");
		
		
		insertPane.add(iPanel);
		
		insertPane.pack();
		insertPane.setVisible(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	private void connectDb() {
		try {
			conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/invoice?zeroDateTimeBehavior=CONVERT_TO_NULL",
				"dbtest", "test12345"
			);
			System.out.println("Connected successfully");
		}
		
		catch(SQLException err ) {
			System.out.println("An error occured while connecting to database\n" + err);;
		}
		
		
		try {
			query = conn.createStatement();
			System.out.println("Query object created successfully");
		}
		catch(SQLException err) {
			System.out.println("couldn't create statement object");
		}
		
		
		
		try {
			result = query.executeQuery(
				"Select itemid, itemcat, itemdesc, itemrate," +
				"itemqty, itemprice, salesid From sales_item"
			);
			System.out.println("Result Object created successfully");
		}
		catch(SQLException err) {
			System.out.println("Encountered error while executing query");
		}
		
		
		
		try {
			while(result.next()) {
				String itemid = result.getString("itemid");
				String itemcat = result.getString("itemcat");
				String itemdesc = result.getString("itemdesc");
				String itemrate = result.getString("itemrate");
				String itemqty = result.getString("itemqty");
				String itemprice = result.getString("itemprice");
				String salesid = result.getString("salesid");
				
				System.out.println(itemid + "   " + itemcat);
				
				salesId = Integer.parseInt(salesid);
				
				tableModel.addRow(
					new Object[] {
							itemid,
							itemcat,
							itemdesc,
							itemrate,
							itemqty,
							itemprice
					}
				);
			}
			
			System.out.println("item retrieved successfully");
		}
		catch(SQLException err) {
			System.out.println("Couldn't retrieve resultset from result object\n" + err);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void searchItem ( ) {
		searchPane = new JDialog(AppFrame.getParent(), "Search Item", true);
		searchPane.pack();
		
		findBtn = new JButton("Find Item");
		viewBtn = new JButton("View Item");
		genInvoiceBtn = new JButton("Generate Invoice");
		
		
		searchLbl = new JLabel("Sales Id : ");
		searchFld = new JTextField(20);
		
		
		sPanel = new JPanel(new MigLayout());
		sPanel.add(searchLbl, "gaptop 30, split 3, gapleft 10");
		sPanel.add(searchFld, "gapleft 10");
		sPanel.add(findBtn, "gapleft 50, wrap, gapright 30, gapbottom 50");
		sPanel.add(viewBtn, "gapleft 80, split 2");
		sPanel.add(genInvoiceBtn, "gapleft 30, wrap");
		
		
		findBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				findItem();
			}
		});
		
		
		viewBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewItem( );
			}
		});
		
		genInvoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				generateSalesInvoice();
			}
		});
		
		searchPane.add(sPanel);
		searchPane.pack();
		searchPane.setVisible(true);
	}
	
	
	
	
	
	
	
	private void findItem() {

		int salesid = Integer.parseInt(searchFld.getText()), count = 0;
		
		try {
			result = query.executeQuery(
				"select * from sales_item where salesid = " + salesid
			);
			
			try {
				
				while (result.next()) {
					JOptionPane.showMessageDialog(
					  searchPane, "Record Found for salesid :    " + salesid  + "     "
				    );
					count++;
				}
				
				if ( count == 0) {
					JOptionPane.showMessageDialog(
					  searchPane, "No record found for sales id:    " + salesid 
				    );
				}
				
				
			}
			
			
			catch(SQLException err) {
				System.out.println(
					"Encountered Error while trying to output result (findItem)"
				);
			}
			
		}
		
		catch(SQLException err) {
			System.out.println("Encountered error while trying to query for sales id");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void viewItem() {
		
		viewItemPane = new JDialog(
			AppFrame.getParent(), "Search Details", true
		);
		viewItemPane.pack();
		
		
		vPanel = new JPanel(new BorderLayout());
		
		vModel = new DefaultTableModel(
			new Object [] {
				"sales id", "client name", "client number", "client address", "item id",
				"item category", "item description", "item rate", "item quantity", "price"
			}, 0
		);
		vTable = new JTable( vModel );
		
		
		
		JButton genInvoiceBtn = new JButton("Generate Invoice");
		
		genInvoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateSalesInvoice( );
			}
		});
		
		
		
		
		
		
		int salesid = Integer.parseInt(searchFld.getText()), count = 0;
		
		try {
			result = query.executeQuery(
				"select * from sales_item where salesid = " + salesid
			);
			
			System.out.println("Search for sales id completed successfully");
			
			try {
				while (result.next()) {
					vModel.addRow(
						new Object [] {
							result.getString("salesid"),
							result.getString("clientname"),
							result.getString("clientnum"),
							result.getString("clientadd"),
							result.getString("itemid"),
							result.getString("itemcat"),
							result.getString("itemdesc"),
							result.getString("itemrate"),
							result.getString("itemqty"),
							result.getString("itemprice")
						}
					);
					count++;
				}
				
				if ( count == 0) {
					JOptionPane.showMessageDialog(
						searchPane, "No record found for sales id:    " + salesid 
					);
				}
			}
			catch(SQLException err) {
				System.out.println(
					"Encountered Error while trying to output result (findItem)"
				);
			}
			
		}
		
		catch(SQLException err) {
			System.out.println(
				"Error Encountered while trying to retrieve resultset (viewItem) \n" + err
			);
		}
		
		vScrollPane = new JScrollPane(vTable);
		vPanel.add(vScrollPane, BorderLayout.CENTER);
		vPanel.add(genInvoiceBtn, BorderLayout.SOUTH);
		
		
		
		
		viewItemPane.add(vPanel);
		
		viewItemPane.setSize(900, 500);
		viewItemPane.setVisible(true);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void generateSalesInvoice () { 
	   
	   int salesid = Integer.parseInt(searchFld.getText()), count = 0;
		
		try {
			result = query.executeQuery(
				"select * from sales_item where salesid = " + salesid
			);
			
			try {
				
				while (result.next()) {
					s_id = result.getString("salesid");
					c_name = result.getString("clientname");
					c_num = result.getString("clientnum");
					c_add = result.getString("clientadd");
					i_id = result.getString("itemid");
					i_cat = result.getString("itemcat");
					i_desc = result.getString("itemdesc");
					i_rate = result.getString("itemrate");
					i_qty = result.getString("itemqty");
					i_price = result.getString("itemprice");
					JOptionPane.showMessageDialog(
					  searchPane, "Genrating Invoice"
				    );
					count++;
				}
				
				if ( count == 0) {
					JOptionPane.showMessageDialog(
					  searchPane, "No record found for sales id:    " + salesid 
				    );
				}
				
				
			}
			
			
			catch(SQLException err) {
				System.out.println(
					"Encountered Error while trying to output result (findItem)"
				);
			}
		}
		
		catch(SQLException err) {
			System.out.println("Encountered error while trying to query for sales id");
		}
		
		
		
		String path = "invoice.pdf"; 
		 
		try {
			pdf = new PdfDocument(new PdfWriter(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        Document document = new Document(pdf);
        document.add(new Paragraph("Hello World!"));
        document.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void itemCalculation() {
		String rate = (String) itemRating.getSelectedItem();
		String category = (String)itemCategory.getSelectedItem();
		int tempPriceCat = 0, tempPriceRate = 0;
		
		switch(rate) {
			case "low":
				tempPriceRate = 5;
				break;
			case "medium":
				tempPriceRate = 10;
				break;
			case "high":
				tempPriceRate = 15;
				break;
		}
		
		
		switch(category) {
			case "ties":
			case "cliffs":
			case "socks":
				tempPriceCat = 5;
				break;
			case "trousers":
			case "sleeves":
				tempPriceCat = 10;
				break;
			case "shorts":
			case "shoes":
			case "belts":
				tempPriceCat = 9;
				break;
		}
		itemRealPrice = (tempPriceCat * tempPriceRate);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	private void saveItem () {
		String clientName = clientNfld.getText();
		String clientNum = clientPhnFld.getText();
		String clientAdd = clientAddFld.getText();
		
		String itemName = (String)itemCategory.getSelectedItem();
		String itemRate = (String)itemRating.getSelectedItem();
		
		String itemDesc = itemDescInput.getText();
		String itemQty = itemQtyInput.getText();
		
		try {
			if ( clientName == "" && 	clientNum == "" && 
				 clientAdd   == "" &&	itemQty == "" ) 
			{
				openNotice();
			}
		
			else 
			{
				itemCalculation( );
				itemRealPrice *= Integer.parseInt(itemQty);
				
				
				int itemId = (int)Math.floor(generator.nextInt(200));
				System.out.printf(
					"%20s\t-- %20s\t-- %20s\n", clientName, clientNum, clientAdd
				);
				
				
				
				
				String newItemP = itemRealPrice + "";
				String newItemId = itemId + "";
				
				
				
				
				try {
					 stmt = conn.prepareStatement(
							"insert into sales_item ("+
							"clientname, clientnum, clientadd, "+
							"itemid, itemcat, itemdesc, itemrate, itemqty, itemprice) " +
							"values(?, ?, ?, ?, ?, ?, ?, ?, ?)"
					);
					 
					
					 
					stmt.setString(1, clientName);
					stmt.setString(2, clientNum);
					stmt.setString(3, clientAdd);
					stmt.setString(4, newItemId);
					stmt.setString(5, itemName);
					stmt.setString(6, itemDesc);
					stmt.setString(7, itemRate);
					stmt.setString(8, itemQty);
					stmt.setString(9, newItemP);
					
					
					stmt.executeUpdate();
					
					salesId += 1;
					
					System.out.println("inserted successfully");
					
				}
				catch(SQLException err) {
					System.out.println("Unable to insert records\n" + err);
				}
				
				
				
				
				
				tableModel.addRow(
						new Object[] {
							itemId,
							itemName, 
							itemDesc, 
							itemRate, 
							itemQty, 
							itemRealPrice
						}
					);
				
				
				
				
				resetFields( );
			}
		}
		
		catch(Exception err) {
			openNotice();
		}
	}
	
	
	
	
	private void openNotice( ) {
		JOptionPane.showMessageDialog(			
			insertPane, 
			"All required fields must be filled before an item can be inserted"
		);
	}
	
	
	
	
	
	private void resetFields() {
		clientNfld.setText("");
		clientPhnFld.setText("");
		clientAddFld.setText("");
		itemQtyInput.setText("");
		itemDescInput.setText("");
		itemRealPrice = 0;
		insertPane.dispose();
	}
}