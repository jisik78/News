package com.example.administrator.news.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.news.R;
import com.example.administrator.news.adapter.MyNewsCollectAdapter;
import com.example.administrator.news.entity.NewsCollect;
import com.example.administrator.news.utils.MyOrmliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class LikeFragment extends Fragment{


    private MyNewsCollectAdapter adapter;
    private List<NewsCollect> datas;
    private Dao<NewsCollect, Long> dao;
    private ListView listview;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_listview_fragment_collect_activity_main, null);
        listview = (ListView) layout.findViewById(R.id.listview_fragment_collect_activity_main);

        try {
            dao = MyOrmliteOpenHelper.getInstance(getContext()).getDao(NewsCollect.class);
            datas = dao.queryForAll();
            if(datas != null){
            adapter = new MyNewsCollectAdapter(datas, getContext());
            listview.setAdapter(adapter);
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListener();
    }


    private void initListener() {
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final NewsCollect newsCollect = datas.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("要删除吗？");
                builder.setIcon(R.drawable.flower);
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            dao = MyOrmliteOpenHelper.getInstance(getContext()).getDao(NewsCollect.class);
                            dao.delete(newsCollect);

                            datas = dao.queryForAll();
                            adapter.setDatas(datas);
                            adapter.notifyDataSetChanged();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();

                return true;
            }
        });
    }

    //Fragment 监听 隐藏改变
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden){
            try {
                dao = MyOrmliteOpenHelper.getInstance(getContext()).getDao(NewsCollect.class);
                datas = dao.queryForAll();
                adapter.setDatas(datas);
                adapter.notifyDataSetChanged();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*//有bug
    //创建toolbar右上角 查询 图标 菜单选项
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.query_db_like_fragment_activity_main);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getDatas().clear();
                adapter.notifyDataSetChanged();

                try {
                    dao = MyOrmliteOpenHelper.getInstance(getContext()).getDao(NewsCollect.class);

                    dao.queryBuilder().where().like("title",newText);
                    datas = dao.queryForAll();
                    adapter.setDatas(datas);
                    adapter.notifyDataSetChanged();

                } catch (SQLException e) {
                }

                return false;
            }
        });

    }*/
}
