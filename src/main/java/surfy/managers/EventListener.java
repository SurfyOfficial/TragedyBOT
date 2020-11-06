package surfy.managers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import surfy.API.APIManager;
import surfy.API.Player;
import surfy.bot.Main;
import surfy.modes.Bedwars;
import surfy.utils.*;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class EventListener extends ListenerAdapter {

    public boolean approval = false;
    public boolean userFound = false;


    Date date = new Date();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String[] syntax = message.getContentDisplay().split(" ");
        CommandsManager.getCommands()
            .stream()
            .filter(command -> message.getContentDisplay().startsWith(command.getCommand()))
            .forEach(command -> {
                try {
                    command.onExecute(message, syntax);
                } catch (Exception e) {
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
                embedApplication.setFooter(Main.version, Main.head)
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
        if(Objects.requireNonNull(event.getUser()).getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
            /* Ignores if the BOT is the author of the event. */
            return;
        }

        try {
            /* Colored Roles */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            if(message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                if(message.getEmbeds().size() > 0 && Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(message.getEmbeds().get(0).getFooter()).getText())).contains("TragedyRoles")) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            /* Opening a Ticket */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            if (message.getEmbeds().size() > 0 && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                if (Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).contains("Need help?")) {
                    message.removeReaction(Emotes.ticketReaction, event.getUser()).queue();
                    String ticketID = StringUtils.randomAlphaNumeric(3);
                    if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCE9")) {
                        try {
                            List<String> lines = FileUtils.readLinesFromFile(new File("tickets.txt"));
                            assert lines != null;
                            if(lines.contains("Ticket- User: " + event.getUser().getAsTag())) {
                                EmbedBuilder embedAlreadyOpen = new EmbedBuilder()
                                        .setAuthor("TragedyTickets")
                                        .addField("Hello, " + event.getUser().getName() + "!", "I found an older ticket from you." +
                                                "\nMake sure you close it before opening a new one!", false)
                                        .setColor(Color.red)
                                        .setFooter("MagicMineBOT", message.getAuthor().getEffectiveAvatarUrl());
                                Utils.sendPrivateMessage(event.getUser(),embedAlreadyOpen.build());
                                return;
                            }
                            Category ticketCategory = Main.getJDiscordAPI().getCategoryById("774218844104294420");
                            lines.add("Ticket- User: " + event.getUser().getAsTag());
                            Role everyone = message.getGuild().getRoleById("666607896112529438");
                            Role muted = message.getGuild().getRoleById("751953877296873515");
                            Role helper = message.getGuild().getRoleById("688414440525529146");
                            Role senior = message.getGuild().getRoleById("666611118524858392");
                            Role owner = message.getGuild().getRoleById("666610534673547265");
                            FileUtils.writeLinesToFile(new File("tickets.txt"), lines);
                            TextChannel textChannel = message.getGuild().createTextChannel("𝐓𝐢𝐜𝐤𝐞𝐭-" + ticketID)
                                    .addPermissionOverride(Objects.requireNonNull(event.getMember()), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE), null)
                                    .addPermissionOverride(Objects.requireNonNull(helper), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE), null)
                                    .addPermissionOverride(Objects.requireNonNull(senior), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE), null)
                                    .addPermissionOverride(Objects.requireNonNull(owner), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE), null)
                                    .addPermissionOverride(Objects.requireNonNull(everyone), null, EnumSet.of(Permission.VIEW_CHANNEL))
                                    .addPermissionOverride(Objects.requireNonNull(muted), null, EnumSet.of(Permission.VIEW_CHANNEL))
                                    .setParent(ticketCategory)
                                    .setPosition(2)
                                    .complete(true);
                            EmbedBuilder embedClosure = new EmbedBuilder()
                                    .setTitle("Ticket-"+ticketID+".")
                                    .addField("Hello, " + event.getUser().getName() + "!", "Explain here your problem/question!\nThe Staff will be here shortly, be patient!", false)
                                    .addField("", "To close this ticket, click the " + Emotes.closeTicketEmoji + " reaction down below.", false)
                                    .setColor(Color.red)
                                    .setFooter(event.getUser().getAsTag(), event.getUser().getEffectiveAvatarUrl());
                            textChannel.sendMessage(embedClosure.build()).queue(msg -> msg.addReaction(Emotes.closeTicketReaction).queueAfter(50, TimeUnit.MILLISECONDS));
                        } catch (RateLimitedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            /* Closing a Ticket */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            if (message.getEmbeds().size() > 0 && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                if (Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).startsWith("Ticket-")) {
                    if (event.getReactionEmote().getEmoji().equals("\uD83D\uDD12")) {
                        message.clearReactions().queue();
                        List<String> lines = FileUtils.readLinesFromFile(new File("tickets.txt"));
                        assert lines != null;
                        if(lines.contains("Ticket- User: " + event.getUser().getAsTag())) {
                            lines.remove("Ticket- User: " + event.getUser().getAsTag());
                            FileUtils.writeLinesToFile(new File("tickets.txt"), lines);
                        }
                        EmbedBuilder embedClosing = new EmbedBuilder()
                                .setTitle("Ticket is closing!")
                                .addField("Hello, " + event.getUser().getName() + "!", "We hope that our answers helped you with your problem.", false)
                                .addField("", "If you're ever going to need our help again, feel free to open a new ticket!!\n*This channel will selfdestroy in **10 seconds**.*", false)
                                .setColor(Color.red)
                                .setFooter(event.getUser().getAsTag(), event.getUser().getEffectiveAvatarUrl());
                        message.getChannel().sendMessage(embedClosing.build()).queue();

                        Objects.requireNonNull(message.getGuild().getTextChannelById(message.getChannel().getId())).delete().queueAfter(10, TimeUnit.SECONDS);
                    }
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            /* Verification system */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            if(message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                if (message.getEmbeds().size() > 0 && Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).contains("READ HERE TO VERIFY")) {
                    event.getGuild().addRoleToMember(event.getUserId(), Utils.getMemberRole()).queue();
                    event.getGuild().removeRoleFromMember(event.getUserId(), Utils.getUnverifiedRole()).queue();
                    System.out.println("TragedyBOT: Added role Members to: " + event.getUser().getName());
                    message.clearReactions().queue();
                    EmbedBuilder embedVerify = new EmbedBuilder();
                    User user = Main.getJDiscordAPI().getUsersByName(Objects.requireNonNull(Objects.requireNonNull(message.getEmbeds().get(0).getAuthor()).getName()), true).get(0);
                    embedVerify.setTitle(user.getAsTag() + " verified successfully!")
                            .addField("ID: " + user.getId(), "Tag: " + user.getAsMention(), false)
                            .setColor(Color.green)
                            .setAuthor(user.getAsTag() + " | " + user.getId(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                            .setFooter(Main.version, Main.head)
                            .setTimestamp(new Date().toInstant());
                    message.editMessage(embedVerify.build()).queue();
                    return;
                }
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            /* Add Applicant role */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            if(message.getContentDisplay().contains("React here if you wish to apply for Tragedy Guild.")
                    && message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                event.getGuild().addRoleToMember(event.getUserId(),Utils.getApplicantRole()).queue();
                System.out.println("TragedyBOT: Added role Applicant to: " + event.getUser().getName());
                return;
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            /* Sending confirm to the Applicant user */
            Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).submit().get();
            APIManager apiManager = new APIManager();
            if(message.getEmbeds().size() > 0 && Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).equals("Result") &&
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
            if(message.getAuthor().getId().equals(Main.getJDiscordAPI().getSelfUser().getId())) {
                if (message.getEmbeds().size() > 0
                        && Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(message.getEmbeds().get(0).getFooter()).getText())).contains("TragedyRoles")) {
                    switch (event.getReactionEmote().getEmoji()) {
                        case Emotes.orangeHeart:
                            event.getGuild().removeRoleFromMember(event.getUser().getId(), Utils.getOrangeRole()).queue();
                            break;
                        case Emotes.yellowHeart:
                            event.getGuild().removeRoleFromMember(event.getUser().getId(), Utils.getYellowRole()).queue();
                            break;
                        case Emotes.blueHeart:
                            event.getGuild().removeRoleFromMember(event.getUser().getId(), Utils.getBlueRole()).queue();
                            break;
                        case Emotes.purpleHeart:
                            event.getGuild().removeRoleFromMember(event.getUser().getId(), Utils.getPurpleRole()).queue();
                            break;
                        case Emotes.greenHeart:
                            event.getGuild().removeRoleFromMember(event.getUser().getId(), Utils.getGreenColor()).queue();
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}