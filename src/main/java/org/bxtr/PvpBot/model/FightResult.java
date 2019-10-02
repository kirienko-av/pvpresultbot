package org.bxtr.PvpBot.model;

import javax.persistence.*;

@Entity
@Table(name = "FIGHT_RESULT")
public class FightResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ONE", referencedColumnName = "ID")
    private Player one;

    @ManyToOne
    @JoinColumn(name = "TWO", referencedColumnName = "ID")
    private Player two;

    @Column(name = "RESULT_ONE")
    private Integer resultOne;

    @Column(name = "RESULT_TWO")
    private Integer resultTwo;

    public FightResult() {
        //empty
    }

    public Player getWinner() {
        if(resultOne != null && resultTwo != null
                && resultOne > resultTwo) {
            return one;
        }

        return two;
    }

    public Long getId() {
        return id;
    }

    public FightResult setId(Long id) {
        this.id = id;
        return this;
    }

    public Player getOne() {
        return one;
    }

    public FightResult setOne(Player one) {
        this.one = one;
        return this;
    }

    public Player getTwo() {
        return two;
    }

    public FightResult setTwo(Player two) {
        this.two = two;
        return this;
    }

    public Integer getResultOne() {
        return resultOne;
    }

    public FightResult setResultOne(Integer resultOne) {
        this.resultOne = resultOne;
        return this;
    }

    public Integer getResultTwo() {
        return resultTwo;
    }

    public FightResult setResultTwo(Integer resultTwo) {
        this.resultTwo = resultTwo;
        return this;
    }
}