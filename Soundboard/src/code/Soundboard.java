package code;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


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
import com.github.kwhat.jnativehook.NativeInputEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class Soundboard extends JFrame implements NativeKeyListener{

	private static final long serialVersionUID = 1L;

	ClipBelegung geladeneKonfig;
	
	SoundClip[] audioClips = new SoundClip[10]; 
	
	
	public static final int[] keycodesIndex = {11,2,3,4,5,6,7,8,9,10};
	
	JTable clipBelegung;
	DefaultTableModel model;
	public static final String[] columnNames = {"Clip", "Path"};
	
	
	Font font = new Font("Segoe UI Symbol", Font.PLAIN, 21); // Beispiel-Font


	
	JButton save;
	
	JFileChooser fileChooser;
	
	JPanel controls; 
	
	JButton newClip;
	JButton deleteClip;
	JButton moveClipUp;
	JButton moveClipDown;
	
	int selectedClip; 
	enum Reihenfolge{
		UP, DOWN
	}
	
	
	public Soundboard() {
	
		//GUI initialisieren und starten
		
		setTitle("Soundboard");
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE); //TODO: vorher noch speichern
		setSize(800, 600);
		//setLayout(new FlowLayout());
		setLayout(new BorderLayout());

		
			
		//letzte Konfigurationen laden (eigens serialisiertes Objekt: (bildpath, audiopath))
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clipBelegung.ser"))) {
		    geladeneKonfig = (ClipBelegung) ois.readObject();  // Cast erforderlich[6][8]
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] paths = geladeneKonfig.getPaths();
		for(int i = 0; i < audioClips.length; i++) {
			if(paths[i] != null) {
				audioClips[i] = new SoundClip(paths[i]);
			}
		}
		
		
		
		/*
		save = new JButton("\uD83D\uDDAB");
		save.setFont(font);		
		save.addActionListener(new KnopfHandler());
		getContentPane().add(save);*/
		
		String[] columnNames = {"Clip", "Path"};
		model = new DefaultTableModel(refreshData(), columnNames);
		clipBelegung = new JTable(model);
		clipBelegung.setEnabled(false);
		clipBelegung.addMouseListener(new MausHandler(this));
		getContentPane().add(clipBelegung, BorderLayout.CENTER);
		
		
		
		clipBelegung.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
		    @Override
		    public java.awt.Component getTableCellRendererComponent(
		            javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		        if (row == selectedClip && selectedClip != -1) {
		            c.setBackground(new java.awt.Color(200, 230, 255)); // z.B. hellblau
		        } else {
		            c.setBackground(java.awt.Color.WHITE);
		        }
		        return c;
		    }
		});

		
		
		
		
		
		
		controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
		
		moveClipUp = new JButton("\u2191");
		moveClipUp.setFont(font);
//		moveClipUp.setToolTipText("move clip up");					
		moveClipUp.addActionListener(new KnopfHandler());
		//moveClipUp.setEnabled(false);
		controls.add(moveClipUp);
		moveClipDown = new JButton("\u2193");
		moveClipDown.setFont(font);
