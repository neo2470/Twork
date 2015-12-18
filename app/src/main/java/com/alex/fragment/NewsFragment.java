package com.alex.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.entity.News;
import com.alex.twork.R;
import com.google.cache.ImageCache;
import com.google.cache.ImageFetcher;
import com.google.cache.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alex on 15-9-21.
 * News list
 */
public class NewsFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(null == news) {
            news = new ArrayList<>();
        } else {
            news.clear();
        }

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);

        mImageFetcher = new ImageFetcher(getActivity(), 300, 200);
        mImageFetcher.setLoadingImage(R.drawable.news_image_holder);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);

        if(null == news) {
            news = new ArrayList<>();
        } else {
            news.clear();
        }

        final ListView newsList = (ListView) view.findViewById(R.id.newsList);
        View header = inflater.inflate(R.layout.news_divider_view, null, false);

        View footer = inflater.inflate(R.layout.news_footer, null, false);
        footerText = (TextView) footer.findViewById(R.id.footerText);
        footerLoadCircle = (ProgressBar) footer.findViewById(R.id.footerLoadCircle);
        newsAdapter = new NewsListAdapter(news, mImageFetcher);
        newsList.addHeaderView(header);
        newsList.addFooterView(footer);
        newsList.setAdapter(newsAdapter);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News item = (News) parent.getAdapter().getItem(position);

                if(null != item) {
                    Uri page = Uri.parse(item.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, page);

                    PackageManager pkgM = getActivity().getPackageManager();
                    List<ResolveInfo> acts = pkgM.queryIntentActivities(intent, 0);

                    if(0 < acts.size()) {
                        startActivity(intent);
                    }
                }
            }
        });
        newsList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(SCROLL_STATE_IDLE == scrollState) {
                    final int lvp = newsList.getLastVisiblePosition();
                    final int size = news.size();
                    if(lvp == size + 1) {
                        queryNewsByShowAPI(++currentPageIndex);
                    }
                }

                if(SCROLL_STATE_FLING == scrollState) {
                    if(!Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });

        // 下拉刷新组件
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryNewsByShowAPI(1);
            }
        });

        queryNewsByShowAPI(currentPageIndex++);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    /**
     * 获取最新财经新闻
     * @param page 0，表示首次请求新闻数据；1，表示下拉刷新请求数据；>1表示上拉加载更多数据
     */
    private void queryNewsByShowAPI(final int page) {

        final int allPage = QueryLatestNews.getNewsPages();

        // 请求页码超出了最大页码，无效请求，忽略之
        if(allPage < page) {
            return;
        }

        new QueryLatestNews(news) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                // 下拉加载最新数据
                if(1 == page) {
                    refreshLayout.setRefreshing(true);
                }

                // 上拉加载更多数据
                if(1 < page) {
                    footerText.setVisibility(View.GONE);
                    footerLoadCircle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);

                // 下拉加载最新数据
                if(1 == page) {
                    refreshLayout.setRefreshing(false);
                }

                // 上拉加载更多数据
                if(1 < page) {

                    if (allPage == page) {
                        footerText.setText(getString(R.string.footer_no_move));
                    }

                    if(allPage > page){
                        footerText.setText(getString(R.string.footer_load_move));
                    }

                    footerText.setVisibility(View.VISIBLE);
                    footerLoadCircle.setVisibility(View.GONE);
                }

                newsAdapter.notifyDataSetChanged();
            }
        }.execute(page);
    }

    private int currentPageIndex;
    private ArrayList<News> news;

    private TextView footerText;
    private ProgressBar footerLoadCircle;

    private ImageFetcher mImageFetcher;
    private NewsListAdapter newsAdapter;
    private SwipeRefreshLayout refreshLayout;

    private static final String IMAGE_CACHE_DIR = "cache";

    class NewsListAdapter extends BaseAdapter {

        public NewsListAdapter(ArrayList<News> news, ImageFetcher mImageFetcher) {
            this.news = news;
            this.mImageFetcher = mImageFetcher;
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {

            int index = position;
            if(news.size() <= index) {
                index = news.size() - 1;
            }

            return news.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if(null == convertView) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.news_item, null);

                holder = new ViewHolder();
                holder.newsImg = (ImageView) convertView.findViewById(R.id.newsImg);
                holder.newsTitle = (TextView) convertView.findViewById(R.id.newsTitle);
                holder.newsDesc = (TextView) convertView.findViewById(R.id.newsDesc);
                holder.newsTimeASource = (TextView) convertView.findViewById(R.id.newsTimeASource);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            News item = news.get(position);

            if(item.hasImage()) {
                mImageFetcher.loadImage(item.getImgUrl(), holder.newsImg);
                holder.newsImg.setVisibility(View.VISIBLE);
            } else {
                holder.newsImg.setVisibility(View.GONE);
            }

            if(item.getDesc().isEmpty()) {
                holder.newsDesc.setVisibility(View.GONE);
            } else {
                holder.newsDesc.setVisibility(View.VISIBLE);
            }

            holder.newsTitle.setText(item.getTitle());
            holder.newsDesc.setText(item.getDesc());

            String timeSource = getString(R.string.news_time_source);
            timeSource = String.format(timeSource, item.getPubDateFromNow(getActivity()), item.getSource());
            holder.newsTimeASource.setText(timeSource);

            if(!item.isAppeared()) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.up_from_bottom);
                convertView.startAnimation(anim);
                item.setAppeared(true);
            }

            return convertView;
        }

        private ArrayList<News> news;
        private ImageFetcher mImageFetcher;
    }

    private static  class ViewHolder {
        ImageView newsImg;
        TextView newsTitle;
        TextView newsDesc;
        TextView newsTimeASource;
    }
}

