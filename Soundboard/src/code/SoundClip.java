package code;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Paths;

public class SoundClip {
	
	
    private Clip clip;
    private String name;
    private String path;

    public SoundClip(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.path = path;
        this.name = Paths.get(path).getFileName().toString(); 
        		
        		
        File file = new File(path);
        if (!file.exists()) throw new FileNotFoundException("File does not exist: " + path);

        try (AudioInputStream originalStream = AudioSystem.getAudioInputStream(file)) {
            AudioFormat baseFormat = originalStream.getFormat();
            AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
            );
            try (AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, originalStream)) {
                clip = AudioSystem.getClip();
                clip.open(convertedStream);
            }
        }
    }

    public void start() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public String getName() { return name; }
    public String getPath() { return path; }
}