package com.vansh.newsaroundyou.Miscellaneous;

public class NewsModel implements Comparable<NewsModel> {
    private String title, category, author, content, publisher, urlToImage, publishedAt, timeAgo, url;

    public NewsModel(String title, String category, String author, String content, String publisher, String urlToImage, String publishedAt, String timeAgo, String url) {
        this.title = title;
        this.category = category;
        this.author = author;
        this.content = content;
        this.publisher = publisher;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.url = url;
        this.timeAgo = timeAgo;
    }

    public NewsModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", publisher='" + publisher + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(NewsModel newsModel) {
        return this.title.compareTo(newsModel.getTitle());

    }
}
