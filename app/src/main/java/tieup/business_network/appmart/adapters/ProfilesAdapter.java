package tieup.business_network.appmart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import tieup.business_network.appmart.R;
import tieup.business_network.appmart.models.ProfilesModel;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ViewHolder> {

    private Context mcontext;
    private List<ProfilesModel> profile_list;

    public ProfilesAdapter(Context mcontext, List<ProfilesModel> profileList) {
        this.mcontext = mcontext;
        profile_list = profileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view =inflater.inflate(R.layout.fragment_all_profiles_card,null);
        ViewHolder holder =new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProfilesModel poJo=profile_list.get(position);
        holder.txtUserName.setText(poJo.getUser_name());
        holder.txtBusinessTitle.setText(poJo.getBusiness_title());
        holder.txtUserMail.setText(poJo.getUser_mail());
        holder.txtMobileno.setText(poJo.getUser_mobile_number());
        if(poJo.getUser_profile().isEmpty()){
            holder.imgProfileList.setImageResource(R.drawable.gradient);
        }
        else {
            Picasso.with(mcontext).invalidate(poJo.getUser_profile());
            Picasso.with(mcontext).load(poJo.getUser_profile()).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.gradient).fit().centerCrop().error(R.drawable.gradient).into(holder.imgProfileList);
        }
        }

    @Override
    public int getItemCount() {
        return profile_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtUserName,txtBusinessTitle,txtUserMail,txtMobileno;
        ImageView imgProfileList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtBusinessTitle=itemView.findViewById(R.id.txtBusinessTitle);
            txtUserMail=itemView.findViewById(R.id.txtUserMail);
            txtMobileno=itemView.findViewById(R.id.txtMobileno);
            imgProfileList=itemView.findViewById(R.id.imgProfileList);

        }
    }
}
