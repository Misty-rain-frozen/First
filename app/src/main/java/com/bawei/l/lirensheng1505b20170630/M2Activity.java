package com.bawei.l.lirensheng1505b20170630;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * 类描述：轮播图 多条目展示
 * 创建人: 李仁晟
 * 创建时间 L on 2017/6/30.
 */
public class M2Activity extends AppCompatActivity implements XListView.IXListViewListener {
    private Banner banner;
    private List<PicLun.ListBean> list;
    private List<String> imagelist;
    private XListView xListView;
    private MyAdapter adapter;
    private int page = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String s = msg.obj.toString();
            Gson gson = new Gson();
            PicLun picLun = gson.fromJson(s, PicLun.class);
            list.addAll(picLun.getList());
            adapter.notifyDataSetChanged();
            List<PicLun.ListBean> beee = picLun.getList();
            imagelist = new ArrayList<>();
            for (PicLun.ListBean bean : beee) {
                String pic = bean.getPic();
                String[] split = pic.split("\\|");
                imagelist.add(split[1]);
            }
            banner.setImages(imagelist);
            banner.start();
            stopXlist();
        }

        private void stopXlist() {
            xListView.stopLoadMore();
            xListView.stopRefresh();
            xListView.setRefreshTime("刚刚");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2activity);
        add();
        loadDate();
    }
   // 网络请求
    private void loadDate() {
        new Thread() {
            @Override
            public void run() {
                String urlConnect = Net.getUrlConnect("http://qhb.2dyt.com/Bwei/news?type=5&postkey=1503d&page=" + page);
                Message message = Message.obtain();
                message.obj = urlConnect;
                handler.sendMessage(message);
            }
        }.start();
    }
 // 寻找控件ID
    private void add() {
        list = new ArrayList<>();
        xListView = (XListView) findViewById(R.id.xlist);
        adapter=new MyAdapter();
        xListView.setAdapter(adapter);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new ImageL(this));
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);

    }

    @Override
    public void onRefresh() {
        page++;
        list.clear();
        loadDate();
    }

    @Override
    public void onLoadMore() {
        page++;
        loadDate();

    }
 // 适配器
    class MyAdapter extends BaseAdapter {
        final int TYPE1 = 0;
        final int TYPE2 = 1;
        ImageLoader imageLoader;
        DisplayImageOptions options;

        public MyAdapter() {
            imageLoader = ImageLoader.getInstance();
//            File file = new File(Environment.getExternalStorageDirectory(), "Bawei");
//            if (!file.exists())
//                file.mkdir();
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(M2Activity.this);
            imageLoader.init(configuration);
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher).cacheOnDisk(true).build();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).getType() == 1 ? TYPE1 : TYPE2;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                switch (type) {
                    case TYPE1:
                        view = View.inflate(M2Activity.this, R.layout.item, null);
                        holder.textView = (TextView) view.findViewById(R.id.t1);
                        holder.image1 = (ImageView) view.findViewById(R.id.i1);
                        holder.image2 = (ImageView) view.findViewById(R.id.i2);
                        break;

                    case TYPE2:
                        view = View.inflate(M2Activity.this, R.layout.item2, null);
                        holder.textView = (TextView) view.findViewById(R.id.t1);
                        holder.image3 = (ImageView) view.findViewById(R.id.i3);
                        holder.image4 = (ImageView) view.findViewById(R.id.i4);
                        holder.image5 = (ImageView) view.findViewById(R.id.i5);
                        holder.image6 = (ImageView) view.findViewById(R.id.i6);
                        break;
                }
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            PicLun.ListBean bean = list.get(i);
            holder.textView.setText(bean.getTitle());
            switch (type) {
                case TYPE1:
                    String pic = bean.getPic();
                    String[] split = pic.split("\\|");
                    imageLoader.displayImage(split[0], holder.image1, options);
                    imageLoader.displayImage(split[1], holder.image2, options);
                    break;
                case TYPE2:
                    String pic2 = bean.getPic();
                    String[] split2 = pic2.split("\\|");
                    imageLoader.displayImage(split2[0], holder.image3, options);
                    imageLoader.displayImage(split2[1], holder.image4, options);
                    imageLoader.displayImage(split2[0], holder.image5, options);
                    imageLoader.displayImage(split2[1], holder.image6, options);
                    break;
            }
            return view;
        }
    }

    class ViewHolder {
        TextView textView;
        ImageView image1, image2, image3, image4,image5,image6;
    }

}
