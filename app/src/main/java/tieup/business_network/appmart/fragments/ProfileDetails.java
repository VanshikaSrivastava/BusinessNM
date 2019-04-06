package tieup.business_network.appmart.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import tieup.business_network.appmart.CircleTransform;
import tieup.business_network.appmart.R;

public class ProfileDetails extends Fragment {
    TextView txtUserName, txtBusinessTitle, txtBusinessCategory, txtPlace, txtMobileno, txtWhatsappno, txtMail, txtWebsite, txtAbtBusiness, txtBusinessYear;
    ImageView imgProfileDetails;
    private CollapsingToolbarLayout collapsing_toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_details_fragment, container, false);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        txtUserName = view.findViewById(R.id.txtUserName);
        txtBusinessTitle = view.findViewById(R.id.txtBusinessTitle);
        txtBusinessCategory = view.findViewById(R.id.txtBusinessCategory);
        txtPlace = view.findViewById(R.id.txtPlace);
        txtMobileno = view.findViewById(R.id.txtMobileno);
        txtWhatsappno = view.findViewById(R.id.txtWhatsappno);
        txtMail = view.findViewById(R.id.txtMail);
        txtWebsite = view.findViewById(R.id.txtWebsite);
        txtAbtBusiness = view.findViewById(R.id.txtAbtBusiness);
        txtBusinessYear = view.findViewById(R.id.txtBusinessYear);
        imgProfileDetails = view.findViewById(R.id.imgProfileDetails);

        collapsing_toolbar = view.findViewById(R.id.collapsing_toolbar);
        //collapsing_toolbar.setTitle("Profile");

        String name = getArguments().getString("user_name");
        String business_title = getArguments().getString("business_title");
        String business_category = getArguments().getString("business_category");
        String place = getArguments().getString("place");
        String user_mobile_number = getArguments().getString("user_mobile_number");
        String user_whatsapp_number = getArguments().getString("user_whatsapp_number");
        String user_mail = getArguments().getString("user_mail");
        String user_website = getArguments().getString("user_website");
        String about_business = getArguments().getString("about_business");
        String business_year = getArguments().getString("business_year");
        String user_Profile = getArguments().getString("user_profile");

        txtUserName.setText(name);
        txtBusinessTitle.setText(business_title);
        txtBusinessCategory.setText(business_category);
        txtPlace.setText(place);
        txtMobileno.setText(user_mobile_number);
        txtWhatsappno.setText(user_whatsapp_number);
        txtMail.setText(user_mail);
        txtWebsite.setText(user_website);
        txtAbtBusiness.setText(about_business);
        txtBusinessYear.setText(business_year  +"year");

        if(user_Profile.isEmpty()){
            imgProfileDetails.setImageResource(R.drawable.gradient);
        }else {
            Picasso.with(getContext()).invalidate(user_Profile);
            Picasso.with(getContext()).load(user_Profile).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).fit().centerCrop().error(R.drawable.gradient).into(imgProfileDetails);
        }
        return view;

    }
}
