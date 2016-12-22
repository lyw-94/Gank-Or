package com.sdust.gankor.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Liu Yongwei on 2016/12/23.
 *
 * @description:
 */

public class ZhihuDailyNews {
    private String date;
    /**
     * images : ["http://pic1.zhimg.com/c27847ba9ac7c11ce195bc5155357bf4.jpg"]
     * type : 0
     * id : 8656865
     * ga_prefix : 080809
     * title : 从经济学角度来看，「走一步看一步」是不是能达到最优？
     */

    private List<StoriesBean> stories;
    /**
     * image : http://pic1.zhimg.com/988f127baf4dd0885e54994e5c2d8a08.jpg
     * type : 0
     * id : 8660448
     * ga_prefix : 080807
     * title : 读读日报 24 小时热门 TOP 5 · 地铁公交上班族如何读书？
     */

    @SerializedName("top_stories")
    private List<TopStoriesBean> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<TopStoriesBean> topStories) {
        this.topStories = topStories;
    }

    public static class StoriesBean {
        private int type;
        private int id;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        private String image;
        private int type;
        private int id;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
