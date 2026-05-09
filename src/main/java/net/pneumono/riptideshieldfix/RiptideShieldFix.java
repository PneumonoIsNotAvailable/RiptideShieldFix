package net.pneumono.riptideshieldfix;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RiptideShieldFix implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Riptide Shield Fix");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Riptide Shield Fix");
	}
}