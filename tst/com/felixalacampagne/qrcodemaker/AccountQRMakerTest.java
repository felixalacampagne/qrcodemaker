package com.felixalacampagne.qrcodemaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountQRMakerTest
{

	@Test
	void testMakeEPCFromAccount()
	{
		String tst ="Amount:        1,00\r\n"
				+ "Date:\r\n"
				+ "Account:        BE14950106443283\r\n"
				+ "Address:        Beobank MC\r\n"
				+ "Communication:    800025807031\r\n"
				+ "\r\n"
				+ "";
					
		AccountQRMaker accQR = new AccountQRMaker();
		String epc = accQR.makeEPCFromAccount(tst);
		System.out.println("EPC equivalent is:\n" + epc);
		
		tst ="Amount:        1,01\r\n"
            + "Date:\r\n"
            + "Account:        BE14950106443283\r\n"
            + "Address:        Beobank MC\r\n"
            + "Communication:    Any old text\r\n"
            + "\r\n"
            + "";
		
		epc = accQR.makeEPCFromAccount(tst);
      System.out.println("EPC equivalent is:\n" + epc);
	}

}
