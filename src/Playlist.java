import java.util.LinkedList;

public class Playlist {
	
	private LinkedList<Song> songs;
	
	public Playlist() {
		songs = new LinkedList<Song>();
	}
	
	public void add(Song song) {
		songs.add(song);
	}
	
	public void remove(Song song) {
		songs.remove(song);
	}
	
	public LinkedList<Song> getPlaylist() {
		return songs;
	}
	
	public int size() {
		return songs.size();
	}
	
}
