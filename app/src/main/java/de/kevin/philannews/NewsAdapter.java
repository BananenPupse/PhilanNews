package de.kevin.philannews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private List<News> news;
    private LayoutInflater layoutInflater;

    NewsAdapter(Context context, List<News> news) {
        this.news = news;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
            convertView.setMinimumHeight(100);
            holder = new ViewHolder();
            holder.titleView = convertView.findViewById(R.id.textTitle);
            holder.contentView = convertView.findViewById(R.id.textContent);
            holder.button = convertView.findViewById(R.id.buttonShow);
            holder.editButton = convertView.findViewById(R.id.buttonEdit);
            holder.layout = convertView.findViewById(R.id.list_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.layout.setClickable(true);
        holder.layout.setLongClickable(true);
        News _news = news.get(position);
        holder.titleView.setText(_news.getTitle());
        holder.contentView.setText(_news.getContent());
        holder.button.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.GONE);

        if (NewsManager.validUser) holder.layout.setOnLongClickListener(v -> {
            Intent editNews = new Intent(NewsActivity.getNewsActivity(), EditNewsActivity.class);
            EditNewsActivity.setData(_news.getId(), _news.getTitle(), _news.getLink(), _news.getContent());
            NewsActivity.getNewsActivity().startActivity(editNews);
            return true;
        });
        holder.layout.setOnClickListener(v -> {
//            Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
//            openURL.setData(Uri.parse(_news.getLink()));
//            NewsActivity.getNewsActivity().startActivity(openURL);
            Intent openNews = new Intent(NewsActivity.getNewsActivity(), NewsViewActivity.class);
            openNews.setAction(_news.getLink());
            NewsActivity.getNewsActivity().startActivity(openNews);
        });

//        holder.button.setOnClickListener(v -> {
//            Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
//            openURL.setData(Uri.parse(_news.getLink()));
//            NewsActivity.getNewsActivity().startActivity(openURL);
//        });

//        holder.editButton.setOnClickListener(v -> {
//            Intent editNews = new Intent(NewsActivity.getNewsActivity(), EditNewsActivity.class);
//            EditNewsActivity.setData(_news.getId(), _news.getTitle(), _news.getLink(), _news.getContent());
//            NewsActivity.getNewsActivity().startActivity(editNews);
//        });

        return convertView;
    }

    public static class News {
        String title;
        String link;
        String content;
        String id;

        News(String id, String title, String link, String content) {
            this.id = id;
            this.title = title;
            this.link = link;
            this.content = content;
        }

        public String getId() {
            return id;
        }

        String getTitle() {
            return title;
        }

        String getLink() {
            return link;
        }

        String getContent() {
            return content;
        }
    }

    static class ViewHolder {
        TextView titleView;
        TextView contentView;
        Button button;
        Button editButton;
        ConstraintLayout layout;
    }
}
