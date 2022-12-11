package xyz.ukrainskiys.discorddota.bot;

public enum SlashCommand {
  ADD_TRACK, PLAY, PLAYLIST, STOP;

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }
}
