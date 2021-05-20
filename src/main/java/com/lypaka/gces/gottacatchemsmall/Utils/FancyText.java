package com.lypaka.gces.gottacatchemsmall.Utils;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class FancyText {

    public static Text getFancyText (String message) {

        return TextSerializers.FORMATTING_CODE.deserialize(message);

    }

}
