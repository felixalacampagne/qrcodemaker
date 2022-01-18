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
			match = Pattern.compile("^(\\d{10,10})(\\d{2,2})$").matcher(commff);
			if(match.find())
			{
			   // According to 'febelfin.be' the structured communication is called 'OGM-VCS' and
			   // is a 10digit number plus 2digit modulo 97 of the number (97 if modulo is zero) making
			   // 12 digits in all. The "+++ / / +++"decoration should only be used for display - the bare number is
			   // used in electronic communications. So I think just providing the number should be enough
			   // maybe checking the modulo and putting it on the reference line will help. 
			   long nreference = 0;
			   String reference = match.group(1);
			   String modulo = match.group(2);

		      nreference += Long.valueOf(reference);
			   
		      nreference = nreference % 97;
		      nreference = nreference==0 ? 97 : nreference;
			   if(nreference == Long.valueOf( modulo))
			   {
   			   commst = String.format("%s%s", reference, modulo);
   			   commff = "";
			   }
			}
		}		
		
		// Name of recipient, account, amount, struct comm, freeformat comm
		String epctmpl = "BCD\n002\n1\nSCT\n\n%s\n%s\nEUR%s\n\n%s\n%s\nBeneToOrigIgnored\n";

		epc = String.format(epctmpl, recipname, account, amount, commst, commff);
		return epc;
	}	
}
