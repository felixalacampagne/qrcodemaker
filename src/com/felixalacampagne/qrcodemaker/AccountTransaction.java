package com.felixalacampagne.qrcodemaker;

public class AccountTransaction
{
	private final String name;
	private final String iban;
	private final String amount;
	private final String communication;
	
	public AccountTransaction(String name, String iban, String amount, String communication)
	{
		this.name = name;
		this.iban = iban;
		this.amount = amount;
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
	
}
