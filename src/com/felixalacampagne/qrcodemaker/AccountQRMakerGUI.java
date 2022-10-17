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
		      this.txtIBAN.setText(txn.displayIBAN());
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
		this.getContentPane().setPreferredSize(new Dimension(600, 140));
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
		
		// This helps the dropdown button stay in the frame. Now the list needs a horizontal scrollbar
		// which you would think would be automatic, but true half-assed nature of all Java implementations
		// it's actually a shirt load of extra stuff which is needed to enable the display of the content of the list
		// - forking typical (see commented SampleJComboBoxWithScrollBar below). 
		// That is definitely for another day!! 
		// For now make the window wider so most items will display fully
		cmbHistory.setPrototypeDisplayValue(new HistoryItem(1, ""));

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

//   class SampleJComboBoxWithScrollBar extends JComboBox {
//
//      SampleJComboBoxWithScrollBar() {
//          super();
//          this.addPopupMenuListener(this.getPopupMenuListener());
//          this.adjustScrollBar();
//      }
//
//      private void adjustPopupWidth() {
//          if (getItemCount() == 0) {
//              return;
//          }
//          Object comp = getUI().getAccessibleChild(this, 0);
//          if (!(comp instanceof JPopupMenu)) {
//              return;
//          }
//          JPopupMenu popup = (JPopupMenu) comp;
//          JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
//          Object value = getItemAt(0);
//          Component rendererComp = getRenderer().getListCellRendererComponent(new JList(), value, 0, false, false);
//          if (rendererComp instanceof JXTable) {
//              scrollPane.setColumnHeaderView(((JTable) rendererComp).getTableHeader());
//          }
//          Dimension prefSize = rendererComp.getPreferredSize();
//          Dimension size = scrollPane.getPreferredSize();
//          size.width = Math.max(size.width, prefSize.width);
//          scrollPane.setPreferredSize(size);
//          scrollPane.setMaximumSize(size);
//          scrollPane.revalidate();
//      }
//
//      private void adjustScrollBar() {
//          if (getItemCount() == 0) {
//              return;
//          }
//          Object comp = getUI().getAccessibleChild(this, 0);
//          if (!(comp instanceof JPopupMenu)) {
//              return;
//          }
//          JPopupMenu popup = (JPopupMenu) comp;
//          JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
//          scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
//          scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//      }
//
//      private PopupMenuListener getPopupMenuListener() {
//
//          return new PopupMenuListener() {
//
//              @Override
//              public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//                  adjustPopupWidth();
//              }
//
//              @Override
//              public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//              }
//
//              @Override
//              public void popupMenuCanceled(PopupMenuEvent e) {
//              }
//          };
//
//      }
//  }	
	
}
