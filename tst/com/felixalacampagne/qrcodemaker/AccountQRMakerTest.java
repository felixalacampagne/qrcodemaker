package com.felixalacampagne.qrcodemaker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AccountQRMakerTest
{

	@Test
	void testMakeEPCFromAccount()
	{
		String result;
		String tst ="Amount:        1,00\r\n"
				+ "Date:\r\n"
				+ "Account:        be14950106443283\r\n"
				+ "Address:        Beobank MC\r\n"
				+ "Communication:    800025807031\r\n"
				+ "\r\n"
				+ "";

		result = "BCD\n"
				+ "002\n"
				+ "1\n"
				+ "SCT\n"
				+ "\n"
				+ "Beobank MC\n"
				+ "be14950106443283\n"
				+ "EUR1.00\n"
				+ "\n"
				+ "800025807031\n"
				+ "\n"
				+ "\n";

		AccountQRMaker accQR = new AccountQRMaker();
		String epc = accQR.makeEPCFromAccount(tst);
		System.out.println("EPC equivalent is:\n" + epc);
		assertEquals(result, epc);

		tst ="Amount:        1,01\r\n"
            + "Date:\r\n"
            + "Account:        BE14950106443283\r\n"
            + "Address:        Beobank MC\r\n"
            + "Communication:    Any old text\r\n"
            + "\r\n"
            + "";
		result = "BCD\n"
				+ "002\n"
				+ "1\n"
				+ "SCT\n"
				+ "\n"
				+ "Beobank MC\n"
				+ "be14950106443283\n"
				+ "EUR1.01\n"
				+ "\n"
				+ "\n"
				+ "Any old text\n"
				+ "\n";
		epc = accQR.makeEPCFromAccount(tst);
      System.out.println("EPC equivalent is:\n" + epc);
      assertEquals(result, epc);
	}

	@Test
	void testMakeEPCFromAccountAmount()
	{
		String tst;
		String epc;
		String result;
		AccountQRMaker accQR = new AccountQRMaker();

		tst ="Amount:        100\r\n"
				+ "Date:\r\n"
				+ "Account:        BE14950106443283\r\n"
				+ "Address:        Beobank MC\r\n"
				+ "Communication:    800025807031\r\n"
				+ "\r\n"
				+ "";

		result = "BCD\n"
				+ "002\n"
				+ "1\n"
				+ "SCT\n"
				+ "\n"
				+ "Beobank MC\n"
				+ "be14950106443283\n"
				+ "EUR100.00\n"
				+ "\n"
				+ "800025807031\n"
				+ "\n"
				+ "\n";
		epc = accQR.makeEPCFromAccount(tst);
		System.out.println("EPC equivalent is:\n" + epc);
		assertEquals(result, epc);

		tst ="Amount:        100,2\r\n"
				+ "Date:\r\n"
				+ "Account:        BE14950106443283\r\n"
				+ "Address:        Beobank MC\r\n"
				+ "Communication:    any old garbage\r\n"
				+ "\r\n"
				+ "";
		result = "BCD\n"
				+ "002\n"
				+ "1\n"
				+ "SCT\n"
				+ "\n"
				+ "Beobank MC\n"
				+ "be14950106443283\n"
				+ "EUR100.20\n"
				+ "\n"
				+ "\n"
				+ "any old garbage\n"
				+ "\n";
		epc = accQR.makeEPCFromAccount(tst);
		System.out.println("EPC equivalent is:\n" + epc);
		assertEquals(result, epc);

		tst ="Amount:        100,\r\n"
				+ "Date:\r\n"
				+ "Account:        BE14950106443283\r\n"
				+ "Address:        Beobank MC\r\n"
				+ "Communication:    any old garbage\r\n"
				+ "\r\n"
				+ "";
		result = "BCD\n"
				+ "002\n"
				+ "1\n"
				+ "SCT\n"
				+ "\n"
				+ "Beobank MC\n"
				+ "be14950106443283\n"
				+ "EUR100.00\n"
				+ "\n"
				+ "\n"
				+ "any old garbage\n"
				+ "\n";
		epc = accQR.makeEPCFromAccount(tst);
		System.out.println("EPC equivalent is:\n" + epc);
		assertEquals(result, epc);

		tst ="Amount:        100.3\r\n"
				+ "Date:\r\n"
				+ "Account:        BE14950106443283\r\n"
				+ "Address:        Beobank MC\r\n"
				+ "Communication:    any old garbage\r\n"
				+ "\r\n"
				+ "";
		result = "BCD\n"
				+ "002\n"
				+ "1\n"
				+ "SCT\n"
				+ "\n"
				+ "Beobank MC\n"
				+ "be14950106443283\n"
				+ "EUR100.30\n"
				+ "\n"
				+ "\n"
				+ "any old garbage\n"
				+ "\n";
		epc = accQR.makeEPCFromAccount(tst);
		System.out.println("EPC equivalent is:\n" + epc);
		assertEquals(result, epc);

		tst ="Amount:        100.34\r\n"
				+ "Date:\r\n"
				+ "Account:        BE14950106443283\r\n"
				+ "Address:        Beobank MC\r\n"
				+ "Communication:    any old garbage\r\n"
				+ "\r\n"
				+ "";
		result = "BCD\n"
				+ "002\n"
				+ "1\n"
				+ "SCT\n"
				+ "\n"
				+ "Beobank MC\n"
				+ "be14950106443283\n"
				+ "EUR100.34\n"
				+ "\n"
				+ "\n"
				+ "any old garbage\n"
				+ "\n";
		epc = accQR.makeEPCFromAccount(tst);
		System.out.println("EPC equivalent is:\n" + epc);
		assertEquals(result, epc);			
	}

   @Test
   void testMakeEPCFromAccountIBAN()
   {
      String tst;
      String epc;
      String result;
      AccountQRMaker accQR = new AccountQRMaker();

      tst ="Amount:        100,12\r\n"
            + "Date:\r\n"
            + "Account:        BE14950106443283\r\n"
            + "Address:        Beobank MC\r\n"
            + "Communication:    800025807031\r\n"
            + "\r\n"
            + "";

      result = "BCD\n"
            + "002\n"
            + "1\n"
            + "SCT\n"
            + "\n"
            + "Beobank MC\n"
            + "be14950106443283\n"
            + "EUR100.12\n"
            + "\n"
            + "800025807031\n"
            + "\n"
            + "\n";
      epc = accQR.makeEPCFromAccount(tst);
      System.out.println("EPC equivalent is:\n" + epc);
      assertEquals(result, epc);

      tst ="Amount:        100,34\r\n"
            + "Date:\r\n"
            + "Account:        BE14 9501 0644 3283\r\n"
            + "Address:        Beobank MC\r\n"
            + "Communication:    800025807031\r\n"
            + "\r\n"
            + "";

      result = "BCD\n"
            + "002\n"
            + "1\n"
            + "SCT\n"
            + "\n"
            + "Beobank MC\n"
            + "be14950106443283\n"
            + "EUR100.34\n"
            + "\n"
            + "800025807031\n"
            + "\n"
            + "\n";
      epc = accQR.makeEPCFromAccount(tst);
      System.out.println("EPC equivalent is:\n" + epc);
      assertEquals(result, epc);
      
      
   }
}
