import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class AudioQueue {

	private Queue<Audio> songs;
	
	public AudioQueue(Playlist playlist) {
		songs = new LinkedList<Audio>();
		for(Audio song : playlist.getPlaylist()) {
			songs.add(song);
		}
	}
	
	public AudioQueue() {
		songs = new LinkedList<Audio>();
	}
	
	public void add(Audio song) {
		songs.add(song);
	}
	
	public void rush(Audio song) {
		Audio[] songsArray = new Audio[songs.size() + 1];
		int i = 1;
		for(Audio songInstance : songs) {
			songsArray[i] = songInstance;
			i++;
		}
		songsArray[0] = song;
		songs.clear();
		for(Audio s : songsArray) {
			songs.add(s);
		}
	}
	
	public Audio getSong() {
		return songs.poll();
	}
	
	public int size() {
		return songs.size();
	}
	
	public Audio[] getSongs() {
		Audio[] songsArray = new Audio[songs.size()];
		int i = 0;
		for(Audio song : songs) {
			songsArray[i] = song;
			i++;
		}
		return songsArray;
	}
	
	public void shuffle() {
		Audio[] tempSongsArray = (Audio[]) songs.toArray();
		ArrayList<Audio> tempSongsList = (ArrayList<Audio>) Arrays.asList(tempSongsArray);
		Collections.shuffle(tempSongsList);
		songs.clear();
		for(Audio s : tempSongsList) {
			songs.add(s);
		}
	}
}
