import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Audio implements Runnable{

	private String songName;
	private Player player;
	private Thread t;
	
	public Audio(String name) {
		songName = name;
	}
	
	public void play() {
		t = new Thread(this);
		t.start();
	}
	
	public void pause() {
		t.suspend();
	}
	
	public void resume() {
		t.resume();
	}
	
	public void end() {
		player.close();
		t.resume();
		t.stop();
	}
	
	public boolean isFinished() {
		if(player != null) {
			return player.isComplete();
		}
		return false;
	}
		
	public String getName() {
		return songName;
	}

	@Override
	public void run() {
		try {
            FileInputStream fis = new FileInputStream("music/" + songName + ".mp3");
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            player.play();
        } catch (Exception e) {
            System.out.println("Problem playing file " + songName);
            System.out.println(e);
        }
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Audio)) {
			return false;
		}
		Audio s = (Audio) o;
		if(this.getName().equals(s.getName())) {
			return true;
		}
		return false;
	}
}
