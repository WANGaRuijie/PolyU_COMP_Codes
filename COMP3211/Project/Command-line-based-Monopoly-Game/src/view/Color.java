package view;

/**
 * This class contains the ANSI escape codes for text and background colors.
 * May not be compatible with all terminals.
 * Works well on IntelliJ IDEA's terminal and MacOS terminal.
 * Source: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
 */
public class Color {

    // Text colors
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String NULL = "";

    // Background colors
    public static final String RED_BACKGROUND = "\u001B[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE
    public static final String NULL_BACKGROUND = "";

}
