package org.bxtr.PvpBot.commands;

import lombok.extern.log4j.Log4j2;
import org.bxtr.PvpBot.Utils;
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

@Log4j2
@Component
@Scope("singleton")
public class AddFightResultShortCommand extends BotCommand {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AddFightResultCommand addFightResultCommand;

    public AddFightResultShortCommand() {
        super("short", "Добавления результатов бой, без указания первого игрока");
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info(Utils.commandInputToString(user, chat, getCommandIdentifier(), strings));
        String userName = user.getUserName();
        Player player = userName != null ? playerService.findPlayer("@" + userName) : null;
        if (player != null) {
            String[] arguments = new String[4];
            arguments[0] = player.getName();
            arguments[1] = strings[0];
            arguments[2] = strings[1];
            arguments[3] = strings[2];
            addFightResultCommand.execute(absSender, user, chat, arguments);
        } else {
            SendMessage sendMessage = new SendMessage()
                    .setChatId(chat.getId())
                    .setText("Вы не зарегистрированы в боте.");
            try {
                absSender.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
