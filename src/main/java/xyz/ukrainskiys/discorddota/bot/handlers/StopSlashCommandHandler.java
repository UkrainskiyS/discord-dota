package xyz.ukrainskiys.discorddota.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.springframework.stereotype.Component;
import xyz.ukrainskiys.discorddota.repository.ServersRepository;
import xyz.ukrainskiys.discorddota.utils.DiscordUtils;

@Component
@RequiredArgsConstructor
public class StopSlashCommandHandler implements SlashCommandHandler {

  private final ServersRepository serversRepository;

  @Override
  public void handle(SlashCommandCreateEvent event) {
    final SlashCommandInteraction interaction = event.getSlashCommandInteraction();
    final Server server = event.getInteraction().getServer().orElse(null);
    if (server != null) {
      final AudioConnection connection = server.getAudioConnection().orElse(null);
      if (connection != null) {
        connection.close().thenAccept(v -> {
          serversRepository.saveServer(server.getId(), null);
          DiscordUtils.sendCommandRespond(interaction, "Track stopped.");
        }).exceptionally(e -> {
          DiscordUtils.sendCommandRespond(interaction, "Track not playing on your server.");
          return null;
        });
        return;
      }
    }
    DiscordUtils.sendCommandRespond(interaction, "Track not playing on your server.");
  }
}
