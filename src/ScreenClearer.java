import java.io.IOException;

public class ScreenClearer {
    public static void clear() throws IOException, InterruptedException {
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
