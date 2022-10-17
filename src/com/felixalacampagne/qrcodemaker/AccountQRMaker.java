package com.felixalacampagne.qrcodemaker;

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
         @Override
			public void run() {
         	accQR.setQRMessage(qrcontent);
         }
     });

	}

	public AccountQRMaker() {
		super();
	}

	public AccountQRMaker(boolean exitonclose) {
		super(exitonclose);
	}

	public String makeEPCFromClip()
	{
		String msg = getClipboardText();
		return makeEPCFromAccount(msg);
	}


	public String makeEPCFromTransaction(AccountTransaction txn)
	{
		// Name of recipient, account, amount, communication
		String commst = "";
		String commff = txn.getCommunication();

		Matcher match = Pattern.compile("^(\\d{10,10})(\\d{2,2})$").matcher(commff);
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
         nreference = (nreference == 0) ? 97 : nreference;
         if(nreference == Long.valueOf( modulo))
         {
            commst = String.format("%s%s", reference, modulo);
            commff = "";
         }
      }

      // Name of recipient, account, amount, struct comm, freeformat comm
      //String epctmpl = "BCD\n002\n1\nSCT\n\n%s\n%s\nEUR%s\n\n%s\n%s\nBeneToOrigIgnored\n";
      String epctmpl = "BCD\n002\n1\nSCT\n\n%s\n%s\nEUR%s\n\n%s\n%s\n\n";

      String epc = String.format(epctmpl, txn.getName(), txn.getIban(), txn.getAmount(), commst, commff);
      return epc;
	}

	public String makeEPCFromAccount(String accountDetails)
	{
		String msg = accountDetails;
		String epc;
		String recipname="";
		String account="";
		String amount="";
      String communication="";

		Matcher match;
		// There must be some whitespace between the label and the value. Originally
		// at least one space was required but something has changed and now only
		// tabs are present (might be a conversion done by the editor I was using for
		// testing). 'whitespace' also includes CR and LF which appears to work!
		// Allowing CRLF after the label might be a bad idea where empty values are
		// used as it might match upto the next label which will then be taken as the
		// value, which is not the idea! Luckily there is the concept of 'horizontal whitespace'
		// which does not include LF or CR (strangely, since CR is only a horizontal movement)
		match = Pattern.compile("(?m)^Amount:\\h+(\\d+([,.]\\d{0,2})?)$").matcher(msg);
		if(match.find())
		{
			amount = match.group(1);
			amount = amount.replace(",", ".");
			if(!amount.contains("."))
			{
				amount += ".00";
			}
			else
			{
				amount += "00";
				int dotpos = amount.indexOf(".");
				amount = amount.substring(0, dotpos+3);
			}
		}

		match = Pattern.compile("(?m)^Account:\\h+([a-zA-Z]{2,2}\\d*)$").matcher(msg);
		if(match.find())
		{
			account = match.group(1);
			account = account.toLowerCase();
		}

		match = Pattern.compile("(?m)^Address:\\h+(.*)$").matcher(msg);
		if(match.find())
		{
			recipname = match.group(1);
		}

		match = Pattern.compile("(?m)^Communication:\\h+(.*)$").matcher(msg);
      if(match.find())
      {
         communication = match.group(1);
      }

		AccountTransaction txn = new AccountTransaction(recipname, account, amount, communication);
		return makeEPCFromTransaction(txn);

	}
}
