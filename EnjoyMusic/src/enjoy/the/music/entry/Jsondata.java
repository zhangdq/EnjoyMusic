package enjoy.the.music.entry;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tarena.fashionmusic.util.ApacheHttpClient;

import enjoy.the.music.entry.NetMusic;

public class Jsondata {

	public static ArrayList<NetMusic> getNetMusic(String url) {
		ArrayList<NetMusic> netMusics = new ArrayList<NetMusic>();
		ApacheHttpClient response = new ApacheHttpClient();
		String result=null;
		try {
			result = response.httpPost(url, null);
			if (result==null) {
				result=response.httpGet(url);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			JSONObject o=new JSONObject(result.substring(13,result.indexOf("/*")));
			JSONArray jsonArray = o.getJSONArray("songlist");
                NetMusic netMusic;
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject obj=jsonArray.getJSONObject(j);
					netMusic=new NetMusic();
					netMusic.setAlbumId(obj.getString("albumId"));
					netMusic.setAlbumLink(obj.getString("albumLink"));
					netMusic.setAlbumName(obj.getString("albumName"));
					netMusic.setSingerId(obj.getString("singerId"));
					netMusic.setSingerName(obj.getString("singerName"));
					netMusic.setUrl(obj.getString("url"));
					netMusic.setId(obj.getString("id"));
					netMusic.setSongName(obj.getString("songName"));
					netMusics.add(netMusic);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return netMusics;

	}

}
