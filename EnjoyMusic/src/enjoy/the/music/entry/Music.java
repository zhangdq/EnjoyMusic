package enjoy.the.music.entry;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Music implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String musicName;
	private String singer;
	private String albumName;
	private String albumPath;
	private String albumkey;
	private String musicPath;
	private String time;
	private String savePath;
	private boolean isLoaded;

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public Music() {
		super();
	}

	public Music(String musicName, String singer, String albumName,
			String savePath) {
		super();
		setMusicName(musicName);
		setSinger(singer);
		setAlbumName(albumName);
		setSavePath(savePath);
	}

	public String getAlbumkey() {
		return albumkey;
	}

	public void setAlbumkey(String albumkey) {
		this.albumkey = albumkey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMusicName() {
		return setStr(musicName);
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getSinger() {
		return setStr(singer);
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getAlbumName() {
		return setStr(albumName);
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumPath() {
		return albumPath;
	}

	public void setAlbumPath(String albumPath) {
		this.albumPath = albumPath;
	}

	public String getMusicPath() {
		return musicPath;
	}

	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String setStr(String string) {
		String result = null;
		try {
			result = new String(string.getBytes("ISO-8859-1"), "GBK");
			if (result.contains("?")) {
				result = string;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
