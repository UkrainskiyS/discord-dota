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

@Service
@ConditionalOnClass({
    PlaySlashCommandHandler.class,
})
public class SlashCommandCreateListenerImpl implements SlashCommandCreateListener {

  private final Map<SlashCommand, SlashCommandHandler> commandHandlerMap = new HashMap<>();

  public SlashCommandCreateListenerImpl(
      PlaySlashCommandHandler playSlashCommandHandler) {
    this.commandHandlerMap.put(SlashCommand.PLAY, playSlashCommandHandler);
    this.commandHandlerMap.put(SlashCommand.ADD_TRACK, (event -> System.out.println()));
    this.commandHandlerMap.put(SlashCommand.STOP, (event -> System.out.println()));
    this.commandHandlerMap.put(SlashCommand.PLAYLIST, (event -> System.out.println()));
  }

  @Override
  public void onSlashCommandCreate(SlashCommandCreateEvent event) {
    final SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
    final SlashCommand command = SlashCommand.parse(slashCommandInteraction.getFullCommandName());

    commandHandlerMap.get(command).handle(event);

//    slashCommandInteraction.createImmediateResponder()
//        .setContent(slashCommandInteraction.getFullCommandName())
//        .setFlags(MessageFlag.EPHEMERAL)
//        .respond();
  }
}
