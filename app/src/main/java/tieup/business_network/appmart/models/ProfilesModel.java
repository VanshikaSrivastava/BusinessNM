package tieup.business_network.appmart.models;

import android.annotation.SuppressLint;

import tieup.business_network.appmart.fragments.AllProfiles;

@SuppressLint("ValidFragment")
public class ProfilesModel extends AllProfiles{

    private String user_id,user_name,business_title,business_category,place,user_mobile_number,user_whatsapp_number,user_mail,user_website,about_business,business_year,user_profile;

    public ProfilesModel(String user_name, String business_title, String business_category, String place, String user_mobile_number, String user_whatsapp_number, String user_mail, String user_website, String about_business, String business_year, String user_profile) {
       // this.user_id = user_id;
        this.user_name = user_name;
        this.business_title = business_title;
        this.business_category = business_category;
        this.place = place;
        this.user_mobile_number = user_mobile_number;
        this.user_whatsapp_number = user_whatsapp_number;
        this.user_mail = user_mail;
        this.user_website = user_website;
        this.about_business = about_business;
        this.business_year = business_year;
        this.user_profile = user_profile;
    }

   /* public String getUser_id() {
        return user_id;
    }
*/
    public String getUser_name() {
        return user_name;
    }

    public String getBusiness_title() {
        return business_title;
    }

    public String getBusiness_category() {
        return business_category;
    }

    public String getUser_mobile_number() {
        return user_mobile_number;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public String getPlace() {
        return place;
    }

    public String getUser_whatsapp_number() {
        return user_whatsapp_number;
    }

    public String getUser_website() {
        return user_website;
    }

    public String getAbout_business() {
        return about_business;
    }

    public String getBusiness_year() {
        return business_year;
    }
}

