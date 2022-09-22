package com.felixalacampagne.qrcodemaker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountQRMakerGUI extends JFrame
{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		AccountQRMakerGUI gui = new AccountQRMakerGUI();

	}

	class HistoryItem
	{
		int idx;
		String display;

		public HistoryItem(int idx, String display)
		{
			this.idx = idx;
			this.display = display;
		}

		@Override
		public String toString()
		{
			return display;
		}

		public int getIndex()
		{
			return idx;
		}
	}

	private static final String HISTORY_FILE = "accountqrmakergui.json";
	private List<AccountTransaction> transHistory;
	private final JTextField txtName = new JTextField();
	private final JTextField txtIBAN = new JTextField();
	private final JTextField txtAmount = new JTextField();
	private final JTextField txtCommunication = new JTextField();
	private final JComboBox<HistoryItem> cmbHistory = new JComboBox<HistoryItem>();
	private final JButton btnShowQR = new JButton("Show QR");

	private ActionListener showQRAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			AccountTransaction txn = new AccountTransaction(txtName.getText(), txtIBAN.getText(),
					                                         txtAmount.getText(), txtCommunication.getText());
			addToTransactionHistory(txn);
			saveHistory();
			populateHistory();
			AccountQRMaker accQR = new AccountQRMaker(false);

			String qrcontent = accQR.makeEPCFromTransaction(txn);

	      SwingUtilities.invokeLater(new Runnable(){
	         @Override
				public void run() {
	         	accQR.setQRMessage(qrcontent);
	         }
	     });
		}
	};

	public AccountQRMakerGUI()
	{
		init();
		loadHistory();
		populateHistory();
	}

   private ActionListener historySelected = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
     		setFromHistory();
      }
   };


   public void setFromHistory()
   {
   	if(cmbHistory.getSelectedItem() != null)
   	{
   		HistoryItem item = (HistoryItem) cmbHistory.getSelectedItem();
	      if(item.getIndex() >= 0)
	      {
		      AccountTransaction txn = this.transHistory.get(item.getIndex());
		      this.txtName.setText(txn.getName());
		      this.txtIBAN.setText(txn.getIban());
		      this.txtCommunication.setText(txn.getCommunication());
	      }
   	}
   }

	protected void init()
	{
		this.setResizable(false);

		// .DISPOSE_ON_CLOSE: QRcode keeps application active);
		// .EXIT_ON_CLOSE means can't save history before exit so it's saved each time QR code is displayed
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		cmbHistory.addActionListener(historySelected);
		cmbHistory.setEditable(false);
		lbl = new JLabel("History:");
		lbl.setLabelFor(cmbHistory);
	   pnl = new JPanel();
	   bl = new BoxLayout(pnl,BoxLayout.X_AXIS);
	   pnl.setLayout(bl);
		pnl.add(lbl);
		pnl.add(Box.createRigidArea(new Dimension(53, 0)));
		pnl.add(cmbHistory);
		this.getContentPane().add(pnl, BorderLayout.CENTER);

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

	protected void addToTransactionHistory(AccountTransaction txn)
	{
//		transHistory = transHistory.stream()
//			.filter(h-> !(h.getIban().equals(txn.getIban())))
//			.collect(Collectors.toList());
//		transHistory.add(txn);
		// Creates a new List each time which is not likely to be a problem given the
		// likely number of elements but I find a bit ugly. stream() is not really
		// well suited to this.

//		// So maybe the old fashioned way doesn't look much better
//		int existing = -1;
//		for(int i = 0; i < transHistory.size(); i++)
//		{
//			if(transHistory.get(i).getIban().equals(txn.getIban()))
//			{
//				existing = i;
//				break;
//			}
//		}
//		if(existing >= 0)
//		{
//			transHistory.remove(existing);
//		}

		// Compromise, a bit of new and a bit of old
		Optional<AccountTransaction> oldtxn = transHistory.stream()
				.filter(h-> (h.getIban().equals(txn.getIban())))
				.findFirst();
		if(oldtxn.isPresent())
		{
			transHistory.remove(oldtxn.get());
		}


		transHistory.add(txn);
	}

	protected File getHistoryFile()
	{
		File file = new File(new File(System.getProperty("user.home"), "felixalacampagne"), HISTORY_FILE);
		try
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	protected void saveHistory()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			mapper.writerWithDefaultPrettyPrinter().writeValue(getHistoryFile(), transHistory);
		}
		catch (StreamWriteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DatabindException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadHistory()
	{
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTransaction> history = new ArrayList<AccountTransaction>();
		try
		{
			history = mapper.readValue(getHistoryFile(), new TypeReference<List<AccountTransaction>>(){});
		}
		catch (StreamReadException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DatabindException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transHistory = history;
	}

	private void populateHistory()
	{
		this.cmbHistory.removeAllItems();
		this.cmbHistory.addItem(new HistoryItem(-1, ""));
		for(int i = 0; i < transHistory.size(); i++)
		{
			this.cmbHistory.addItem(new HistoryItem(i, transHistory.get(i).toString()));
		}
	}

}
