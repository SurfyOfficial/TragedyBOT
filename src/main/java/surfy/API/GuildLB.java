package surfy.API;

import lombok.Getter;

@Getter
public class GuildLB {
    private String name,bwWins,totalWins,level,exp;

    public GuildLB(String name, String bwWins, String totalWins, String level, String exp) {
        /* Building Guild LB */
        this.name = name;
        this.bwWins = bwWins;
        this.totalWins = totalWins;
        this.level = level;
        this.exp = exp;
    }
}
