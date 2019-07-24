import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class PlaylistManager {

	private TreeMap<String, Playlist> playlists;
	private String fileName;
	
	public PlaylistManager(String file) {
		playlists = new TreeMap<>();
		fileName = file;

		try {
			Scanner fin = new Scanner(new File(file));
			
			int numOfPlaylists = fin.nextInt();
			fin.nextLine();
			for(int i = 0; i < numOfPlaylists; i++) {
				String playlistName = fin.nextLine();
				int numOfSongs = fin.nextInt();
				fin.nextLine();
				Playlist playlist = new Playlist();
				for(int j = 0; j < numOfSongs; j++) {
					String songName = fin.nextLine();
					Audio song = new Audio(songName);
					playlist.add(song);
				}
				playlists.put(playlistName, playlist);
			}
			
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Playlist playlistOfAllSongs = new Playlist();
		File folder = new File("music");
		File[] listOfFiles = folder.listFiles();
		for (File musicFile : listOfFiles) {
		    if (musicFile.isFile() && !musicFile.getName().equals(".DS_Store")) {
		        playlistOfAllSongs.add(new Audio(musicFile.getName().substring(0, musicFile.getName().length() - 4)));
		    }
		}
		playlists.put("all songs", playlistOfAllSongs);
	}
	
	public void create(String playlistName) {
		playlists.put(playlistName, new Playlist());
	}
	
	public void replace(String playlistName, Playlist playlist) {
		playlists.replace(playlistName, playlist);
	}
	
	public void remove(String playlistName) {
		playlists.remove(playlistName);
	}
	
	public Set<String> getPlaylistNames() {
		return playlists.keySet();
	}
	
	public Playlist getPlaylist(String playlistName) {
		return playlists.get(playlistName);
	}
	
	public void save() {
		try {
			PrintWriter pw = new PrintWriter(new File(fileName));
			
			int numOfPlaylists = playlists.size();
			pw.println(numOfPlaylists);
			
			for(String playlistName : playlists.keySet()) {
				pw.println(playlistName);
				Playlist playlist = playlists.get(playlistName);
				int playlistSize = playlist.size();
				pw.println(playlistSize);
				
				LinkedList<Audio> songs = playlist.getPlaylist();
				for(Audio song : songs) {
					pw.println(song.getName());
				}
			}
			
			pw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
