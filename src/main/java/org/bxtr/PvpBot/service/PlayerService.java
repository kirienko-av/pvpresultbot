package org.bxtr.PvpBot.service;

import org.bxtr.PvpBot.model.Player;
import org.bxtr.PvpBot.repository.PlayerCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerCrudRepository playerCrudRepository;

    public void createPlayer(String name) {
        if (name == null)
            return;

        Player player = new Player().setName(name.toLowerCase());
        playerCrudRepository.save(player);
    }

    public Player findPlayer(String name) {
        return playerCrudRepository.findByName(name.toLowerCase()).orElse(null);
    }

    public List<Player> findAll() {
        final List<Player> players = new ArrayList<>();
        playerCrudRepository.findAll()
                .forEach(item -> players.add(item));
        return players;
    }
}