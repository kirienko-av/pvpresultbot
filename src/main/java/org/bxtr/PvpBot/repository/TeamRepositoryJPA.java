package org.bxtr.PvpBot.repository;

import org.bxtr.PvpBot.model.Team;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepositoryJPA extends JpaRepository<Team, Long> {
    Team getByPlayer_Name(String playerName);
}
