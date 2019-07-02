package de.kevin.philannews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            holder.date = convertView.findViewById(R.id.textDate);
            holder.layout = convertView.findViewById(R.id.list_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.layout.setClickable(true);
        holder.layout.setLongClickable(true);
        News _news = news.get(position);
        holder.titleView.setText(_news.getTitle());
        holder.contentView.setText(_news.getSummary());
        holder.date.setText(_news.getDate());

        if (NewsManager.validUser) holder.layout.setOnLongClickListener(v -> {
            Intent editNews = new Intent(NewsActivity.getNewsActivity(), EditNewsActivity.class);
            EditNewsActivity.setData(_news.getId(), _news.getTitle(), _news.getSummary(), _news.getContent());
            NewsActivity.getNewsActivity().startActivity(editNews);
            return true;
        });
        holder.layout.setOnClickListener(v -> {
            Intent openNews = new Intent(NewsActivity.getNewsActivity(), NewsViewActivity.class);
            openNews.putExtra("title", _news.getTitle());
            openNews.putExtra("content", _news.getContent());
            openNews.putExtra("author", _news.getAuthor());
            openNews.putExtra("date", _news.getDate());
            NewsActivity.getNewsActivity().startActivity(openNews);
        });
        return convertView;
    }

    public static class News {
        String title, summary, content, id, author, date;

        News(String id, String title, String summary, String content, String author, String date) {
            this.id = id;
            this.title = title;
            this.summary = summary;
            this.content = content;
            this.author = author;
            this.date = date;
        }

        public String getId() {
            return id;
        }

        String getTitle() {
            return title;
        }

        String getSummary() {
            return summary;
        }

        String getAuthor() {
            return author;
        }

        String getDate() {
            return date;
        }

        String getContent() {
            return content;
        }
    }

    static class ViewHolder {
        TextView titleView;
        TextView contentView;
        TextView date;
        ConstraintLayout layout;
    }
}
