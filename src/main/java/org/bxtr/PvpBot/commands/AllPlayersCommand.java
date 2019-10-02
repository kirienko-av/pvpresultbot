package org.bxtr.PvpBot.commands;

import org.bxtr.PvpBot.model.Player;
import org.bxtr.PvpBot.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Scope("singleton")
public class AllPlayersCommand extends BotCommand {
    public final static String DESCRIPTION = "все зарегистрированные игроки";

    @Autowired
    private PlayerService playerService;

    public AllPlayersCommand() {
        super("all", DESCRIPTION);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<Player> players = playerService.findAll();
        final StringBuilder stringBuilder = new StringBuilder();
        players.forEach(player -> {
            stringBuilder.append(player.getName()).append("\n");
        });

        SendMessage sendMessage = new SendMessage()
                .setChatId(chat.getId())
                .setText(stringBuilder.toString());

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}