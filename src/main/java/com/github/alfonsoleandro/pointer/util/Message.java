package com.github.alfonsoleandro.pointer.util;

import com.github.alfonsoleandro.mputils.message.MessageEnum;
import org.jetbrains.annotations.NotNull;

public enum Message implements MessageEnum {
    NO_PERMISSION("&cNo permission"),
    UNKNOWN_COMMAND("&cUnknown command. Try &7/%command% help"),
    CANNOT_SEND_CONSOLE("&cThat command can only be sent by a player"),
    INVALID_ITEM("&cYou are not holding a valid pointer item, please choose another item in your main hand"),
    POINTER_SET("&aPointer set! Ready to point"),
    POINTER_REMOVED("&aPointer removed"),
    ;

    private final String dflt;

    Message(String dflt) {
        this.dflt = dflt;
    }

    @Override
    public @NotNull String getPath() {
        return this.toString().toLowerCase().replace("_", " ");
    }

    @Override
    public @NotNull String getDefault() {
        return this.dflt;
    }
}
