package com.highalchprices;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface HighAlchPricesConfig extends Config
{

	@ConfigItem(
		keyName = "highAlchPrices",
		name = "Show High Alch Values: ",
		description = "Shows the total High-Alch value of all items in the price checker",
        position = 1
	)
	default boolean highAlchPrices(){return true;}

    @ConfigItem(
        keyName = "updatePlayTime",
        name = "Update playtime: ",
        description = "Changes the playtime shown from hours and days to just hours.",
        position = 2
    )
    default boolean updatePlayTime(){return false;}
}
