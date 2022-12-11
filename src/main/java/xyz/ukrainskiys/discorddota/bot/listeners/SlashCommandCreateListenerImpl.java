package xyz.ukrainskiys.discorddota.bot.listeners;

import java.util.HashMap;
import java.util.Map;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import xyz.ukrainskiys.discorddota.bot.SlashCommand;
import xyz.ukrainskiys.discorddota.bot.handlers.PlaySlashCommandHandler;
import xyz.ukrainskiys.discorddota.bot.handlers.SlashCommandHandler;
import xyz.ukrainskiys.discorddota.bot.handlers.StopSlashCommandHandler;

@Service
@ConditionalOnClass({
    PlaySlashCommandHandler.class,
})
public class SlashCommandCreateListenerImpl implements SlashCommandCreateListener {

  private final Map<SlashCommand, SlashCommandHandler> commandHandlerMap = new HashMap<>();

  public SlashCommandCreateListenerImpl(
          PlaySlashCommandHandler playSlashCommandHandler,
          StopSlashCommandHandler stopSlashCommandHandler) {
    this.commandHandlerMap.put(SlashCommand.PLAY, playSlashCommandHandler);
    this.commandHandlerMap.put(SlashCommand.ADD_TRACK, null /*TODO*/);
    this.commandHandlerMap.put(SlashCommand.PLAYLIST, null /*TODO*/);
    this.commandHandlerMap.put(SlashCommand.STOP, stopSlashCommandHandler);
  }

  @Override
  public void onSlashCommandCreate(SlashCommandCreateEvent event) {
    final SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
    final SlashCommand command = SlashCommand.valueOf(slashCommandInteraction.getFullCommandName().toUpperCase());

    commandHandlerMap.get(command).handle(event);
  }
}
