import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class SongQueue {

	private Queue<Song> songs;
	
	public SongQueue(Playlist playlist) {
		songs = new LinkedList<Song>();
		for(Song song : playlist.getPlaylist()) {
			songs.add(song);
		}
	}
	
	public SongQueue() {
		songs = new LinkedList<Song>();
	}
	
	public void add(Song song) {
		songs.add(song);
	}
	
	public void rush(Song song) {
		Song[] songsArray = new Song[songs.size() + 1];
		int i = 1;
		for(Song songInstance : songs) {
			songsArray[i] = songInstance;
			i++;
		}
		songsArray[0] = song;
		songs.clear();
		for(Song s : songsArray) {
			songs.add(s);
		}
	}
	
	public Song getSong() {
		return songs.poll();
	}
	
	public int size() {
		return songs.size();
	}
	
	public Song[] getSongs() {
		Song[] songsArray = new Song[songs.size()];
		int i = 0;
		for(Song song : songs) {
			songsArray[i] = song;
			i++;
		}
		return songsArray;
	}
	
	public void shuffle() {
		Song[] tempSongsArray = (Song[]) songs.toArray();
		ArrayList<Song> tempSongsList = (ArrayList<Song>) Arrays.asList(tempSongsArray);
		Collections.shuffle(tempSongsList);
		songs.clear();
		for(Song s : tempSongsList) {
			songs.add(s);
		}
	}
}
