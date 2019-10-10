package org.bxtr.PvpBot;

import lombok.extern.log4j.Log4j2;
import org.bxtr.PvpBot.commands.*;
import org.bxtr.PvpBot.model.Player;
import org.bxtr.PvpBot.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
@Scope("singleton")
public class PvpBot extends TelegramLongPollingCommandBot {

    @Autowired
    private TelegramBotsApi telegramBotsApi;
    @Autowired
    private AddPlayerCommand addPlayerCommand;
    @Autowired
    private AddFightResultCommand addFightResultCommand;
    @Autowired
    private AllFightResultCommand allFightResultCommand;
    @Autowired
    private AllPlayersCommand allPlayersCommand;
    @Autowired
    private HelpCommand helpCommand;
    @Autowired
    private LeaderboardCommand leaderboardCommand;
    @Autowired
    private UpdateResultsOnChallongeCommand updateResultsOnChallongeCommand;
    @Autowired
    private FriendCodeListCommand friendCodeListCommand;
    @Autowired
    private AddFightResultShortCommand addFightResultShortCommand;

    @Autowired
    private PlayerService playerService;

    private static final Integer CACHETIME = 86400;

    @Value("${pvpbot.telegram.token}")
    private String TOKEN;


    public PvpBot(@Autowired DefaultBotOptions options) {
        super(options, "PvpResultBot");

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot.");
            try {
                execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }

    @PostConstruct
    private void postConstruct() {
        try {
            telegramBotsApi.registerBot(this);
            register(addPlayerCommand);
            register(addFightResultCommand);
            register(allPlayersCommand);
            register(allFightResultCommand);
            register(helpCommand);
            register(leaderboardCommand);
            register(updateResultsOnChallongeCommand);
            register(friendCodeListCommand);
            register(addFightResultShortCommand);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasInlineQuery()) {
            handleIncomingInlineQuery(update.getInlineQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            System.out.println(message);
            SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), message);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleIncomingInlineQuery(InlineQuery inlineQuery) {
        String query = inlineQuery.getQuery();
        log.info(String.format("Searching: %s \n", query));
        try {
            if (!query.isEmpty()) {
                List<Player> results = playerService.findLike(query);
                execute(converteResultsToResponse(inlineQuery, results));
            } else {
                execute(converteResultsToResponse(inlineQuery, new ArrayList<>()));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private static AnswerInlineQuery converteResultsToResponse(InlineQuery inlineQuery, List<Player> results) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(CACHETIME);
        answerInlineQuery.setResults(convertRaeResults(results));
        return answerInlineQuery;
    }


    private static List<InlineQueryResult> convertRaeResults(List<Player> raeResults) {
        List<InlineQueryResult> results = new ArrayList<>();
        for (int i = 0; i < raeResults.size(); i++) {
            for (String score : Arrays.asList("2 1", "1 2", "2 0", "0 2")) {
                Player player = raeResults.get(i);
                InputTextMessageContent messageContent = new InputTextMessageContent();
                messageContent.disableWebPagePreview();
                messageContent.enableMarkdown(true);
                messageContent.setMessageText("/short " + player.getName() + " " + score);
                InlineQueryResultArticle article = new InlineQueryResultArticle();
                article.setInputMessageContent(messageContent);
                article.setId(Integer.toString(i) + "#" + score);
                article.setTitle(player.getName());
                article.setDescription(score);
                results.add(article);
            }
        }

        return results;
    }
}
