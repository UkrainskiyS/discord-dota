package xyz.ukrainskiys.discorddota.bot.handlers;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;

public interface SlashCommandHandler {

  void handle(SlashCommandCreateEvent event);
}
