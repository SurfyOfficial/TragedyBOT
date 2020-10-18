package surfy.API;

import java.util.Map;

public class GuildMember {

    /* Getting GuildMember from Hypixel's APIs */
    private String uuid;
    private String rank;
    private long joined;
    private int questParticipation;
    private Map<String, Integer> expHistory;
    private Map<String, Integer> memberInfo;

    public String getUUID() {
        return uuid;
    }

    public String getRank() {
        return rank;
    }

    public long getJoined() {
        return joined;
    }

    public int getQuestParticipation() {
        return questParticipation;
    }

    public Map<String, Integer> getExpHistory() {
        return expHistory;
    }

    public Map<String, Integer> getMemberInfo() {
        return memberInfo;
    }
}
