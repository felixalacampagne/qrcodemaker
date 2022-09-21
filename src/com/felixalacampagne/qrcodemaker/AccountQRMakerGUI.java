package com.felixalacampagne.qrcodemaker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class AccountQRMakerGUI extends JFrame
{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		AccountQRMakerGUI gui = new AccountQRMakerGUI();

	}


	private final JTextField txtName = new JTextField();
	private final JTextField txtIBAN = new JTextField();
	private final JTextField txtAmount = new JTextField();
	private final JTextField txtCommunication = new JTextField();
	private final JButton btnShowQR = new JButton("Show QR");

	private ActionListener showQRAction = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e)
		{
			AccountTransaction txn = new AccountTransaction(txtName.getText(), txtIBAN.getText(), 
					                                         txtAmount.getText(), txtCommunication.getText());
			AccountQRMaker accQR = new AccountQRMaker(false);
			
			String qrcontent = accQR.makeEPCFromTransaction(txn);

	      SwingUtilities.invokeLater(new Runnable(){
	         public void run() {
	         	accQR.setQRMessage(qrcontent);
	         }
	     });			
		}
	};	
	
	public AccountQRMakerGUI()
	{
		init();
	}
	
	protected void init() 
	{
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.getContentPane().setPreferredSize(new Dimension(450, 140));
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));		
		
	   JPanel pnl;
	   BoxLayout bl;
		JLabel lbl;

		// So the boxes are there but it looks like shirt! (VB6 come back please!!!!)
		//
		// Textboxes are too high, no idea how to tell them to use a sensible size.
		// There is no border. Could brute force it by adding padding on left and right
		// but there must be an easier way.
		// Currently using hand made padding to vertically align the textboxes, there must
		// be a better way!
		lbl = new JLabel("Name:");  
		lbl.setLabelFor(txtName); 
		lbl.setToolTipText("Name of the account holder.");
	   pnl = new JPanel(); 
	   bl = new BoxLayout(pnl,BoxLayout.X_AXIS);
	   pnl.setLayout(bl);
		pnl.add(lbl);
		pnl.add(Box.createRigidArea(new Dimension(60, 0)));
		pnl.add(txtName);
		this.getContentPane().add(pnl, BorderLayout.CENTER);
		
		lbl = new JLabel("IBAN:");  
		lbl.setLabelFor(txtIBAN); 
		lbl.setToolTipText("IBAN code for account");	
	   pnl = new JPanel(); 
	   bl = new BoxLayout(pnl,BoxLayout.X_AXIS);
	   pnl.setLayout(bl);		
		pnl.add(lbl);
		pnl.add(Box.createRigidArea(new Dimension(66, 0)));
		pnl.add(txtIBAN);
		this.getContentPane().add(pnl, BorderLayout.CENTER);
		
		lbl = new JLabel("Amount:");  
		lbl.setLabelFor(txtAmount); 
		lbl.setToolTipText("Amount to be transferred, decimal separator is dot");
	   pnl = new JPanel(); 
	   bl = new BoxLayout(pnl,BoxLayout.X_AXIS);
	   pnl.setLayout(bl);
		pnl.add(lbl);
		pnl.add(Box.createRigidArea(new Dimension(49, 0)));
		pnl.add(txtAmount);
		this.getContentPane().add(pnl, BorderLayout.CENTER);
		
		lbl = new JLabel("Communication:");  
		lbl.setLabelFor(txtCommunication); 
		lbl.setToolTipText("Either free text OR a structured communication (not both)");	
	   pnl = new JPanel(); 
	   bl = new BoxLayout(pnl,BoxLayout.X_AXIS);
	   pnl.setLayout(bl);
		pnl.add(lbl);
		pnl.add(Box.createRigidArea(new Dimension(4, 0)));
		pnl.add(txtCommunication);
		this.getContentPane().add(pnl, BorderLayout.CENTER);
		
	   btnShowQR.setEnabled(true);
	   btnShowQR.addActionListener(showQRAction);
	   pnl = new JPanel(); 
	   bl = new BoxLayout(pnl,BoxLayout.X_AXIS);
	   pnl.setLayout(bl);		
		pnl.add(btnShowQR);
		this.getContentPane().add(pnl, BorderLayout.CENTER);		

		this.pack();
		this.setVisible(true);
	}
}
