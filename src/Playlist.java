import java.util.LinkedList;

public class Playlist {
	
	private LinkedList<Audio> songs;
	
	public Playlist() {
		songs = new LinkedList<Audio>();
	}
	
	public void add(Audio song) {
		songs.add(song);
	}
	
	public void remove(Audio song) {
		songs.remove(song);
	}
	
	public LinkedList<Audio> getPlaylist() {
		return songs;
	}
	
	public int size() {
		return songs.size();
	}
	
}
