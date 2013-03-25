package enjoy.the.music;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.tarena.fashionmusic.MusicLayIntenface;
import com.tarena.fashionmusic.MyApplication;
import com.tarena.fashionmusic.util.Constant;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import enjoy.the.music.R;
import enjoy.the.music.db.MusicGroupDao;
import enjoy.the.music.db.MusicItemDao;
import enjoy.the.music.entry.Music;
import enjoy.the.music.entry.MusicGroup;
import enjoy.the.music.entry.MusicItem;
import enjoy.the.music.main.adapter.LocalMusicListAdapter;


public class LocalLayout extends LinearLayout implements MusicLayIntenface {

	
	View rootview;
	ListView localistview;
	LayoutInflater inflater;
	Context context;
	MusicItemDao musicItemDao;
	LocalMusicListAdapter adapter;
	Handler handler;

	public static final int MULTCHOSE = 1;
	public static final int STAR_ADDGROUP = 2;
	public static final int SINGLE_CHOSE = 3;
	public static final int CHANGE_LIST = 4;

	//本地歌曲的表单显示
	public LocalLayout(Context context, Handler handler) {
		super(context);
		//通过LayoutInflater一个对象inflater，来显示一个表单布局
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		rootview = inflater.inflate(R.layout.musiclist, this, true);
		this.handler = handler;
		initView();//更新及初始表单
		initData();//更新及初始数据
		initListener();
		//Others();
	
	}

	@Override
	public void initView() {
		musicItemDao = new MusicItemDao(context);
		localistview = (ListView) rootview.findViewById(R.id.lvSounds);
	}

	ArrayList<Music> locallist;
	ArrayList<Integer> musicid = new ArrayList<Integer>();;
	MusicItem musicItem;

	@Override
	public void initData() {

		locallist = MyApplication.musics;
		adapter = new LocalMusicListAdapter(context, locallist, localistview);
		localistview.setAdapter(adapter);
	}

	/*对Listview的监听与具体行的相应操作*/
	@Override
	public void initListener() {
		localistview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String[] data = { "播放", "添加到分组", "删除" };
				ShowpopChoes(data, arg2);
				return false;
			}
		});

		localistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				goplay(arg2);
			}
		});

	}

	public void ShowpopChoes(String[] data, final int positions) {
		AlertDialog.Builder builder = new Builder(context);
		AlertDialog dialog = builder
				.setTitle(R.string.choice_do)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(data, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 获取组id
								Music music = locallist.get(positions);
								if (which == 0) {// 播放
									goplay(positions);
								} else if (which == 1) {// 添加到分组
									AddToGroup(context, music.getId(), false,
											null);
								} else if (which == 2) {// 删除
									// 从媒体库中删除该音乐的记录
									LocalLayout.this.context
											.getContentResolver()
											.delete(ContentUris
													.withAppendedId(
															MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
															music.getId()),
													null, null);
									// 从sdcard上删除文件
									new File(music.getSavePath()).delete();
									// 更新listView
									LocalLayout.this.adapter.remove(positions);
									// 从音乐分组列表中移除音乐信息
									musicItemDao.deleteItemByMusicid(music
											.getId());

								}
							
								dialog.cancel();
							}
							 
						}).setNegativeButton(R.string.cancel, null).show();
	}
	
	

	


	/**
	 * 添加当前音乐到分组
	 * 
	 * @param context
	 * @param positions
	 */
	private void AddToGroup(final Context context, final int musicId,
			final boolean ismult, final ArrayList<Integer> ids) {
		// 查询所有的musicgroup
		ArrayList<MusicGroup> groups = new MusicGroupDao(context).getGroups();
		// 将所有的musicgrouptitle保存在一个数组中
		final String[] titles = new String[groups.size()];
		for (int i = 0; i < titles.length; i++) {
			titles[i] = groups.get(i).getId() + "、" + groups.get(i).getTitle();
		}
		// 创建单选对话框
		AlertDialog.Builder builder = new Builder(context);
		final AlertDialog dialog = builder
				.setTitle(R.string.choice_group)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(titles, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 获取组id
								int groupId = Integer.parseInt(titles[which]
										.substring(0,
												titles[which].indexOf("、")));
								if (ismult) {
									for (Integer id : ids) {
										addToGroup(id, groupId);
									}
								} else {
									addToGroup(musicId, groupId);
								}
								// 关闭对话框
								dialog.cancel();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	private void addToGroup(final int musicId, int groupId) {
		// 构造musicItem对象
		musicItem = new MusicItem();
		musicItem.setGroupId(groupId);
		musicItem.setMusicId(musicId);
		// 添加数据到musicitem表
		musicItemDao.addMusicItem(musicItem);
	}

	public void goplay(int position) {
		Intent intent = new Intent(Constant.ACTION_JUMR);
		intent.putExtra("position", position);
		context.sendBroadcast(intent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Refresh(Object... obj) {
		int FLAG = Integer.parseInt(String.valueOf(obj[0]));
		if (FLAG == MULTCHOSE) {
			localistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			adapter.setmultchosemode(true);
		} else if (FLAG == STAR_ADDGROUP) {
			Iterator i = adapter.getchoseMap().values().iterator();
			while (i.hasNext()) {
				Music o = (Music) i.next();
				musicid.add(o.getId());
			}
			AddToGroup(context, 0, true, musicid);
			handler.sendEmptyMessage(10);
		} else if (SINGLE_CHOSE == FLAG) {
			adapter.setmultchosemode(false);
		} else if (CHANGE_LIST == FLAG) {
			adapter.addItems((ArrayList<Music>) obj[1]);
		}
	}

}
