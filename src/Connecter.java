public class Connecter implements Runnable{

	private PlaylistManager pm;
	private AudioQueue sq;
	private Audio song;
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
			for(Audio song : pm.getPlaylist(playlistName).getPlaylist()) {
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
				sq = new AudioQueue();
			}
			sq.rush(new Audio(songName));
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
			Audio song = new Audio(parts[1]);
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
			Audio[] songs = sq.getSongs();
			for(Audio song : songs) {
				returnString += song.getName() + "\n";
			}
		} else if(command.equals("queue")) {
			queue(parts[1]);
		} else if(command.equals("save")) {
			pm.save();
			pm = new PlaylistManager("playlists.txt");
		}
		return returnString;
	}
	
	private void queue(String songName) {
		Audio song = new Audio(songName);
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
		playlist.add(new Audio(songName));
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
		sq = new AudioQueue(playlist);
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
		sq = new AudioQueue(playlist);
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
