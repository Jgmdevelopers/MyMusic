package ar.com.jgmdevelopers.mymusic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private List<File> songs;
    private int currentIndex;

    public Playlist() {
        this.songs = new ArrayList<>();
        this.currentIndex = -1;
    }

    public void addSong(File song) {
        songs.add(song);
    }

    public void removeSong(int index) {
        if (index >= 0 && index < songs.size()) {
            songs.remove(index);
            if (currentIndex == index) {
                currentIndex = -1; // Reset current index if the current song is removed
            } else if (currentIndex > index) {
                currentIndex--; // Adjust current index if a preceding song is removed
            }
        }
    }

    public File getNextSong() {
        if (songs.isEmpty()) return null;
        currentIndex = (currentIndex + 1) % songs.size();
        return songs.get(currentIndex);
    }

    public File getPreviousSong() {
        if (songs.isEmpty()) return null;
        currentIndex = (currentIndex - 1 + songs.size()) % songs.size();
        return songs.get(currentIndex);
    }

    public File getCurrentSong() {
        if (currentIndex >= 0 && currentIndex < songs.size()) {
            return songs.get(currentIndex);
        }
        return null;
    }

    public List<File> getSongs() {
        return songs;
    }
}
