package org.bxtr.PvpBot.service;

import lombok.RequiredArgsConstructor;
import org.bxtr.PvpBot.model.Player;
import org.bxtr.PvpBot.repository.PlayerCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerService {

    private final PlayerCrudRepository playerCrudRepository;

    public void createPlayer(String name) {
        if (name == null)
            return;

        Player player = new Player().setName(name.toLowerCase());
        playerCrudRepository.save(player);
    }

    public Player findPlayer(String name) {
        return playerCrudRepository.findByNameIgnoreCase(name).orElse(null);
    }

    public List<Player> findAll() {
        final List<Player> players = new ArrayList<>();
        playerCrudRepository.findAll()
                .forEach(item -> players.add(item));
        return players;
    }

    public List<Player> findLike(String name) {
        if (name != null)
            return playerCrudRepository.findByNameContaining(name.toLowerCase());
        return Collections.emptyList();
    }

    public void updatePlayer(Player player) {
        if (player == null)
            return;
        playerCrudRepository.save(player);
    }
}
