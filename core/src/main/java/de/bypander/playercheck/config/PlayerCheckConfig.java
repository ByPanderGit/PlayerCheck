package de.bypander.playercheck.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
@SpriteTexture("settings.png")
public class PlayerCheckConfig extends AddonConfig {

  @SpriteSlot(x=0, y=0)
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> name = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> uuid = new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> name() {
    return this.name;
  }

  public ConfigProperty<Boolean> uuid() {
    return this.uuid;
  }
}
