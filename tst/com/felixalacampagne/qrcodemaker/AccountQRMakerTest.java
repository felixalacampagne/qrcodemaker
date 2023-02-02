package com.felixalacampagne.qrcodemaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AccountQRMakerTest
{

	@Test
	void testMakeEPCFromAccount()
	{
	   AccountQRMaker accQR = new AccountQRMaker();
		String result;
		String epc;
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
				+ "BE14950106443283\n"
				+ "EUR1.00\n"
				+ "\n"
				+ "800025807031\n"
				+ "\n"
				+ "\n";

		epc = accQR.makeEPCFromAccount(tst);
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
				+ "BE14950106443283\n"
				+ "EUR1.01\n"
				+ "\n"
				+ "\n"
				+ "Any old text\n"
				+ "\n";
		epc = accQR.makeEPCFromAccount(tst);
      System.out.println("EPC equivalent is:\n" + epc);
      assertEquals(result, epc);

      tst ="Amount:        1,00\r\n"
            + "Date:\r\n"
            + "Account:        be14950106443283\r\n"
            + "Address:        Beobank MC\r\n"
            + "Communication:    +++ 800 / 0258 / 07031 +++\r\n"
            + "\r\n"
            + "";

      result = "BCD\n"
            + "002\n"
            + "1\n"
            + "SCT\n"
            + "\n"
            + "Beobank MC\n"
            + "BE14950106443283\n"
            + "EUR1.00\n"
            + "\n"
            + "800025807031\n"
            + "\n"
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
				+ "BE14950106443283\n"
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
				+ "BE14950106443283\n"
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
				+ "BE14950106443283\n"
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
				+ "BE14950106443283\n"
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
				+ "BE14950106443283\n"
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
            + "BE14950106443283\n"
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
            + "BE14950106443283\n"
            + "EUR100.34\n"
            + "\n"
            + "800025807031\n"
            + "\n"
            + "\n";
      epc = accQR.makeEPCFromAccount(tst);
      System.out.println("EPC equivalent is:\n" + epc);
      assertEquals(result, epc);

      tst ="Amount:        100,56\r\n"
            + "Date:\r\n"
            + "Account:        BE14   9501    0644    3283\r\n"
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
            + "BE14950106443283\n"
            + "EUR100.56\n"
            + "\n"
            + "800025807031\n"
            + "\n"
            + "\n";
      epc = accQR.makeEPCFromAccount(tst);
      System.out.println("EPC equivalent is:\n" + epc);
      assertEquals(result, epc);

   }

   @Test
   void testDisplayIBAN()
   {
   	String dispiban;
   	AccountTransaction atxn;

   	atxn = new AccountTransaction("test", "be12345678901234", "0,00", "test");
   	dispiban = atxn.displayIBAN();
   	assertEquals("BE12 3456 7890 1234", dispiban);

   	atxn = new AccountTransaction("test", "be1234567890123456", "0,00", "test");
   	dispiban = atxn.displayIBAN();
   	assertEquals("BE12 3456 7890 1234 56", dispiban);

   	atxn = new AccountTransaction("test", "be1234567", "0,00", "test");
   	dispiban = atxn.displayIBAN();
   	assertEquals("BE12 3456 7", dispiban);

   }

   @Test
   void testValidAmount()
   {
      String amt;

      amt = "100.20";
      assertTrue(AccountTransaction.isValidAmount(amt), amt);
      amt = "100.";
      assertTrue(AccountTransaction.isValidAmount(amt), amt);
      amt = "100";
      assertTrue(AccountTransaction.isValidAmount(amt), amt);
      amt = "100,20";
      assertTrue(AccountTransaction.isValidAmount(amt), amt);
      amt = "100,";
      assertTrue(AccountTransaction.isValidAmount(amt), amt);

      amt = "";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = ",01";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = ".01";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = "123.,01";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = "123,.01";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = "123..01";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = "123.0.1";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = "1,230.01";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);
      amt = "stachocash";
      assertFalse(AccountTransaction.isValidAmount(amt), amt);


   }
}
