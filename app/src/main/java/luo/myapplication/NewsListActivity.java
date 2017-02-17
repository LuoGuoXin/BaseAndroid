package luo.myapplication;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;
import org.byteam.superadapter.animation.ScaleInAnimation;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import luo.library.base.base.BaseActivity;
import luo.library.base.base.BaseHttp;
import luo.library.base.base.BaseImage;
import luo.library.base.utils.GsonUtil;
import luo.myapplication.model.NewsList;


/**
 * 网络请求示例
 */
@ContentView(R.layout.activity_news_list)
public class NewsListActivity extends BaseActivity {

    @ViewInject(R.id.recyclerView)
    SuperRecyclerView recyclerView;
    int page = 1;
    List<NewsList> lists = new ArrayList<NewsList>();
    SuperAdapter<NewsList> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("新闻列表");
        init();
        http();
    }

    void init() {
        adapter = new SuperAdapter<NewsList>(NewsListActivity.this, lists, R.layout.item_news_list) {

            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, NewsList item) {
                holder.setText(R.id.title, item.getT_title());
                ImageView imageView = holder.findViewById(R.id.image);
                String imgs[] = item.getP_file().split(",");
                BaseImage.getInstance().displayImage(NewsListActivity.this, imgs[0], imageView);
            }
        };
        adapter.enableLoadAnimation(300, new ScaleInAnimation());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                http();
            }
        });
        recyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                page++;
                http();
            }
        }, 1);
    }

    void http() {
        RequestParams params = new RequestParams("http://www.zs21.cn/zswjk/queryTender");
        params.addQueryStringParameter("pageNo", String.valueOf(page));
        BaseHttp.get(params, new BaseHttp.BaseHttpCallback() {
            @Override
            public void onSuccess(String result) {
                lists = GsonUtil.GsonToList(result, NewsList.class);
                if (page == 1) {
                    adapter.replaceAll(lists);
                } else {
                    adapter.addAll(lists);
                }
                if (lists.size() == 0) {
                    showToast("加载完毕");
                }
            }

            @Override
            public void onError(int code, String message) {
                showToast(message);
            }
        });
    }
}
