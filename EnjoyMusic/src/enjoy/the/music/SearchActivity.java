package enjoy.the.music;

import java.util.List;

import com.tarena.fashionmusic.util.MusicUtil;

import enjoy.the.music.entry.Music;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
/**搜索歌曲的活动单元组件**/
public class SearchActivity extends Activity {
	private AutoCompleteTextView actv;
	private String[] names;
	private List<Music> musics;
	private Button search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		actv = (AutoCompleteTextView) findViewById(R.id.actv);
		search = (Button) findViewById(R.id.search);
		MusicUtil mu = new MusicUtil(this); 
		musics = mu.getMusic();
		names = new String[musics.size()];
		for (int i = 0; i < musics.size(); i++) {
			names[i] = musics.get(i).getMusicName();
		}
    
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, names);
		actv.setAdapter(adapter);
		search.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				String select = actv.getText().toString();
				Intent intent = new Intent(SearchActivity.this,
						MainActivity.class);
				intent.putExtra("select", select);
				intent.putExtra("isselect", true);
				startActivity(intent);
				finish();
				

			}
		});
	}

}
