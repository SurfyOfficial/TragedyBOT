package surfy.commands;

public class oldBlacklistCommand /*extends Command*/ {
/*
    public oldBlacklistCommand() {
        super(">blacklist");
    }

    private static int c = 72;

    public static int getBlCounter() {
        return ++c;
    }

    @Override
    public void onExecute(Message message, String[] args) {
        EmbedBuilder embedBlacklist = new EmbedBuilder();

        if(message.getMember() != null && !Utils.isSurfy(message.getAuthor().getId()) & !Utils.isOfficer(message.getMember())) {
            /* Checks if messageAuthor is a Guild Staff.
            embedBlacklist.setTitle("You don't have this permission.")
                    .setColor(Color.black)
                    .setFooter(Main.version, Main.head)
                    .setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedBlacklist.build()).queue();
            return;
        }
        if (args.length == 1) {
            message.getChannel().sendMessage("**Usage** » >blacklist [IGN] [@Member] ").queue();
            return;
        }
        String ign = args[1];
        if(args.length > 2) {
            Member memb = message.getGuild().getMember(message.getMentionedUsers().get(0));
            assert memb != null;
            if(Utils.isChild(memb)) {
                message.getGuild().removeRoleFromMember(memb.getUser().getId(),Utils.getChildRole()).queue();
            }
            if(Utils.isUnderage(memb)) {
                message.getGuild().removeRoleFromMember(memb.getUser().getId(),Utils.getUnderageRole()).queue();
            }
            if(Utils.isAdult(memb)) {
                message.getGuild().removeRoleFromMember(memb.getUser().getId(),Utils.getAdultRole()).queue();
            }
            embedBlacklist.setTitle(memb.getUser().getName() + " | \"" + ign + "\" blacklisted successfully!")
                    .addField("Issued by: ","**"+message.getAuthor().getAsMention()+"**",false)
                    .setColor(Color.black)
                    .setFooter(Main.version, Main.head)
					.setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedBlacklist.build()).queue();
        } else {
            embedBlacklist.setTitle("\"" + ign + "\" blacklisted successfully!")
                    .addField("Issued by: ","**"+message.getAuthor().getAsMention()+"**",false)
                    .setColor(Color.black)
                    .setFooter(Main.version, Main.head)
					.setTimestamp(new Date().toInstant())
                    .setAuthor(message.getAuthor().getAsTag() + " | " + message.getAuthor().getId(),message.getAuthor().getEffectiveAvatarUrl(),message.getAuthor().getEffectiveAvatarUrl());
            message.getChannel().sendMessage(embedBlacklist.build()).queue();
        }

        //Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("765221397760835595")).sendMessage(getBlCounter() + ") " + args[1] + ".").queue(); //test bot
        Objects.requireNonNull(Main.getJDiscordAPI().getTextChannelById("693555959653597255")).sendMessage(getBlCounter() + ") " + ign + ".").queue(); //normal bot

    }
*/
}