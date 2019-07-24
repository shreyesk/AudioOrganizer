import java.util.LinkedList;

public class Connecter implements Runnable{

	private PlaylistManager pm;
	private SongQueue sq;
	private Song song;
	private Thread songFinished;
	private boolean isPaused = false;
	
	public Connecter(String fileName) {
		pm = new PlaylistManager(fileName);
		pm.save();
	}
	
	public String decide(String line) {
		String returnString = "";
		String[] parts = line.split("\t");
		String command = parts[0];
		if(command.equals("list")) {
			for(String playlistName : pm.getPlaylistNames()) {
				returnString += playlistName + "\n";
			}
		} else if(command.equals("view")) {
			String playlistName = parts[1];
			for(Song song : pm.getPlaylist(playlistName).getPlaylist()) {
				returnString += song.getName() + "\n";
			}
		} else if(command.equals("create")) {
			create(parts[1]);
			pm.save();
			pm = new PlaylistManager("playlists.txt");
		} else if(command.equals("play")) {
			play(parts[1]);
		} else if(command.equals("rush")) {
			String songName = parts[1];
			if(sq == null) {
				sq = new SongQueue();
			}
			sq.rush(new Song(songName));
			if(isPaused) {
				resumePlaying();
			}
			if(song != null) {
				song.end();
			}
			song = sq.getSong();
			song.play();
			songFinished = new Thread(this);
			songFinished.start();
		} else if(command.equals("shuffle")) {
			shuffle(parts[1]);
		} else if(command.equals("delete")) {
			String playlistName = parts[1];
			pm.remove(playlistName);
			pm.save();
			pm = new PlaylistManager("playlists.txt");
		} else if(command.equals("remove")) {
			Song song = new Song(parts[1]);
			pm.getPlaylist(parts[2]).remove(song);
			pm.save();
			pm = new PlaylistManager("playlists.txt");
		} else if(command.equals("add")) {
			add(parts[1], parts[2]);
			pm.save();
			pm = new PlaylistManager("playlists.txt");
		} else if(command.equals("pause")) {
			pause();
		} else if(command.equals("resume")) {
			resumePlaying();
		} else if(command.equals("skip")) {
			skip();
		} else if(command.equals("preview")) {
			Song[] songs = sq.getSongs();
			for(Song song : songs) {
				returnString += song.getName() + "\n";
			}
		} else if(command.equals("queue")) {
			queue(parts[1]);
		} else if(command.equals("search")) {
			String query = parts[1];
			String playlistName = parts[2];
			LinkedList<Song> songs = pm.getPlaylist(playlistName).getPlaylist();
			for(Song song : songs) {
				if(song.getName().contains(query)) {
					returnString += song.getName() + "\n";
				}
			}
		} else if(command.equals("save")) {
			pm.save();
			pm = new PlaylistManager("playlists.txt");
		}
		return returnString;
	}
	
	private void queue(String songName) {
		Song song = new Song(songName);
		sq.add(song);
	}

	private void skip() {
		if(song != null) {
			song.end();
		}
		if(isPaused) {
			resumePlaying();
		}
		song = sq.getSong();
		song.play();
		songFinished = new Thread(this);
		songFinished.start();
	}

	private void resumePlaying() {
		song.resume();
		isPaused = false;
	}

	private void pause() {
		song.pause();
		isPaused = true;
	}

	private void add(String songName, String playlistName) {
		Playlist playlist = pm.getPlaylist(playlistName);
		playlist.add(new Song(songName));
		pm.replace(songName, playlist);
	}

	private void play(String playlistName) {
		Playlist playlist = pm.getPlaylist(playlistName);
		if(isPaused) {
			resumePlaying();
		}
		if(song != null) {
			song.end();
			songFinished.stop();
		}
		sq = new SongQueue(playlist);
		song = sq.getSong();
		song.play();
		songFinished = new Thread(this);
		songFinished.start();
	}
	
	private void shuffle(String playlistName) {
		Playlist playlist = pm.getPlaylist(playlistName);
		if(isPaused) {
			resumePlaying();
		}
		if(song != null) {
			song.end();
			songFinished.stop();
		}
		sq = new SongQueue(playlist);
		sq.shuffle();
		song = sq.getSong();
		song.play();
		songFinished = new Thread(this);
		songFinished.start();
	}

	public void create(String playlistName) {
		pm.create(playlistName);
	}

	@Override
	public void run() {
		String currentSong = song.getName();
		while(song!= null && currentSong == song.getName() && !song.isFinished()) {
			System.out.print("");
		}
		if(sq.size() > 0 && song != null && currentSong.equals(song.getName())) {
			skip();
		}
	}

}
