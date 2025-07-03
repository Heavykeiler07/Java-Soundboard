package code;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip {

	Clip clip;
	AudioInputStream originalStream, convertedStream;
	private String name;
	private String path;
	
	public SoundClip(String path) {
		
		
		name = Soundboard.pathToName(path);
		this.path = path; 
		
		/*
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(inputStream == null) {
			System.out.println("Fehler beim Laden -> InputStream ist null!!\n" + path);
		}*/
		
		File file = new File(path);
		if(file.exists() == false) {
			return;
		}
		try {
			originalStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
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
	    
	    convertedStream = AudioSystem.getAudioInputStream(targetFormat, originalStream);

	    // 2. SoftClipProvider erzwingen
	    System.setProperty("javax.sound.sampled.Clip", "com.sun.media.sound.SoftClipProvider");

	    
	    // 3. Clip initialisieren
	    try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	    try {
			clip.open(convertedStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void start() {
		/*
		try {
			clip.open(convertedStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		clip.setFramePosition(0);
		clip.start();
		
	}
	
	public void stop() {
		
		clip.stop();
		/*try {
			originalStream.close();
			convertedStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
}
