package xyz.ukrainskiys.discorddota.bot.handlers;

import lombok.RequiredArgsConstructor;
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
    event.getInteraction().getServer()
            .ifPresent(server -> serversRepository.getVoiceChannelId(server.getId())
                    .ifPresentOrElse(channelId -> server.getVoiceChannelById(channelId)
                            .ifPresent(channel -> {
                              channel.disconnect();
                              serversRepository.saveServer(server.getId(), null);
                              DiscordUtils.sendCommandRespond(interaction, "Track stopped.");
                            }), () -> DiscordUtils.sendCommandRespond(interaction, "Track not playing on your server.")));
  }
}
