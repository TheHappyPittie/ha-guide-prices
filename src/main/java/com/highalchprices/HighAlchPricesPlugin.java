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

@Slf4j
@PluginDescriptor(
	name = "High Alch Guide",
		description = "A plugin to add alch value to price checker",
		tags = {"high", "alch", "price", "checker"}
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

	private String createText(long alchValue)
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
		textWidget.setText(textWidget.getText() + createText(alchValue));

	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		int containerID = event.getContainerId();
		if (containerID == 90) {
			addHighAlchText();
		}
	}

	@Provides
	HighAlchPricesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HighAlchPricesConfig.class);
	}
}
