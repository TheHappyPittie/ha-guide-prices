package com.highalchprices;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.game.ItemManager;
import net.runelite.api.ItemContainer;
import net.runelite.client.util.QuantityFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Text Changers",
		description = "A plugin to change various texts on the client",
		tags = {"high", "alch", "price", "checker", "text", "change", "time"}
)
public class HighAlchPricesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private HighAlchPricesConfig config;

	//Snippet taken from BankPlugin.java getHaPrice()
	private int getHaPrice(int itemId)
	{
		switch (itemId)
		{
			case ItemID.COINS_995:
				return 1;
			case ItemID.PLATINUM_TOKEN:
				return 1000;
			default:
				return itemManager.getItemComposition(itemId).getHaPrice();
		}
	}
	private String createHighAlchText(long alchValue)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" <col=ffffff>(HA: ");
		sb.append(QuantityFormatter.formatNumber(alchValue));
		sb.append(")</col>");
		return sb.toString();
	}

	public void addHighAlchText()
	{
		long alchValue = 0;
		//Guide Prices interfaces uses the trade container for items
		ItemContainer guidePricesItems = client.getItemContainer(90);
		Item[] items = guidePricesItems.getItems();
		for (Item i : items) {
			alchValue += getHaPrice(i.getId()) * i.getQuantity();
		}
		Widget textWidget = client.getWidget(464, 12);
		textWidget.setText(textWidget.getText() + createHighAlchText(alchValue));

	}
	private int calcHours(String s)
	{
		String days = "";
		String hours = "";
		Pattern pat = Pattern.compile("\\d+ ");
		Matcher mat = pat.matcher(s);
		while (mat.find())
		{
			if (days == ""){days = mat.group();}
			else if (hours == ""){hours = mat.group();}

		}
		int daysInt = Integer.parseInt(days.trim()) * 24;
		int hoursInt = Integer.parseInt(hours.trim());

		return(daysInt + hoursInt);
	}

	public void updatePlayTime()
	{
		Widget base = client.getWidget(712, 2);
		if (base.isHidden()) {return;} // the base widget isn't shown so abort here
		Widget[] children = base.getChildren();
		Widget textWidget = children[100];
		if (textWidget.isHidden() == false)
		{
			int updateHours = calcHours(textWidget.getText());
			textWidget.setText("Time Played: <col=0dc10d>" + updateHours + " Hours</col>");
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == 90 && config.highAlchPrices()) {
			addHighAlchText();
		}
	}
	@Subscribe
	public void onClientTick(ClientTick event)
	{
		if (config.updatePlayTime()){updatePlayTime();}
	}

	@Provides
	HighAlchPricesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HighAlchPricesConfig.class);
	}
}
