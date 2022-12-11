package xyz.ukrainskiys.discorddota.bot;

public enum SlashCommand {
  ADD_TRACK, PLAY, PLAYLIST, STOP, UNDEFINED;

  public static SlashCommand parse(String name) {
    return switch (name) {
      case "add_track" -> ADD_TRACK;
      case "play" -> PLAY;
      case "playlist" -> PLAYLIST;
      case "stop" -> STOP;
//      case "volume" -> VOLUME;
      default -> UNDEFINED;
    };
  }

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }
}
