package surfy.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import surfy.bot.Main;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {

    public static  <T> Stream<T> getStream(Iterator<T> iterator){
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator,0);
        return StreamSupport.stream(spliterator,false);
    }

    public static <T,E> T getKeyByValue(Map<T,E> map, E value){
        return map.entrySet()
                .stream()
                .filter(e->Objects.equals(e.getValue(),value))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }

    public static String formatTimestamp(long time) {
        Timestamp timestamp = new Timestamp(time);
        Date date = new Date(timestamp.getTime());
        return new SimpleDateFormat("MM/dd/yyy").format(date);
    }

    public static boolean isToday(String date) {
        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
        today.setTimeZone(TimeZone.getTimeZone("EST"));
        today.setLenient(false);
        return today.format(new Date()).equalsIgnoreCase(date);
    }

    public static boolean isSurfy(String id) {
        String[] whitelisted = new String[] {
                "247152228182392833", "584495073715290132"
        };
        return Arrays.asList(whitelisted).stream().anyMatch(id::equals);
    }

    public static boolean isActuallyMe(String id) {
        String[] whitelisted = new String[] {"247152228182392833"};
        return Arrays.asList(whitelisted).contains(id);
    }

    public static boolean isGuildMember(Member member){
        return member.getRoles()
                .stream()
                .filter(role->role.getName().equalsIgnoreCase("Child") ||
                        role.getName().equalsIgnoreCase("Underage") ||
                        role.getName().equalsIgnoreCase("Adult"))
                .count() > 0;
    }

    public static boolean isOfficer(Member member) {
        return member.getRoles()
                .stream()
                .filter(role->role.getName().equalsIgnoreCase("Helper") ||
                        role.getName().equalsIgnoreCase("Senior") ||
                        role.getName().equalsIgnoreCase("Owner"))
                .count() > 0;
    }

    public static float expToLevel(float xp) {
        float a = -1250L;
        float b = -8750L;
        float lvl = (float) (-b-(Math.sqrt((b*b) - 4*a*xp))) / (2*a);
        return (float) (((Math.floor(lvl * 100)) / 100.0) + 1);
    }

    public static float lvlToExp(Float level) {
        float xp;
        if(level <= 1000) {
            xp = 0L;
            for(int i = 1; i < level; i++) {
                xp += (i * 2500) + 7500;
            }
            return xp;
        }
        return 0L;
    }

    public static Object getKeyByValue(Properties properties,String value){
        return properties.entrySet()
                .stream()
                .filter(e->e.getValue().equals(value))
                .findFirst()
                .get()
                .getKey();
    }

    public static String formatDay(String date)throws ParseException {
        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
        today.setTimeZone(TimeZone.getTimeZone("EST"));
        today.setLenient(false);
        Date parsed = today.parse(date);
        String data = today.format(new Date());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            sdf.setLenient(false);
            Date firstDate = sdf.parse(date);
            Date currentDate = new Date();
            long diffDate = currentDate.getTime() - firstDate.getTime();
            long diffDays = diffDate / (1000 * 60 * 60 * 24);
            return diffDays + " day(s) ago";
        } catch(Exception exc) {
            return null;
        }
    }

    public static String indexToEmoji(int index) {
        switch(index){
            case 0:
                return ":one: ";
            case 1:
                return ":two: ";
            case 2:
                return ":three: ";
            case 3:
                return ":four: ";
            case 4:
                return ":five: ";
            case 5:
                return ":six: ";
            case 6:
                return ":seven: ";
            case 7:
                return ":eight: ";
            case 8:
                return ":nine: ";
            case 9:
                return ":keycap_ten: ";
            default:
                return String.valueOf(index);
        }
    }

    public static void sendPrivateMessage(User user, Object message) {
        user.openPrivateChannel().queue(channel ->{
            if(message instanceof MessageEmbed) channel.sendMessage((MessageEmbed)message).queue();
            else channel.sendMessage((String)message).queue();
        });
    }

    public static void sendConfirm(User user, Object message, String emoji) {
        user.openPrivateChannel().queue(channel ->{
            if(message instanceof MessageEmbed) channel.sendMessage((MessageEmbed)message).queue(msg -> msg.addReaction(emoji).queue());
            else channel.sendMessage((String)message).queue(msg -> msg.addReaction(emoji).queue());
        });
    }

    public static Role getOrangeRole() {
        return Main.getJDiscordAPI().getRolesByName("Orange",true).get(0);
    }

    public static Role getYellowRole() {
        return Main.getJDiscordAPI().getRolesByName("Yellow",true).get(0);
    }

    public static Role getBlueRole() {
        return Main.getJDiscordAPI().getRolesByName("Blue",true).get(0);
    }

    public static Role getPurpleRole() {
        return Main.getJDiscordAPI().getRolesByName("Purple",true).get(0);
    }

    public static Role getGreenColor() {
        return Main.getJDiscordAPI().getRolesByName("Green",true).get(0);
    }

    public static Role getApplicantRole(){
        return Main.getJDiscordAPI().getRolesByName("Applicant",true).get(0);
    }


    public static Role getChildRole() {
        return Main.getJDiscordAPI().getRolesByName("Child",true).get(0);
    }

    public static Role getUnderageRole() {
        return Main.getJDiscordAPI().getRolesByName("Underage",true).get(0);
    }

    public static Role getAdultRole() {
        return Main.getJDiscordAPI().getRolesByName("Adult",true).get(0);
    }

    public static Role getHelperRole() {
        return Main.getJDiscordAPI().getRolesByName("Helper",true).get(0);
    }

    public static boolean isVerified(Member member) {
        return member.getRoles()
                .stream()
                .filter(role->role.getName().equalsIgnoreCase("Members"))
                .count() > 0;
    }

    public static Role getUnverifiedRole() {
        return Main.getJDiscordAPI().getRolesByName("Unverified",true).get(0);
    }

    public static Role getMemberRole() {
        return Main.getJDiscordAPI().getRolesByName("Members",true).get(0);
    }

    public static Role getPermsRole() {
        return Main.getJDiscordAPI().getRolesByName("Top Shotta",true).get(0);
    }

    public static boolean isChild(Member member){
        return member.getRoles()
                .stream()
                .filter(role->role.getName().equalsIgnoreCase("Child"))
                .count() > 0;
    }

    public static boolean isUnderage(Member member){
        return member.getRoles()
                .stream()
                .filter(role->role.getName().equalsIgnoreCase("Underage"))
                .count() > 0;
    }

    public static boolean isAdult(Member member){
        return member.getRoles()
                .stream()
                .filter(role->role.getName().equalsIgnoreCase("Adult"))
                .count() > 0;
    }
}
