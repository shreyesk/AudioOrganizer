import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Vis {

	Connecter connecter;
	
	JFrame frame;
	
	public Vis() {
		connecter = new Connecter("playlists.txt");
		
		JTextField text = new JTextField();
		text.setEditable(false);
		
		frame = new JFrame("Music Player");
	    frame.setSize(700, 500);
	    frame.setLocationRelativeTo(null);

	    showHomeRow();
	    
	    frame.setVisible(true);
	}
	
	public void addToPlaylist(String song, String playlistNameToRedisplay) {
		frame.getContentPane().removeAll();
		
		String playlistList = connecter.decide("list");
		String[] playlists = playlistList.split("\n");
		JPanel buttonsPanel = new JPanel();
		for(String playlist : playlists) {
			JButton playlistButton = new JButton(playlist);
			buttonsPanel.add(playlistButton);
			playlistButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connecter.decide("add\t" + song + "\t" + playlist);
					displayPlaylist(playlistNameToRedisplay);
				}
			});
		}
		frame.getContentPane().add(BorderLayout.CENTER, buttonsPanel);
		
		showHomeRow();
		
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}
	
	public void displayPlaylist(String playlistName) {
		frame.getContentPane().removeAll();
		
		JPanel playlistControlPanel = new JPanel();
		
		JButton playPlaylist = new JButton("Play");
		playPlaylist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connecter.decide("play\t" + playlistName);
			}
		});
		playlistControlPanel.add(playPlaylist);
		
		JButton shufflePlaylist = new JButton("Shuffle");
		shufflePlaylist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connecter.decide("shuffle\t" + playlistName);
			}
		});
		playlistControlPanel.add(shufflePlaylist);
		
		JButton deletePlaylist = new JButton("Delete");
		deletePlaylist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connecter.decide("delete\t" + playlistName);
				goHome();
			}
		});
		playlistControlPanel.add(deletePlaylist);
		
		frame.getContentPane().add(BorderLayout.NORTH, playlistControlPanel);
		
		String songsList = connecter.decide("view\t" + playlistName);
		String[] songs = songsList.split("\n");
		JPanel songsPanel = new JPanel();
		songsPanel.setLayout(new BoxLayout(songsPanel, BoxLayout.Y_AXIS));
		for(String song : songs) {
			JLabel songLabel = new JLabel(song);
			JButton add = new JButton("Add");
			JButton remove = new JButton("Remove");
			JButton play = new JButton("Play");
			JButton queue = new JButton("Queue");
			
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addToPlaylist(song, playlistName);
				}
			});
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connecter.decide("remove\t" + song + "\t" + playlistName);
					displayPlaylist(playlistName);
				}
			});
			play.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connecter.decide("rush\t" + song);
				}
			});
			queue.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connecter.decide("queue\t" + song);
				}
			});
			
			JPanel songPanel = new JPanel();
			songPanel.add(songLabel);
			songPanel.add(add);
			songPanel.add(remove);
			songPanel.add(play);
			songPanel.add(queue);
			songsPanel.add(songPanel);
		}
		JScrollPane scrollPane = new JScrollPane(songsPanel);
		frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
		
		showHomeRow();
		
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}
	
	public void createPlaylist() {
		frame.getContentPane().removeAll();
		
		JTextField playlistName = new JTextField(20);
		JButton create = new JButton("Create");
		
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connecter.decide("create\t" + playlistName.getText());
				goHome();
			}
		});
		
		JPanel createPanel = new JPanel();
		createPanel.add(playlistName);
		createPanel.add(create);
		
		frame.getContentPane().add(BorderLayout.CENTER, createPanel);
		
		showHomeRow();
		
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}
	
	public void goHome() {
		connecter.decide("save");
		frame.getContentPane().removeAll();
		
		JButton addNewPlaylist = new JButton("Create");
		addNewPlaylist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createPlaylist();
			}
		});
		frame.getContentPane().add(BorderLayout.NORTH, addNewPlaylist);
		
		String playlistList = connecter.decide("list");
		String[] playlists = playlistList.split("\n");
		JPanel buttonsPanel = new JPanel();
		for(String playlist : playlists) {
			JButton playlistButton = new JButton(playlist);
			buttonsPanel.add(playlistButton);
			playlistButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					displayPlaylist(playlistButton.getText());
				}
			});
		}
		frame.getContentPane().add(BorderLayout.CENTER, buttonsPanel);
		
		showHomeRow();
		
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}
	
	public void showHomeRow() {
		JButton pause = new JButton("Pause");
		JButton resume = new JButton("Resume");
		JButton skip = new JButton("Skip");
		JButton homeButton = new JButton("Home");
		
		JPanel homePanel = new JPanel();
		homePanel.add(BorderLayout.SOUTH, pause);
		homePanel.add(BorderLayout.SOUTH, resume);
	    homePanel.add(BorderLayout.SOUTH, homeButton);
		homePanel.add(BorderLayout.SOUTH, skip);
		frame.getContentPane().add(BorderLayout.SOUTH, homePanel);
		
		pause.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		connecter.decide("pause");
		    	}
	    });
		
		resume.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		connecter.decide("resume");
		    	}
	    });
		
	    homeButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		goHome();
		    	}
	    });
	    
	    skip.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		connecter.decide("skip");
		    	}
	    });
	}
}
