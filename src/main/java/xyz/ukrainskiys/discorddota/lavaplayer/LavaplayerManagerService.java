package xyz.ukrainskiys.discorddota.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import lombok.RequiredArgsConstructor;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.util.logging.ExceptionLogger;
import org.springframework.stereotype.Service;
import xyz.ukrainskiys.discorddota.lavaplayer.errors.CannotLoadTrackException;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class LavaplayerManagerService {

  private static final AudioPlayer player;
  private static final AudioPlayerManager playerManager;

  static {
    playerManager = new DefaultAudioPlayerManager();
    playerManager.registerSourceManager(new YoutubeAudioSourceManager());
    player = playerManager.createPlayer();
  }

  public void playTrackFromUrl(ServerVoiceChannel channel, String url) throws CannotLoadTrackException {
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
        throw new CannotLoadTrackException(e.getMessage());
      }
    }).exceptionally(ExceptionLogger.get());
  }
}
