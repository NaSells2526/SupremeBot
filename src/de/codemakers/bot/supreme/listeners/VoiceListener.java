package de.codemakers.bot.supreme.listeners;

import de.codemakers.bot.supreme.entities.AdvancedGuild;
import de.codemakers.bot.supreme.util.Standard;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMuteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceSuppressEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * VoiceListener
 *
 * @author Panzer1119
 */
public class VoiceListener extends ListenerAdapter {
    
    public static final String VOICECHANNEL = "VoiceChannel";
    public static final String LOG_CHANNEL_ID_VOICE = "log_channel_id_voice";
    public static final String LOG_VOICE_TEXT_JOIN = "log_voice_text_join";
    public static final String LOG_VOICE_TEXT_MOVE = "log_voice_text_move";
    public static final String LOG_VOICE_TEXT_LEAVE = "log_voice_text_leave";
    public static final String LOG_VOICE_TEXT_MUTE = "log_voice_text_mute";
    public static final String LOG_VOICE_TEXT_DEAFEN = "log_voice_text_deafen";
    public static final String LOG_VOICE_TEXT_GUILD_MUTE = "log_voice_text_guild_mute";
    public static final String LOG_VOICE_TEXT_GUILD_DEAFEN = "log_voice_text_guild_deafen";
    public static final String LOG_VOICE_TEXT_SELF_MUTE = "log_voice_text_self_mute";
    public static final String LOG_VOICE_TEXT_SELF_DEAFEN = "log_voice_text_self_deafen";
    public static final String LOG_VOICE_TEXT_SUPPRESS = "log_voice_text_suppress";

    @Override
    public final void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_JOIN, "%1$s joined %2$s #%3$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), VOICECHANNEL, event.getChannelJoined().getName()).queue();
            }
        }
    }
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_MOVE, "%1$s moved from #%2$s to #%3$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), event.getChannelLeft().getName(), event.getChannelJoined().getName()).queue();
            }
        }
    }
    
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_LEAVE, "%1$s left #%2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), event.getChannelLeft().getName()).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceMute(GuildVoiceMuteEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_MUTE, "%1$s was %2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), (event.isMuted() ? "muted" : "unmuted")).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceDeafen(GuildVoiceDeafenEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_DEAFEN, "%1$s was %2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), (event.isDeafened() ? "deafened" : "undeafened")).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_GUILD_MUTE, "%1$s was guild %2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), (event.isGuildMuted() ? "muted" : "unmuted")).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_GUILD_DEAFEN, "%1$s was guild %2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), (event.isGuildDeafened() ? "deafened" : "undeafened")).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_SELF_MUTE, "%1$s was self %2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), (event.isSelfMuted() ? "muted" : "unmuted")).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_SELF_DEAFEN, "%1$s was self %2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), (event.isSelfDeafened() ? "deafened" : "undeafened")).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceSuppress(GuildVoiceSuppressEvent event) {
        final AdvancedGuild advancedGuild = Standard.getAdvancedGuild(event.getGuild());
        final String log_channel_id_voice = advancedGuild.getSettings().getProperty(LOG_CHANNEL_ID_VOICE, null);
        if (log_channel_id_voice != null) {
            final TextChannel channel = event.getGuild().getTextChannelById(log_channel_id_voice);
            if (channel != null) {
                final String log_voice_text = advancedGuild.getSettings().getProperty(LOG_VOICE_TEXT_SUPPRESS, "%1$s was %2$s");
                channel.sendMessageFormat(log_voice_text, event.getVoiceState().getMember().getUser().getName(), (event.isSuppressed()? "suppressed" : "unsuppressed")).queue();
            }
        }
    }

}
