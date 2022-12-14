package xyz.ukrainskiys.discorddota.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.springframework.stereotype.Component;
import xyz.ukrainskiys.discorddota.model.PlayHistory;
import xyz.ukrainskiys.discorddota.repository.PlayingHistoryRepository;
import xyz.ukrainskiys.discorddota.utils.DiscordUtils;

import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayingHistorySlashCommandHandler implements SlashCommandHandler {

  private final PlayingHistoryRepository playingHistoryRepository;

  @Override
  public void handle(SlashCommandCreateEvent event) {
    final SlashCommandInteraction interaction = event.getSlashCommandInteraction();
    final Long count = interaction.getArgumentLongValueByIndex(0).orElse(10L);
    interaction.getServer().ifPresent(server -> {
      final TextChannel channel = interaction.getChannel().orElse(null);
      if (channel != null) {
        final List<PlayHistory.DTO> historyList = playingHistoryRepository.getLastHistoryList(server.getId(), count);
        final MessageBuilder builder = new MessageBuilder();
        historyList.forEach(history -> builder
                .append(history.getUrl() + " ")
                .append(history.getName() != null ? history.getName() + " " : "")
                .appendTimestamp(history.getStartTime().toInstant(ZoneOffset.UTC))
                .appendNewLine());
        DiscordUtils.sendCommandRespond(interaction, builder.getStringBuilder().toString());
      }
    });
  }
}
