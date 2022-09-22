package com.felixalacampagne.qrcodemaker;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccountTransaction
{
	private String iban;
	private String name;

	@JsonIgnore
	private String amount;
	private String communication;

	protected AccountTransaction()
	{
		// For JSON
	}

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

	@Override
	public String toString()
	{
		return name + " : " + iban + " : " + communication;
	}

}
