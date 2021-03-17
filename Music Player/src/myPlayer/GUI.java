package myPlayer;

//importing all necessary packages
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

//implementing ActionListener interface
public class GUI implements ActionListener {
    JFrame frame;
    JLabel songName=new JLabel();
    JButton open=new JButton("Choose an Mp3");
    JButton play=new JButton("Play");
    JButton pause=new JButton("Pause");
    JButton resume=new JButton("Resume");
    JButton stop=new JButton("Stop");
    JFileChooser song;
    FileInputStream input;
    BufferedInputStream binput;
    File music=null;
    String musicName;
    String musicPath;
    long Length;
    long paused;
    Player player;
    Thread playThread;
    Thread resumeThread;
    GUI(){
        prepareGUI();
        addActionEvents();
        playThread=new Thread(runnablePlay);
        resumeThread=new Thread(runnableResume);

    }
    public void prepareGUI(){
        frame=new JFrame();
        frame.setTitle("Music Player");
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(540,200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        open.setBounds(200,10,100,30);
        frame.add(open);
        style(open);

        songName.setBounds(30,50,1000,30);
        frame.add(songName);

        play.setBounds(80,110,100,30);
        frame.add(play);
        style(play);

        pause.setBounds(170,110,100,30);
        frame.add(pause);
        style(pause);

        resume.setBounds(260,110,100,30);
        frame.add(resume);
        style(resume);
        
        stop.setBounds(350,110,100,30);
        frame.add(stop);
        style(stop);

    }
    public void addActionEvents(){
        //registering action listener to buttons
        open.addActionListener(this);
        play.addActionListener(this);
        pause.addActionListener(this);
        resume.addActionListener(this);
        stop.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==open){
            //code for selecting our mp3 file from dialog window
            song=new JFileChooser();
            song.setCurrentDirectory(new File("C:"));
            song.setDialogTitle("Select Mp3");
            song.setFileSelectionMode(JFileChooser.FILES_ONLY);
            song.setFileFilter(new FileNameExtensionFilter("Mp3 files","mp3"));
            if(song.showOpenDialog(open)==JFileChooser.APPROVE_OPTION){
                music=song.getSelectedFile();
                musicName=song.getSelectedFile().getName();
                musicPath=song.getSelectedFile().getPath();
            }
        }
        if(e.getSource()==play){
            //starting play thread
          playThread.start();
          songName.setForeground(Color.white);
          songName.setText("now playing : "+musicName);
        }
        if(e.getSource()==pause){
            //code for pause button
                 if(player!=null){
                     try {
                         paused=input.available();
                         player.close();
                     } catch (IOException e1) {
                         e1.printStackTrace();
                     }
                 }
        }

        if(e.getSource()==resume){
            //starting resume thread
           resumeThread.start();
        }
        if(e.getSource()==stop){
            //code for stop button
            if(player!=null){
                player.close();
                songName.setText("");
            }

        }

    }

  Runnable runnablePlay=new Runnable() {
      @Override
      public void run() {
          try {
              //code for play button
              input=new FileInputStream(music);
              binput=new BufferedInputStream(input);
              player=new Player(binput);
              Length=input.available();
              player.play();//starting music
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (JavaLayerException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  };

    Runnable runnableResume=new Runnable() {
        @Override
        public void run() {
            try {
                //code for resume button
                input=new FileInputStream(music);
                binput=new BufferedInputStream(input);
                player=new Player(binput);
                input.skip(Length-paused);
                player.play();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    public void style(JButton button) {
    	button.setBackground(Color.black);
    	button.setForeground(Color.white);
    }
}
