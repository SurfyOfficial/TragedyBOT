package surfy.managers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import surfy.API.APIManager;
import surfy.API.Player;
import surfy.bot.Main;
import surfy.modes.Bedwars;
import surfy.utils.ApplicationForm;
import surfy.utils.ConfigManager;
import surfy.utils.Emotes;
import surfy.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class EventListener extends ListenerAdapter {

    public boolean approval = false;
    public boolean userFound = false;


    Date date = new Date();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        /* Read texts as commands */
        Message message = event.getMessage();
        String[] syntax = message.getContentDisplay().split(" ");
        CommandsManager.getCommands()
                .stream()
                .filter(command -> message.getContentDisplay().startsWith(command.getCommand()))
                .forEach(command-> {
                    try {
                        command.onExecute(message,syntax);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {  /* Private Message Received Event */
        /* Application formatting */
        Message message = event.getMessage();
        if(ConfigManager.getQueueApplications().containsKey(event.getAuthor().getIdLong())){
            ApplicationForm applicationForm = ConfigManager.getQueueApplications().get(message.getAuthor().getIdLong());
            if(applicationForm.getEmptyQuestions() == 9) {
                try {
                    APIManager apiManager = new APIManager();
                    Player tipo = apiManager.getPlayer(event.getMessage().getContentDisplay());
                    if(tipo != null) {
                        Bedwars bedwars = tipo.getBedwarsInfo();
                        double kdr_bw = bedwars.getKills() / bedwars.getDeaths();
                        int bwstar = Math.toIntExact(bedwars.getReqLevel());
                        double bwwins = bedwars.getWins();
                        approval = (bwstar >= 300 & kdr_bw >= 6) || bwwins >= 3000;
                        userFound = true;
                    } else {
                        userFound = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(applicationForm.getEmptyQuestions() <= 1) {
                applicationForm.setAnswer(message.getContentDisplay());
                Utils.sendPrivateMessage(message.getAuthor(),"Thank you " + MarkdownUtil.bold(message.getAuthor().getName())
                        + " for submitting an application, you will be notified with a response within 24 hours.");
                EmbedBuilder embedApplication = new EmbedBuilder();
                embedApplication.setAuthor(event.getAuthor().getName());
                embedApplication.setThumbnail(event.getAuthor().getAvatarUrl());
                embedApplication.setTitle("Result");
                applicationForm.getQuestions().keySet().stream().sorted(Comparator.naturalOrder()).forEach(key-> embedApplication.addField(key,applicationForm.getQuestions().get(key),false));
                if(userFound) {
                    if (approval) {
                        embedApplication.addField("Requirements", Emotes.msgYES, false);
                    } else {
                        embedApplication.addField("Requirements", Emotes.msgNO, false);
                    }
                } else {
                    embedApplication.addField("Requirements",MarkdownUtil.italics("Username not found!"),false);
                }
                embedApplication.setFooter("TragedyBOT v1.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
						.setTimestamp(new Date().toInstant());
                Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("754113268435386369"))
                    .sendMessage(embedApplication.build()).queue(e-> {
                    e.addReaction(Emotes.YES).queue();
                    e.addReaction(Emotes.NO).queue();
                });
                ConfigManager.getQueueApplications().remove(event.getAuthor().getIdLong());
                return;
            }
            applicationForm.setAnswer(message.getContentDisplay());
            Utils.sendPrivateMessage(message.getAuthor(),applicationForm.getPendingQuestion());
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {  /* Message Reaction Add Event */
        if(Objects.requireNonNull(event.getUser()).getId().equals(Main.getJDiscordAPI().getSelfUser().getId()))
            /* Ignores if the BOT is the author of the event. */
            return;
        try {
            /* Colored Roles */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            if(message.getEmbeds().size() > 0
                    && Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(message.getEmbeds().get(0).getFooter()).getText())).contains("TragedyRoles")
                    && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                switch (event.getReactionEmote().getEmoji()) {
                    case Emotes.orangeHeart:
                        event.getGuild().addRoleToMember(event.getUser().getId(),Utils.getOrangeRole()).queue();
                        break;
                    case Emotes.yellowHeart:
                        event.getGuild().addRoleToMember(event.getUser().getId(),Utils.getYellowRole()).queue();
                        break;
                    case Emotes.blueHeart:
                        event.getGuild().addRoleToMember(event.getUser().getId(),Utils.getBlueRole()).queue();
                        break;
                    case Emotes.purpleHeart:
                        event.getGuild().addRoleToMember(event.getUser().getId(),Utils.getPurpleRole()).queue();
                        break;
                    case Emotes.greenHeart:
                        event.getGuild().addRoleToMember(event.getUser().getId(),Utils.getGreenColor()).queue();
                        break;
                }
                return;
            }

            /* Verification system */
            if(message.getEmbeds().size() > 0 && Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).contains("READ HERE TO VERIFY")
                    && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                event.getGuild().addRoleToMember(event.getUserId(),Utils.getMemberRole()).queue();
                event.getGuild().removeRoleFromMember(event.getUserId(),Utils.getUnverifiedRole()).queue();
                System.out.println("TragedyBOT: Added role Members to: " + event.getUser().getName());
                message.clearReactions().queue();
                EmbedBuilder embedVerify = new EmbedBuilder();
                User user = Main.getJDiscordAPI().getUsersByName(Objects.requireNonNull(Objects.requireNonNull(message.getEmbeds().get(0).getAuthor()).getName()),true).get(0);
                embedVerify.setTitle(user.getAsTag() + " verified successfully!")
                        .addField(user.getAsMention(),user.getId(),false)
                        .setColor(Color.green)
                        .setAuthor(user.getAsTag() + " | " + user.getId(),user.getEffectiveAvatarUrl(),user.getEffectiveAvatarUrl())
                        .setFooter("TragedyBOT v1.1 by ↬Surfy#0069", "https://visage.surgeplay.com/head/8/b32bf3ceba1e4c4ca4d5274dd9c89eec")
                        .setTimestamp(new Date().toInstant());
                message.editMessage(embedVerify.build()).queue();
                return;
            }

            /* Add Applicant role */
            if(message.getContentDisplay().contains("React here if you wish to apply for Tragedy Guild.")
                    && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                event.getGuild().addRoleToMember(event.getUserId(),Utils.getApplicantRole()).queue();
                System.out.println("TragedyBOT: Added role Applicant to: " + event.getUser().getName());
                return;
            }

            /* Sending confirm to the Applicant user */
            APIManager apiManager = new APIManager();
            if(message.getEmbeds().size() > 0 && Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).contains("Result") &&
                    Utils.isOfficer(Objects.requireNonNull(event.getMember())) ||
                    Utils.isSurfy(Objects.requireNonNull(event.getMember()).getId())) {
                User user = Main.getJDiscordAPI().getUsersByName(Objects.requireNonNull(Objects.requireNonNull(message.getEmbeds().get(0).getAuthor()).getName()),true).get(0);
                switch(event.getReactionEmote().getId()) {
                    case "757271443674365962":
                        /* Accepting */
                        Utils.sendConfirm(user,MarkdownUtil.bold("" +
                                "We would like to congratulate you, you've been accepted into Tragedy!" +
                                "\nYou'll be invited in the guild soon!" +

                                "\n\nBest of luck!" +
                                "\nTragedy Staff Team."),"❤");
                        message.clearReactions().queue();
                        message.getTextChannel().sendMessage( Emotes.msgYES + " - " + MarkdownUtil.bold(event.getUser().getName()) + " accepted applicant: " +
                                MarkdownUtil.bold(user.getName()) + ".").queue(msg -> msg.addReaction("❤").queue());

                        apiManager.sendPrivateMessage(Objects.requireNonNull(message.getJDA().getUserById("247152228182392833")),
                                MarkdownUtil.bold("Applications: " + event.getUser().getName() + " accepted user " + user.getName()));
                        System.out.println("Applications: " + event.getUser().getName() + " accepted user " + user.getName());
                        event.getGuild().addRoleToMember(event.getUserId(),Utils.getChildRole()).queue();
                        event.getGuild().removeRoleFromMember(event.getUserId(),Utils.getApplicantRole()).queue();
                        break;
                    case "757271455066095729":
                        /* Rejecting */
                        Utils.sendConfirm(user,MarkdownUtil.bold("" +
                                "Unfortunately your statistics do not meet our current requirements, " +
                                "but you’re free to apply again in the future." +

                                "\n\nBest of luck!" +
                                "\nTragedy Staff Team."),"❤");
                        message.clearReactions().queue();
                        message.getTextChannel().sendMessage(Emotes.msgNO + " - " + MarkdownUtil.bold(event.getUser().getName()) + " rejected applicant: " +
                                MarkdownUtil.bold(user.getName()) + ".").queue(msg -> msg.addReaction("\uD83D\uDE22").queue());

                        apiManager.sendPrivateMessage(Objects.requireNonNull(message.getJDA().getUserById("247152228182392833")),
                                MarkdownUtil.bold("Applications: " + event.getUser().getName() + " rejected user " + user.getName()));
                        System.out.println("Applications: " + event.getUser().getName() + " rejected user " + user.getName());
                        event.getGuild().removeRoleFromMember(event.getUserId(),Utils.getApplicantRole()).queue();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {  /* Message Reaction Remove Event */
        if(Objects.requireNonNull(event.getUser()).getId().equals(Main.getJDiscordAPI().getSelfUser().getId()))
            /* Ignores if the BOT is the author of the event. */
            return;
        try {
            /* Remove Applicant Role */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            if(message.getContentDisplay().contains("React here if you wish to apply for Tragedy Guild.")
                    && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())){
                event.getGuild().removeRoleFromMember(event.getUserId(),Utils.getApplicantRole()).queue();
                System.out.println("TragedyBOT: Removed role Applicant to: " + event.getUser().getName());
            }

            /* Remove Color Roles */
            if(message.getEmbeds().size() > 0
                    && Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(message.getEmbeds().get(0).getFooter()).getText())).contains("TragedyRoles")
                    && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                switch (event.getReactionEmote().getEmoji()) {
                    case Emotes.orangeHeart:
                        event.getGuild().removeRoleFromMember(event.getUser().getId(),Utils.getOrangeRole()).queue();
                        break;
                    case Emotes.yellowHeart:
                        event.getGuild().removeRoleFromMember(event.getUser().getId(),Utils.getYellowRole()).queue();
                        break;
                    case Emotes.blueHeart:
                        event.getGuild().removeRoleFromMember(event.getUser().getId(),Utils.getBlueRole()).queue();
                        break;
                    case Emotes.purpleHeart:
                        event.getGuild().removeRoleFromMember(event.getUser().getId(),Utils.getPurpleRole()).queue();
                        break;
                    case Emotes.greenHeart:
                        event.getGuild().removeRoleFromMember(event.getUser().getId(),Utils.getGreenColor()).queue();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}