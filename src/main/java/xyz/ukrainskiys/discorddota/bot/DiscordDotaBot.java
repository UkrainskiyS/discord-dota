package xyz.ukrainskiys.discorddota.bot;

import static xyz.ukrainskiys.discorddota.bot.SlashCommand.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordDotaBot {

  @Value("${discord.token}")
  private String token;

  private final SlashCommandCreateListener slashCommandCreateListener;

  @PostConstruct
  public void init() {
    final DiscordApi api = new DiscordApiBuilder()
        .setToken(token)
        .addIntents(Intent.MESSAGE_CONTENT)
        .login()
        .join();

    api.bulkOverwriteGlobalApplicationCommands(getBuildersSlashCommands()).join();
    api.addSlashCommandCreateListener(slashCommandCreateListener);
  }

  private Set<SlashCommandBuilder> getBuildersSlashCommands() {
    final Set<SlashCommandBuilder> commands = new HashSet<>();
    commands.add(SlashCommand.with(STOP.toString(), "Interrupt playback"));
    commands.add(SlashCommand.with(PLAYLIST.toString(), "Saved tracks"));
    commands.add(SlashCommand.with(PLAY.toString(), "Launch a track or album from youtube", List.of(
        SlashCommandOption.createChannelOption("channel", "the channel to modify", true,
            List.of(ChannelType.SERVER_VOICE_CHANNEL)),
        SlashCommandOption.createStringOption("url", "link on youtube source", true)
    )));
    commands.add(SlashCommand.with(ADD_TRACK.toString(), "Add new track", List.of(
        SlashCommandOption.createStringOption("name", "name", true),
        SlashCommandOption.createStringOption("url", "link on youtube source", true))));
    return commands;
  }
}
