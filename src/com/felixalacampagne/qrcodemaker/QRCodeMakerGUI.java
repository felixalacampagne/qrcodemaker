package com.felixalacampagne.qrcodemaker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class QRCodeMakerGUI
{
	final JFrame frame = new JFrame();
	ImageIcon mQRImg;
	JLabel lblBackground;

	
	public QRCodeMakerGUI()
	{
		frame.setUndecorated (true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		
		MouseListener ml = new MouseAdapter()
		{
	
	        public void mouseClicked(MouseEvent e) 
	        {
	      	  if(e.getClickCount() >= 2)
	      	  {
	        			frame.setVisible(false);
	        			frame.dispose();
	        			System.exit(0);
	      	  }
	        }
	    };		
	    frame.addMouseListener(ml);
	}
	
	public void setQRMessage(String msg)
	{
		QRCodeMaker qrmk = new QRCodeMaker();
		mQRImg = qrmk.createQRImageIcon(msg);
		drawQRImage();
	}
	
	public void drawQRImage()
	{
		frame.getContentPane().removeAll();

		if(mQRImg != null)
		{
			int overflow = 0; //20;
			// Need the application frame to adjust it's size to display the whole QR code and
			// only the QR code whether or not there is a application title bar.
			// As ever getting Java to create components with the correct sizes is completely
			// hit and miss but this seems to work both with and without the application title bar.

			lblBackground = new JLabel(mQRImg);
			lblBackground.setLayout(new FlowLayout());
			lblBackground.setMinimumSize(new Dimension(mQRImg.getIconWidth(), mQRImg.getIconHeight()+overflow));
			lblBackground.setPreferredSize(lblBackground.getMinimumSize());
			lblBackground.setMaximumSize(lblBackground.getMinimumSize());
			lblBackground.setBounds(0,0,mQRImg.getIconWidth(),mQRImg.getIconHeight());
			
			JPanel lpane = new JPanel();
			lpane.setBounds(0,0,mQRImg.getIconWidth(), mQRImg.getIconHeight()+overflow);
			
			lpane.add(lblBackground, BorderLayout.CENTER);
//			lpane.add(app, JLayeredPane.PALETTE_LAYER);

			frame.add(lpane);
//			app.setLocation(100,0);
			lblBackground.setLocation(0,overflow);

		}
		else
		{
			lblBackground = new JLabel("No Image");
		}

		frame.pack();
		frame.setVisible(true);

	}

	public String getClipboardText()
	{
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
	   Transferable t = c.getContents(this);
	   String cliptext = null;
		try
		{
			DataFlavor flavor = DataFlavor.stringFlavor; // getTextPlainUnicodeFlavor();
			if(t.isDataFlavorSupported(flavor))
			{
				cliptext =  (String) t.getTransferData(flavor);
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return cliptext;
	}
	
	
	public static void main(String[] args)
	{
		QRCodeMakerGUI qrmk = new QRCodeMakerGUI();
		String msg = Optional.of(qrmk.getClipboardText()).orElse("Please copy text for QR code onto clipboard");
      SwingUtilities.invokeLater(new Runnable(){
         public void run() {
         	qrmk.setQRMessage(msg);
         }
     });		
		
	}	
}
