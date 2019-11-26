package org.bxtr.PvpBot.commands;

import lombok.extern.log4j.Log4j2;
import org.bxtr.PvpBot.Utils;
import org.bxtr.PvpBot.service.ChallongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Component
public class AddParticipantFromChallongeCommand extends BotCommand {

    @Autowired
    private ChallongeService challongeService;

    public AddParticipantFromChallongeCommand() {
        super("refreshFromChallonge", "Обновит участников из турнира на Challonge");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info(Utils.commandInputToString(user, chat, getCommandIdentifier(), arguments));
        Set<String> participant = challongeService.getParticipant();
        SendMessage sendMessage = new SendMessage().setChatId(chat.getId());
        if(participant.size() > 0) {
            sendMessage.setText("Added players: " + participant.stream().collect(Collectors.joining(", ")));
        } else {
            sendMessage.setText("None new player is added");
        }
        Utils.send(absSender, sendMessage);
    }
}
