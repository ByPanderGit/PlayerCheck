package de.bypander.playercheck;


import de.bypander.playercheck.commands.NameCommand;
import de.bypander.playercheck.commands.UUIDCommand;
import de.bypander.playercheck.config.PlayerCheckConfig;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;



@AddonMain
public class PlayerCheck extends LabyAddon<PlayerCheckConfig> {

  private static PlayerCheck playerCheck;
  private final String prefix = "§8[§bPlayerCheck§8] ";

  @Override
  protected void enable() {
    playerCheck = this;
    this.registerSettingCategory();

    this.registerCommand(new UUIDCommand());
    this.registerCommand(new NameCommand());

    this.logger().info("Enabled the Addon");
  }

  @Override
  protected Class<PlayerCheckConfig> configurationClass() {
    return PlayerCheckConfig.class;
  }

  public static PlayerCheck get() {
    return playerCheck;
  }

  public String prefix() {
    return prefix;
  }
}
