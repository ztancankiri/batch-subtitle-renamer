import java.io.*;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String rootPath = args[0];
        String[] videos = new File(rootPath).list((dir, name) -> name.endsWith(".mkv"));

        for (String video : Objects.requireNonNull(videos)) {
            Matcher matcher = Pattern.compile("[Ss]([0-9]+)[Ee]([0-9\\-]+)", Pattern.MULTILINE).matcher(video);

            if (matcher.find()) {
                String season = matcher.group(1);
                String episode = matcher.group(2);

                renameSubtitle(rootPath, video, season, episode);
            }
        }
    }

    public static void renameSubtitle(String rootPath, String video, String season, String episode) {
        String[] subtitles = new File(rootPath).list((dir, name) -> name.endsWith(".srt"));

        for (String subtitle : Objects.requireNonNull(subtitles)) {
            Matcher matcher = Pattern.compile(String.format("[Ss]%s[Ee]%s", season, episode), Pattern.MULTILINE).matcher(subtitle);

            if (matcher.find()) {
                File oldFile = new File(Paths.get(rootPath, subtitle).toString());
                File newFile = new File(Paths.get(rootPath, video).toString().replace(".mkv", ".srt"));

                if (oldFile.renameTo(newFile)) {
                    System.out.println(newFile.getPath());
                }
            }
        }
    }
}
