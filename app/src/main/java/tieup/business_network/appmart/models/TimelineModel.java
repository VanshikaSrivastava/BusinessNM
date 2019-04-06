package tieup.business_network.appmart.models;

public class TimelineModel {
    private String user_name,post_title,post_description,post_image,post_datee,post_time;

    public TimelineModel(String user_name, String post_title, String post_description, String post_image, String post_datee, String post_time) {
        this.user_name = user_name;
        this.post_title = post_title;
        this.post_description = post_description;
        this.post_image = post_image;
        this.post_datee = post_datee;
        this.post_time = post_time;
    }

    public String getPost_title() {
        return post_title;
    }

    public String getPost_description() {
        return post_description;
    }

    public String getPost_image() {
        return post_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPost_datee() {
        return post_datee;
    }

    public String getPost_time() {
        return post_time;
    }
}
