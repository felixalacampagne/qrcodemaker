package com.felixalacampagne.qrcodemaker;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccountTransaction
{
   private String iban;
   private String name;

   @JsonIgnore
   private String amount;
   private String communication;

   public static boolean isValidAmount(String amtstr)
   {
      return amtstr.matches("\\d+(\\.|,){0,1}\\d{0,2}");
   }



   protected AccountTransaction()
   {
      // For JSON
   }

   public AccountTransaction(String name, String iban, String amount, String communication)
   {
      this.name = name;
      this.iban = normalizeIBAN(iban);
      this.amount = normalizeAmount(amount);
      this.communication = communication;

   }

   public String getCommunication()
   {
      return communication;
   }

   public String getAmount()
   {
      return amount;
   }

   public String getIban()
   {
      return iban;
   }

   public String getName()
   {
      return name;
   }

   @Override
   public String toString()
   {
      return name + " : " + displayIBAN() + " : " + communication;
   }

   protected String normalizeIBAN(String iban)
   {
      // Belfius rejected the account with lower case country code but
      // accepted it with upper case. So use upper case...
      iban = iban.toUpperCase().replaceAll(" ", "");
      return iban;
   }

   protected String normalizeAmount(String amt)
   {
      if(amt.isEmpty())
         return amt;

      String amount = amt.replace(",", ".");
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
      return amount;
   }

   public String displayIBAN()
   {
      return iban.replaceAll("([A-Za-z\\d]{4,4})", "$0 ").toUpperCase().trim();
   }

}
