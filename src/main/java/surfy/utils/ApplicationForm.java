package surfy.utils;

import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class ApplicationForm {
    private HashMap<String,String> questions = new HashMap<String, String>() {{
        put(MarkdownUtil.bold("1- What's your Minecraft ign?"),null);
        put(MarkdownUtil.bold("2- Where are you from? (Timezone) eg. US EST"),null);
        put(MarkdownUtil.bold("3- What's your Hypixel Network Level?"),null);
        put(MarkdownUtil.bold("4- What games you play the most on Hypixel Network?"),null);
        put(MarkdownUtil.bold("5- Weekly GXP you can contribute with:"),null);
        put(MarkdownUtil.bold("6- How toxic are you?"),null);
        put(MarkdownUtil.bold("7- Why should we choose you over another applicant?"),null);
        put(MarkdownUtil.bold("8- How can we contact you? ex. Discord (please leave tag)"),null);
        put(MarkdownUtil.bold("9- Tell us something about yourself!"),null);
    }};

    public HashMap<String, String> getQuestions() {
        return questions;
    }

    public String getPendingQuestion(){
        return getQuestions().keySet().stream()
                .sorted(Comparator.naturalOrder())
                .filter(key-> Objects.isNull(getQuestions().get(key)))
                .findFirst()
                .get();
    }

    public int getEmptyQuestions(){
        return (int)getQuestions().keySet().stream()
                .filter(key->Objects.isNull(getQuestions().get(key)))
                .count();
    }

    public void setAnswer(String text){
        getQuestions().put(getPendingQuestion(),text);
    }
}
