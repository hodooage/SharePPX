/**
  * Copyright 2018 bejson.com 
  */
package com.example.z1310_000.sharedppx.entity;

import java.util.List;

/**
 * Auto-generated: 2018-06-01 11:35:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class WallPaper {

    private String msg;
    private Res res;
    private int code;
    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setRes(Res res) {
         this.res = res;
     }
     public Res getRes() {
         return res;
     }

    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    @Override
    public String toString() {
        return "WallPaper{" +
                "msg='" + msg + '\'' +
                ", res=" + res +
                ", code=" + code +
                '}';
    }

    /**
     * Auto-generated: 2018-06-01 11:35:58
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Res {

        private List<Vertical> vertical;
        public void setVertical(List<Vertical> vertical) {
             this.vertical = vertical;
         }
         public List<Vertical> getVertical() {
             return vertical;
         }
        /**
         * Auto-generated: 2018-06-01 11:35:58
         *
         * @author bejson.com (i@bejson.com)
         * @website http://www.bejson.com/java2pojo/
         */
        public class Vertical {

            private int views;
            private int ncos;
            private int rank;
            private List<String> tag;
            private String wp;
            private boolean xr;
            private boolean cr;
            private int favs;
            private long atime;
            private String id;
            private String desc;
            private String thumb;
            private String img;
            private List<String> cid;
            private List<String> url;
            private String rule;
            private String preview;
            private String store;
            public void setViews(int views) {
                this.views = views;
            }
            public int getViews() {
                return views;
            }

            public void setNcos(int ncos) {
                this.ncos = ncos;
            }
            public int getNcos() {
                return ncos;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }
            public int getRank() {
                return rank;
            }

            public void setTag(List<String> tag) {
                this.tag = tag;
            }
            public List<String> getTag() {
                return tag;
            }

            public void setWp(String wp) {
                this.wp = wp;
            }
            public String getWp() {
                return wp;
            }

            public void setXr(boolean xr) {
                this.xr = xr;
            }
            public boolean getXr() {
                return xr;
            }

            public void setCr(boolean cr) {
                this.cr = cr;
            }
            public boolean getCr() {
                return cr;
            }

            public void setFavs(int favs) {
                this.favs = favs;
            }
            public int getFavs() {
                return favs;
            }

            public void setAtime(long atime) {
                this.atime = atime;
            }
            public long getAtime() {
                return atime;
            }

            public void setId(String id) {
                this.id = id;
            }
            public String getId() {
                return id;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
            public String getDesc() {
                return desc;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }
            public String getThumb() {
                return thumb;
            }

            public void setImg(String img) {
                this.img = img;
            }
            public String getImg() {
                return img;
            }

            public void setCid(List<String> cid) {
                this.cid = cid;
            }
            public List<String> getCid() {
                return cid;
            }

            public void setUrl(List<String> url) {
                this.url = url;
            }
            public List<String> getUrl() {
                return url;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }
            public String getRule() {
                return rule;
            }

            public void setPreview(String preview) {
                this.preview = preview;
            }
            public String getPreview() {
                return preview;
            }

            public void setStore(String store) {
                this.store = store;
            }
            public String getStore() {
                return store;
            }

        }
    }


}