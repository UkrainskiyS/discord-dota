package xyz.ukrainskiys.discorddota.bot.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.Optional;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.util.logging.ExceptionLogger;
import org.springframework.stereotype.Component;
import xyz.ukrainskiys.discorddota.lavaplayer.LavaplayerAudioSource;
import xyz.ukrainskiys.discorddota.utils.DiscordBotUtils;

@Component
public class PlaySlashCommandHandler implements SlashCommandHandler {

  private static final AudioPlayer player;
  private static final AudioPlayerManager playerManager;

  static {
    playerManager = new DefaultAudioPlayerManager();
    playerManager.registerSourceManager(new YoutubeAudioSourceManager());
    player = playerManager.createPlayer();
  }

  @Override
  public void handle(SlashCommandCreateEvent event) {
    final Optional<ServerChannel> optChannel = event.getSlashCommandInteraction().getArgumentChannelValueByIndex(0);
    final Optional<String> optUrl = event.getSlashCommandInteraction().getArgumentStringValueByIndex(1);
    if (optChannel.isPresent() && optUrl.isPresent()) {
      final String url = optUrl.get();
      if (!DiscordBotUtils.isValidYouTubeURL(url)) {
        DiscordBotUtils.sendMessage(event, "Incorrect YouTube url!");
        return;
      }

      final ServerVoiceChannel channel = (ServerVoiceChannel) optChannel.get();
      channel.connect().thenAccept(audioConnection -> {
        final AudioSource source = new LavaplayerAudioSource(channel.getApi(), player);
        audioConnection.setAudioSource(source);
        playerManager.loadItem(url, new AudioLoadResultHandler() {
          @Override
          public void trackLoaded(AudioTrack audioTrack) {
            player.playTrack(audioTrack);
          }

          @Override
          public void playlistLoaded(AudioPlaylist audioPlaylist) {
            for (AudioTrack track : audioPlaylist.getTracks()) {
              player.playTrack(track);
            }
          }

          @Override
          public void noMatches() {
            DiscordBotUtils.sendMessage(event, String.format("Nothing found by url: %s!", url));
            channel.disconnect();
          }

          @Override
          public void loadFailed(FriendlyException e) {
            DiscordBotUtils.sendMessage(event, String.format("Load failed by url: %s!", url));
            channel.disconnect();
          }
        });
      }).exceptionally(ExceptionLogger.get());
    }
  }
}
