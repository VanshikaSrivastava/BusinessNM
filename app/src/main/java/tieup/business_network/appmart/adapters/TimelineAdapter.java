package tieup.business_network.appmart.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import tieup.business_network.appmart.R;
import tieup.business_network.appmart.models.TimelineModel;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private Context mcontext;
    private List<TimelineModel> post_list;
    TimelineModel poJo;
    ViewHolder holder;
    ImageView imgFullSize;

    public TimelineAdapter(Context mcontext, List<TimelineModel> post_List) {
        this.mcontext = mcontext;
        post_list = post_List;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.activity_timeline_card, null);
        holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        poJo = post_list.get(position);
        holder.txtName.setText(poJo.getUser_name());
        holder.txtTitle.setText(poJo.getPost_title());
        holder.txtDescription.setText(poJo.getPost_description());
        holder.txtDate.setText(poJo.getPost_datee());
        holder.txtTime.setText(poJo.getPost_time());
        holder.setIsRecyclable(false);
        if(!poJo.getPost_image().equals("--")) {
            Picasso.with(mcontext).invalidate(poJo.getPost_image());
            Picasso.with(mcontext).load(poJo.getPost_image()).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.gradient).fit().centerCrop().error(R.drawable.gradient).into(holder.imgPost);
        }
        else{
           holder.imgPost.getLayoutParams().height = 0;
        }
        holder.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.image_dialog);
                dialog.setCancelable(true);
                imgFullSize = dialog.findViewById(R.id.imgFullSize);

                try {
                    URL url = new URL(post_list.get(position).getPost_image());
                    Bitmap bt = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    imgFullSize.setImageBitmap(bt);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return post_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDescription, txtName, txtDate, txtTime;
        ImageView imgPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            txtName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);

            imgPost = itemView.findViewById(R.id.imgPost);




        }

    }
}
