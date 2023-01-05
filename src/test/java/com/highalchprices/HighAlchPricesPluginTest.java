package com.highalchprices;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HighAlchPricesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HighAlchPricesPlugin.class);
		RuneLite.main(args);
	}
}