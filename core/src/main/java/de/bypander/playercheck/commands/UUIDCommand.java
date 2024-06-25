package de.bypander.playercheck.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bypander.playercheck.PlayerCheck;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.util.I18n;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

public class UUIDCommand extends Command {

  public UUIDCommand() {
    super("uuid");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (arguments.length < 1) {
      this.displayMessage(Component.text(PlayerCheck.get().prefix() + I18n.translate("playercheck.command.missingname")));
      return true;
    }
    String name = arguments[0];

    if (!isValidName(name)) {
      this.displayMessage(Component.text(PlayerCheck.get().prefix() + I18n.translate("playercheck.command.notavalidname").replace("{name}", name)));
      return true;
    }

    new Thread(() -> {
      String uuid = getUUID(name);
      if (uuid == null) {
        this.displayMessage(Component.text(PlayerCheck.get().prefix() + I18n.translate("playercheck.command.nouuidfound")));
      } else {
        TextComponent uuidComponent = Component.text(uuid).clickEvent(ClickEvent.copyToClipboard(uuid));
        String foundMessage = I18n.translate("playercheck.command.uuidfound");
        int index = foundMessage.indexOf("{uuid}");
        if (index == -1) {
          this.displayMessage(Component.text(PlayerCheck.get().prefix() + foundMessage));
        }
        this.displayMessage(Component.text(PlayerCheck.get().prefix() + foundMessage.substring(0, index))
          .append(uuidComponent)
          .append(Component.text(foundMessage.substring(index + "{uuid}".length()))));
      }
    }).start();
    return true;
  }

  private String getUUID(String name) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.minetools.eu/uuid/" + name).openStream()));
      JsonObject json = new Gson().fromJson(reader, JsonObject.class);

      if (json == null) throw new Exception("No response for name " + name);
      if (!json.has("id") || !json.has("status") || !json.get("status").getAsString().equals("OK"))
        throw new Exception("Invalid response: " + json);

      String uuid = json.get("id").getAsString();
      return Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})").matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
    } catch (Exception e) {
      System.out.println("Could not get uuid from minetools api: " + e);
    }
    return null;
  }

  private boolean isValidName(String nameString) {
    String namePattern = "^\\w{3,16}$";
    return Pattern.matches(namePattern, nameString);
  }
}
