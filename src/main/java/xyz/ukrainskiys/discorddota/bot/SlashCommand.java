package xyz.ukrainskiys.discorddota.bot;

public enum SlashCommand {
  ADD_TRACK, PLAY, PLAY_ADDED, PLAYLIST, STOP, PLAYING_HISTORY;

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }
}