class QueryLatestNews extends AsyncTask<Integer, Void, Boolean> {

    public QueryLatestNews(ArrayList<News> news) {
        this.news = news;
        NEWS_PAGES = Integer.MAX_VALUE;
        if (null == cache) {
            cache = new ArrayList<>();
        } else {
            cache.clear();
        }
    }

    public static int getNewsPages() {
        return NEWS_PAGES;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {

        page = params[0];
        int pageCp = page;
        if (0 >= page) {
            pageCp = 1;
        }

        HashMap<String, String> header = new HashMap<>();
        header.put(SHOW_API_KEY, SHOW_API_API_VALUE);

        boolean result = false;// 查询数据的结果

        do {

            final String newsUrl = getNewsUrl(SHOW_API_CHANNEL_ID, pageCp);
            final String content = getWebContent(newsUrl, header, SHOW_API_CHARSET);
            final Status status = fromJSON(content);

//            Log.d("Print-News-While", pageCp + ", " + status + " ============================");

            if (1 == page) {
                if (Status.refreshSuccess == status) {//下拉刷新成功
                    result = true;
                    break;
                } else if (Status.networkError == status || Status.queryFailure == status) {// 下拉刷新失败
                    break;
                } else {

                    // 为了确保下拉刷新得到的数据和本地已经下载到数据相互衔接
                    // 需要找到nid与本地最新数据nid相同的那条数据，这样才算
                    // 一次下拉刷新成功
                    ++pageCp;
                }
            } else {// 不是下拉刷新则跳出循环
                result = Status.querySuccess == status;
                break;
            }

        } while (true);

        return result;
    }

    private Status fromJSON(String content) {
        Status flag = Status.querySuccess;
        try {
            JSONObject data = new JSONObject(content);

            final String resBodyKey = "showapi_res_body";
            if (!data.has(resBodyKey)) {
                return Status.networkError;
            }

            JSONObject resBody = data.optJSONObject(resBodyKey);
            JSONObject pageBean = resBody.optJSONObject("pagebean");
            JSONArray contentList = pageBean.optJSONArray("contentlist");

            NEWS_PAGES = pageBean.optInt("allPages");
            Log.d("Debug-pageNum", NEWS_PAGES+"");

            final int contentSize = contentList.length();
            if (0 < contentSize) {
                for (int i = 0; i < contentSize; ++i) {
                    JSONObject newsObj = contentList.optJSONObject(i);
//                    Log.d("Print-News-Json", newsObj.toString());

                    if (1 == page) {

                        // TODO 下拉刷新 not be tested
                        final String nid = newsObj.optString("nid");
                        News latestNews = news.get(0);
//                        Log.d("Print-News-Foreach", nid + " , " + latestNews.getNid() + ", " + nid.equals(latestNews.getNid()));
                        if (nid.equals(latestNews.getNid())) {
                            news.addAll(0, cache);
                            cache.clear();
                            flag = Status.refreshSuccess;
                            break;
                        } else {
                            cache.add(new News(newsObj));
                        }

                    } else {
                        news.add(new News(newsObj));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            flag = Status.queryFailure;
        }

        return flag;
    }

    /**
     * 读取一张网页的内容
     * @param webUrl 网页对应的URL
     * @return 网页内容字符串
     */
    private String getWebContent(String webUrl, HashMap<String, String> header, String charset) {
        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            if(null != header) {
                for(String key : header.keySet()) {
                    urlConnection.setRequestProperty(key, header.get(key));
                }
            }

            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));

            String line;
            while (null != (line=bufferedReader.readLine())) {
                builder.append(line);
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return builder.toString();
    }

    private String getNewsUrl(String channelID, int page) {
        StringBuilder builder = new StringBuilder();
        builder.append(SHOW_API_NEWS_URL);

        // 频道ID
        builder.append("channelId");
        builder.append("=");
        builder.append(channelID);
        builder.append("&");

        // 请求的页码
        builder.append("page");
        builder.append("=");
        builder.append(page);

        return builder.toString();
    }

    private enum Status {
        networkError,// 网络错误，获取数据失败
        refreshSuccess,// 下拉刷新成功（要加载）
        querySuccess,// 加载数据成功（上拉加载数据）
        queryFailure// 加载数据失败
    }

    private int page;
    private ArrayList<News> news;
    private static int NEWS_PAGES;// 新闻的总页数（每页约20条数据）
    private static ArrayList<News> cache;// 存储下拉刷新得到的数据

    private final String SHOW_API_CHARSET = "utf-8";
    private final String SHOW_API_KEY = "apikey";
    private final String SHOW_API_API_VALUE = "7099530a107f136565aa4e1dafc3f74f";
    private final String SHOW_API_CHANNEL_ID = "5572a108b3cdc86cf39001cd";
    private final String SHOW_API_NEWS_URL = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news?";
}

