package ultimate.common.util.text;

import net.minecraft.util.text.TextFormatting;
import static net.minecraft.util.text.TextFormatting.*;

public enum LudicrousText {
    FABLOUSNESS(new TextFormatting[] { RED, GOLD, YELLOW, GREEN, AQUA, BLUE, LIGHT_PURPLE }, 80.0, 1, 1);

    LudicrousText(TextFormatting[] style, double delay, int step, int posstep) {
        this.style = style;
        this.delay = delay;
        this.step = step;
        this.posstep = posstep;
    }

    public final TextFormatting[] style;
    public final double delay;
    public final int step;
    public final int posstep;

    public String make(String input) {
        StringBuilder stringBuilder = new StringBuilder(input.length() * 3);
        double delay = this.delay;
        if (delay <= 0) {
            delay = 0.001;
        }

        int length = style.length;
        int offset = (int) Math.floor(getSystemTime() / delay) % length;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            int col = ((i * posstep) + style.length - offset) % length;

            stringBuilder.append(style[col].toString());
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    private static long getSystemTime() {
        return System.currentTimeMillis();
    }
}
