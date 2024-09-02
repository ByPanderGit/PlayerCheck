package de.bypander.playercheck.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bypander.playercheck.PlayerCheck;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

public class NameCommand extends Command {

  public NameCommand() {
    super("name");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(!PlayerCheck.get().configuration().name().get())
      return false;
    if (arguments.length < 1) {
      this.displayMessage(Component.text(PlayerCheck.get().prefix() + I18n.translate("playercheck.command.missinguuid")));
      return true;
    }
    String uuid = arguments[0];

    if (!isValidUUID(uuid)) {
      this.displayMessage(Component.text(PlayerCheck.get().prefix() + I18n.translate("playercheck.command.notavaliduuid").replace("{uuid}", uuid)));
      return true;
    }

    new Thread(() -> {
      String name = getName(uuid);
      if (name == null) {
        this.displayMessage(Component.text(PlayerCheck.get().prefix() + I18n.translate("playercheck.command.nonamefound")));
      } else {
        TextComponent nameComponent = Component.text(name).clickEvent(ClickEvent.copyToClipboard(name));
        String foundMessage = I18n.translate("playercheck.command.namefound");
        int index = foundMessage.indexOf("{name}");
        if (index == -1) {
          this.displayMessage(Component.text(PlayerCheck.get().prefix() + foundMessage));
        }
        this.displayMessage(Component.text(PlayerCheck.get().prefix() + foundMessage.substring(0, index))
          .append(nameComponent)
          .append(Component.text(foundMessage.substring(index + "{name}".length()))));
      }
    }).start();
    return true;
  }

  private String getName(String uuid) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.minetools.eu/uuid/" + uuid).openStream()));
      JsonObject json = new Gson().fromJson(reader, JsonObject.class);

      if (json == null) throw new Exception("No response for uuid: " + uuid);
      if (!json.has("name") || !json.has("status") || !json.get("status").getAsString().equals("OK"))
        throw new Exception("Invalid response: " + json);

      return json.get("name").getAsString();
    } catch (Exception e) {
      System.out.println("Could not get name from minetools api: " + e);
    }
    return null;
  }

  private boolean isValidUUID(String uuidString) {
    String uuidPattern =
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    return Pattern.matches(uuidPattern, uuidString);
  }
}
