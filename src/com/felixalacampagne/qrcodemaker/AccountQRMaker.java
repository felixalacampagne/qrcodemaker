package com.felixalacampagne.qrcodemaker;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

public class AccountQRMaker extends QRCodeMakerGUI
{
	public static void main(String[] args)
	{
		AccountQRMaker accQR = new AccountQRMaker();
		
		String qrcontent = accQR.makeEPCFromClip();

//		QRCodeMakerGUI qrmk = new QRCodeMakerGUI();
//		String msg = Optional.of(qrmk.getClipboardText()).orElse("Please copy text for QR code onto clipboard");
      SwingUtilities.invokeLater(new Runnable(){
         public void run() {
         	accQR.setQRMessage(qrcontent);
         }
     });		
		
	}

	public String makeEPCFromClip()
	{
		String msg = getClipboardText();
		return makeEPCFromAccount(msg);
	}
	
	public String makeEPCFromAccount(String accountDetails)
	{
		String msg = accountDetails;
		String epc;
		String recipname="";
		String account="";
		String amount="";
		String commff="";
		String commst="";
		
		Matcher match;
		match = Pattern.compile("(?m)^Amount:\\s*(\\d*,\\d*)$").matcher(msg);
		if(match.find())
		{
			amount = match.group(1);
			amount = amount.replace(",", ".");
		}
		
		match = Pattern.compile("(?m)^Account:\\s+([A-Z]{2,2}\\d*)$").matcher(msg);
		if(match.find())
		{
			account = match.group(1);
			account = account.toLowerCase();
		}		
		
		match = Pattern.compile("(?m)^Address:\\s+(.*)$").matcher(msg);
		if(match.find())
		{
			recipname = match.group(1);
		}		
		
		match = Pattern.compile("(?m)^Communication:\\s+(.*)$").matcher(msg);
		if(match.find())
		{
			commff = match.group(1);
			match = Pattern.compile("^(\\d{3,3})(\\d{4,4})(\\d{5,5})$").matcher(commff);
			if(match.find())
			{
			   commst = String.format("+++ %s / %s / %s +++", match.group(1), match.group(2), match.group(3));
			   commff = "";
			}
		}		
		
		// Name of recipient, account, amount, struct comm, freeformat comm
		String epctmpl = "BCD\n002\n1\nSCT\n\n%s\n%s\nEUR%s\n\n%s\n%s\nBeneToOrigIgnored\n";

		epc = String.format(epctmpl, recipname, account, amount, commst, commff);
		return epc;
	}	
}
