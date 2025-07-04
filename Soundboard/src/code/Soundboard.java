package code;

import java.awt.BorderLayout;
import java.awt.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.logging.Logger;
import java.util.logging.Level;

import code.listener.GlobalTastenHandler;
import code.listener.MausHandler;
import code.listener.KnopfHandler;

public class Soundboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Soundboard.class.getName());
    private static final int CLIP_COUNT = 10;
    public static final int[] KEYCODES_INDEX = {11,2,3,4,5,6,7,8,9,10};
    public static final String[] COLUMN_NAMES = {"Clip", "Path"};

    private ClipBelegung geladeneKonfig;
    private SoundClip[] audioClips = new SoundClip[CLIP_COUNT]; 

    private JTable clipBelegung;
    private DefaultTableModel model;
    private Font font = new Font("Segoe UI Symbol", Font.PLAIN, 21);
    private JFileChooser fileChooser;
    private JPanel controls; 
    private JButton newClip;
    private JButton deleteClip;
    private JButton moveClipUp;
    private JButton moveClipDown;

    private int selectedClip;

    enum Direction { UP, DOWN }

    
    
	public Soundboard() {
		initFrame();
        initConfig();
        initClips();
        initTable();
        initControls();
        initListeners();
        finalizeInit();
	}
	
	
	private void initFrame() {
        setTitle("Soundboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
    }

    private void initConfig() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clipBelegung.ser"))) {
            geladeneKonfig = (ClipBelegung) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            geladeneKonfig = new ClipBelegung(new String[CLIP_COUNT]);
            // Logging oder Info für den User hier!
        }
    }

    private void initClips() {
        String[] paths = geladeneKonfig.getPaths();
        for (int i = 0; i < audioClips.length; i++) {
            if (paths[i] != null) {
            	try {
            		audioClips[i] = new SoundClip(paths[i]);
            	} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            		LOGGER.log(Level.WARNING, "SoundClip could not be loaded: " + paths[i], e);
                    audioClips[i] = null; // Oder z.B. ein Dummy-Objekt/Platzhalter
            	}
            }
        }
    }

    private void initTable() {
        model = new DefaultTableModel(refreshData(), COLUMN_NAMES);
        clipBelegung = new JTable(model);
        clipBelegung.setEnabled(false);
        clipBelegung.addMouseListener(new MausHandler(this));
        getContentPane().add(clipBelegung, BorderLayout.CENTER);
        // ... evtl. Renderer usw.
    }

    private void initControls() {
        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        moveClipUp = new JButton("\u2191");
        moveClipUp.setFont(font);
        moveClipUp.addActionListener(new KnopfHandler(this));
        controls.add(moveClipUp);

        moveClipDown = new JButton("\u2193");
        moveClipDown.setFont(font);
        moveClipDown.addActionListener(new KnopfHandler(this));
        controls.add(moveClipDown);

        newClip = new JButton("+");
        newClip.setFont(font);
        newClip.addActionListener(new KnopfHandler(this));
        newClip.setEnabled(false);
        controls.add(newClip);

        deleteClip = new JButton("\uD83D\uDDD1");
        deleteClip.setFont(font);
        deleteClip.addActionListener(new KnopfHandler(this));
        deleteClip.setEnabled(false);
        controls.add(deleteClip);

        getContentPane().add(controls, BorderLayout.EAST);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        JLabel hint = new JLabel(" Use Ctrl + Number | only .wav supported rn ");
        hint.setFont(new Font("Open Sans", Font.ITALIC, 14));
        getContentPane().add(hint, BorderLayout.SOUTH);
    }

    private void initListeners() {
        selectedClip = -1;
        checkIfButtonsShouldBeEnabled();
    }

    private void finalizeInit() {
    	pack();
    	setLocationRelativeTo(null); 
        setVisible(true);
    }
	
	
	
	
	
    public void handleMouseAction(java.awt.event.MouseEvent e) {
		int row = clipBelegung.rowAtPoint(e.getPoint()); 
        
        
        
        selectedClip = row;
        String cellcontent;
        try{
        	cellcontent = clipBelegung.getValueAt(row, 1).toString();
        }catch (Exception e1) {
	        cellcontent = null;
        }
        
        System.out.println("Zeile: " + row + " | Content: " + cellcontent);
        
        
        if(cellcontent == null) {
        	newClip.setEnabled(true);
        	deleteClip.setEnabled(false);
        }else {
        	newClip.setEnabled(false);
        	deleteClip.setEnabled(true);
        }
        
        
        checkIfButtonsShouldBeEnabled();
        	       
        //Testweise
        clipBelegung.repaint();
	}
	
	public void handleButtonAction(java.awt.event.ActionEvent e) {
		if(e.getSource() == newClip) {
			int result = fileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    String pfad = selectedFile.getAbsolutePath(); 
			    System.out.println(pfad); 
			    
			    try {
					audioClips[selectedClip] = new SoundClip(pfad);
					newClip.setEnabled(false);
				    deleteClip.setEnabled(true);
				} catch (IOException | UnsupportedAudioFileException | LineUnavailableException el ) {
					LOGGER.log(Level.WARNING, "SoundClip konnte nicht geladen werden: " + pfad, el);
                    audioClips[selectedClip] = null; // Oder z.B. ein Dummy-Objekt/Platzhalter
				}
			}
			
		}else if(e.getSource() == deleteClip) {
			audioClips[selectedClip] = null;
			
			newClip.setEnabled(true);
			deleteClip.setEnabled(false);
			
		}else if(e.getSource() == moveClipUp) {
			audioClips = changeOrder(audioClips, selectedClip, Direction.UP);
			selectedClip--;
		}else if(e.getSource() == moveClipDown) {
			audioClips = changeOrder(audioClips, selectedClip, Direction.DOWN);
			selectedClip++;
		}
		
		
		
		
		checkIfButtonsShouldBeEnabled();
		model.setDataVector(refreshData(), COLUMN_NAMES);
		saveClipBelegung();
	}
	
	//CTRL + NUM -> Play Sound
    public void handleGlobalTastenAction(NativeKeyEvent e) {
    	int index = keycodeToIndex(e.getKeyCode());
		if(index != -1) {
			for(SoundClip clip : audioClips) {
				if(clip != null) clip.stop();
			}
			System.out.println("Clip-" + index + " started");
			if(audioClips[index] != null) audioClips[index].start();
		}
    }
	
	
		
			
			
		
	
	public int keycodeToIndex(int code) {
		
		for (int i = 0; i < KEYCODES_INDEX.length; i++) {
	        if (KEYCODES_INDEX[i] == code) {
	            return i; 	        }
	    }
	    return -1; // Element nicht vorhanden
		
		
		
	}
	
	public static SoundClip[] changeOrder (SoundClip[] clipsIn, int index, Direction reihenf) {
		
		SoundClip[] clipsOut = clipsIn.clone();
		
		if(reihenf == Direction.UP) {
			SoundClip tmp = clipsIn[index-1];
			clipsOut[index-1] = clipsIn[index];
			clipsOut[index] = tmp;
		}else if(reihenf == Direction.DOWN) {
			SoundClip tmp = clipsIn[index+1];
			clipsOut[index+1] = clipsIn[index];
			clipsOut[index] = tmp;
		}
		
		return clipsOut;
	}
	

	
		
		
			
			
			
	
	
	private Object[][] refreshData (){
		Object[][] data = new Object[CLIP_COUNT][2];
		
		for(int i = 0; i < CLIP_COUNT; i++) {
			data[i][0] = i;
			data[i][1] = (audioClips[i] == null) ? null : audioClips[i].getName();
		}
		return data;
	}
	
	//TODO: Logik verbessern und kommentieren
	private void checkIfButtonsShouldBeEnabled() {
		if (selectedClip == -1) {
	        moveClipUp.setEnabled(false);
	        moveClipDown.setEnabled(false);
	        newClip.setEnabled(false);
	        deleteClip.setEnabled(false);
	    } else {
	    	moveClipUp.setEnabled(true);
	    	moveClipDown.setEnabled(true);
	    	
	        if(selectedClip == 0) { 
	        	moveClipUp.setEnabled(false);
	        }else if(selectedClip == audioClips.length-1) {
		        moveClipDown.setEnabled(false);
	        }
	    }
	}
	
	//TODO: Use JSON for serialization instead of Java serialization (Versionsprobleme und "Binary Garbage")
	private void saveClipBelegung() {
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("clipBelegung.ser"))) {
	        String[] paths = new String[CLIP_COUNT];
	        for (int i = 0; i < CLIP_COUNT; i++) {
	            paths[i] = (audioClips[i] == null) ? null : audioClips[i].getPath();
	        }
	        oos.writeObject(new ClipBelegung(paths));
	    } catch (FileNotFoundException e) {
	        LOGGER.log(Level.SEVERE, "Belegungsdatei nicht gefunden", e);
	    } catch (IOException e) {
	        LOGGER.log(Level.SEVERE, "Fehler beim Speichern der Clipbelegung", e);
	    }
	}
	
	
	
	
	public static void main(String[] args) {
	    try {
	        System.setProperty("jnativehook.enable.system.grabs", "true");
	        GlobalScreen.registerNativeHook();
	    } catch (NativeHookException ex) {
	    	LOGGER.log(Level.SEVERE, "There was a problem registering the native hook.", ex);
	        System.exit(1);
	    }
	    Soundboard soundboard = new Soundboard();
	    GlobalScreen.addNativeKeyListener(new GlobalTastenHandler(soundboard));
	}
}