//		moveClipDown.setToolTipText("move clip down");
		moveClipDown.addActionListener(new KnopfHandler());
		//moveClipDown.setEnabled(false);
		controls.add(moveClipDown);
		newClip = new JButton("+");
		newClip.setFont(font);
		newClip.addActionListener(new KnopfHandler());
		newClip.setEnabled(false);
		controls.add(newClip);
		deleteClip = new JButton("\uD83D\uDDD1");
		deleteClip.setFont(font);
		deleteClip.addActionListener(new KnopfHandler());
		deleteClip.setEnabled(false);
		controls.add(deleteClip);
		getContentPane().add(controls, BorderLayout.EAST);
		
		JLabel tmp1 = new JLabel("");
		getContentPane().add(tmp1, BorderLayout.SOUTH);
		JLabel hint = new JLabel(" Use Ctrl + Number | only .wav supported rn ");
		hint.setFont(new Font("Open Sans", Font.ITALIC, 14));
		getContentPane().add(hint, BorderLayout.SOUTH);
		
		
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		setVisible(true);
		pack();
		
		selectedClip = -1;
		checkIfButtonsShouldBeEnabled();
	}
	
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		//System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

		/*if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            		try {
                		GlobalScreen.unregisterNativeHook();
            		} catch (NativeHookException nativeHookException) {
                		nativeHookException.printStackTrace();
            		}
        	}
        */
		
		
		
		
		
		//Sounds spielen: 
		if(NativeInputEvent.CTRL_L_MASK == (NativeInputEvent.CTRL_L_MASK & e.getModifiers())) {
			
			int index = keycodeToIndex(e.getKeyCode());
			if(index != -1) {
				for(SoundClip clip : audioClips) {
					if(clip != null) clip.stop();
				}
				System.out.println("Clip-" + index + "gestartet");
				if(audioClips[index] != null) audioClips[index].start();
			}	
			
		}	
			
		
		
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		//System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
	}
	
	public int keycodeToIndex(int code) {
		
		for (int i = 0; i < keycodesIndex.length; i++) {
	        if (keycodesIndex[i] == code) {
	            return i; 	        }
	    }
	    return -1; // Element nicht vorhanden
		
		
		
	}
	
	public static void main(String[] args) {
	
		try {
			//System.setProperty("jnativehook.lib.path", "/usr/lib");
			System.setProperty("jnativehook.enable.system.grabs", "true");
			
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new Soundboard());
	}
	
	public static SoundClip[] ändereReihenfolge (SoundClip[] clipsIn, int index, Reihenfolge reihenf) {
		
		SoundClip[] clipsOut = clipsIn.clone();
		
		if(reihenf == Reihenfolge.UP) {
			SoundClip tmp = clipsIn[index-1];
			clipsOut[index-1] = clipsIn[index];
			clipsOut[index] = tmp;
		}else if(reihenf == Reihenfolge.DOWN) {
			SoundClip tmp = clipsIn[index+1];
			clipsOut[index+1] = clipsIn[index];
			clipsOut[index] = tmp;
		}
		
		return clipsOut;
	}
	
	//ItemListener oder so, wenn ich die Clips in der Tabelle anklick -> Buttons sperren, je nachdem welcher selectedClip-Index
	public class MausHandler implements java.awt.event.MouseListener{
		
		Soundboard soundboard;

		
		public MausHandler(Soundboard soundboard) {
			//call by reference?
			this.soundboard = soundboard;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
						
			
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

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

	}
	
	public class KnopfHandler implements java.awt.event.ActionListener {
		
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			
			
			if(e.getSource() == newClip) {
				
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    String pfad = selectedFile.getAbsolutePath(); 
				    System.out.println(pfad); 
				    
				    audioClips[selectedClip] = new SoundClip(pfad);
				    
				    newClip.setEnabled(false);
				    deleteClip.setEnabled(true);
				}
				
			}else if(e.getSource() == deleteClip) {
				audioClips[selectedClip] = null;
				
				newClip.setEnabled(true);
				deleteClip.setEnabled(false);
				
			}else if(e.getSource() == moveClipUp) {
				audioClips = ändereReihenfolge(audioClips, selectedClip, Reihenfolge.UP);
				selectedClip--;
			}else if(e.getSource() == moveClipDown) {
				audioClips = ändereReihenfolge(audioClips, selectedClip, Reihenfolge.DOWN);
				selectedClip++;
			}
			
			
			
			
			checkIfButtonsShouldBeEnabled();
			model.setDataVector(refreshData(), Soundboard.columnNames);
			saveClipBelegung();
			
			/*if(e.getSource() == save) {
				saveClipBelegung();
			}*/
		}
	}
	
	public Object[][] refreshData (){
		Object[][] data = new Object[audioClips.length][2];
		
		for(int i = 0; i < audioClips.length; i++) {
			data[i][0] = i;
			data[i][1] = (audioClips[i] == null) ? null : audioClips[i].getName();
		}
		return data;
	}
	
	public void checkIfButtonsShouldBeEnabled() {
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
	
	public void saveClipBelegung() {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("clipBelegung.ser"))) {
			
			String[] paths = new String[10];
			for(int i = 0; i < audioClips.length; i++) {
				paths[i] = (audioClips[i] == null) ? null : audioClips[i].getPath();
			}
			
			
			oos.writeObject(new ClipBelegung(paths));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String pathToName (String pfad) {

		// 1. Letzten Schrägstrich finden (Windows/Unix-kompatibel)
		int letzterSlash = Math.max(
		    pfad.lastIndexOf('/'),   // Unix-Pfad
		    pfad.lastIndexOf('\\')   // Windows-Pfad
		);

		// 2. Dateinamen extrahieren (alles NACH dem letzten Schrägstrich)
		String dateiname = (letzterSlash >= 0) 
		    ? pfad.substring(letzterSlash + 1)  // "bruh.mp3"
		    : pfad;  // Falls kein Pfad vorhanden

		// 3. Letzten Punkt finden (Endung entfernen)
		int letzterPunkt = dateiname.lastIndexOf('.');
		String name = (letzterPunkt > 0) 
		    ? dateiname.substring(0, letzterPunkt)  // "bruh"
		    : dateiname;  // Falls keine Endung existiert

		return name;
	}
}
