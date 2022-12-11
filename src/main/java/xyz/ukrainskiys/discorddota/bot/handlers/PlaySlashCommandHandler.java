package xyz.ukrainskiys.discorddota.bot.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import lombok.RequiredArgsConstructor;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.util.logging.ExceptionLogger;
import org.springframework.stereotype.Component;
import xyz.ukrainskiys.discorddota.lavaplayer.LavaplayerAudioSource;
import xyz.ukrainskiys.discorddota.repository.ServersRepository;
import xyz.ukrainskiys.discorddota.utils.DiscordUtils;

@Component
@RequiredArgsConstructor
public class PlaySlashCommandHandler implements SlashCommandHandler {

  private static final AudioPlayer player;
  private static final AudioPlayerManager playerManager;

  static {
    playerManager = new DefaultAudioPlayerManager();
    playerManager.registerSourceManager(new YoutubeAudioSourceManager());
    player = playerManager.createPlayer();
  }

  private final ServersRepository serversRepository;

  @Override
  public void handle(SlashCommandCreateEvent event) {
    final SlashCommandInteraction interaction = event.getSlashCommandInteraction();
    final Optional<ServerChannel> optChannel = event.getSlashCommandInteraction().getArgumentChannelValueByIndex(0);
    final Optional<String> optUrl = event.getSlashCommandInteraction().getArgumentStringValueByIndex(1);
    if (optChannel.isPresent() && optUrl.isPresent()) {
      final String url = optUrl.get();
      if (!DiscordUtils.isValidYouTubeURL(url)) {
        DiscordUtils.sendCommandRespond(interaction, "Incorrect YouTube url!");
        return;
      }

      final ServerVoiceChannel channel = (ServerVoiceChannel) optChannel.get();
      channel.connect().thenAccept(audioConnection -> {
        final AudioSource source = new LavaplayerAudioSource(channel.getApi(), player);
        audioConnection.setAudioSource(source);
        try {
          playerManager.loadItem(url, new FunctionalResultHandler(
              player::playTrack,
              audioPlaylist -> audioPlaylist.getTracks().forEach(player::playTrack),
              () -> {
                throw new RuntimeException();
              },
              e -> {
                throw new RuntimeException();
              }
          )).get();
        } catch (RuntimeException | InterruptedException | ExecutionException e) {
          DiscordUtils.sendCommandRespond(interaction, String.format("""
              Can't download item from url: %s
              Check that this is the correct link on the YouTube video.
              """, url));
          channel.disconnect();
          return;
        }

        serversRepository.saveServer(channel.getServer().getId(), channel.getId());
        DiscordUtils.sendCommandRespond(interaction, "Track launched!");
      }).exceptionally(ExceptionLogger.get());
    }
  }
}
