package nge.lk.mods.simpletabs.tabs;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a chat tab.
 */
public class ChatTab extends GuiNewChat {

    /**
     * The filter which selects the messages to accept.
     */
    private Matcher filter;

    /**
     * Whether this tab has unread messages.
     */
    @Getter private boolean unread;

    /**
     * The pattern string, for saving.
     */
    @Getter private String pattern;

    /**
     * Whether the pattern is literal, for saving.
     */
    @Getter private boolean literal;

    /**
     * Whether this tab has a whitelist.
     */
    @Getter private boolean whitelist;

    /**
     * The prefix for sent messages in this tab.
     */
    @Getter @Setter private String prefix;

    /**
     * Constructor.
     *
     * @param mc The minecraft reference.
     */
    public ChatTab(final Minecraft mc, final String pattern, final boolean literal, final boolean whitelist,
                   final String prefix) {
        super(mc);
        this.pattern = pattern;
        this.literal = literal;
        this.whitelist = whitelist;
        this.prefix = prefix;
        filter = Pattern.compile(pattern, literal ? Pattern.LITERAL : 0).matcher("");
    }

    /**
     * Updates the filter pattern.
     *
     * @param pattern The new pattern.
     * @param literal Whether the pattern will be escaped.
     * @param whitelist Whether the tab implements a whitelist or a blacklist.
     */
    public void updatePattern(final String pattern, final boolean literal, final boolean whitelist) {
        this.pattern = pattern;
        this.literal = literal;
        this.whitelist = whitelist;
        filter = Pattern.compile(pattern, literal ? Pattern.LITERAL : 0).matcher("");
    }

    @Override
    public void printChatMessageWithOptionalDeletion(final IChatComponent chatComponent, final int chatLineId) {
        super.printChatMessageWithOptionalDeletion(chatComponent, chatLineId);
        unread = true;
    }

    /**
     * Whether this tab accepts the given message for display.
     *
     * @param message The message.
     *
     * @return Whether it is accepted.
     */
    public boolean acceptsMessage(final CharSequence message) {
        filter.reset(message);
        return filter.find() == whitelist;
    }

    /**
     * Marks this tab as read.
     */
    public void markRead() {
        unread = false;
    }

    /**
     * Returns the export part of this tab.
     *
     * @return The export string.
     */
    public String getExport() {
        return pattern + "§" + Boolean.toString(literal) + "§" + prefix + "§" + Boolean.toString(whitelist);
    }
}