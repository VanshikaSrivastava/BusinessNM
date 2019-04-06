package tieup.business_network.appmart.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import tieup.business_network.appmart.models.CategorySearchModel;
import tieup.business_network.appmart.R;

import static tieup.business_network.appmart.fragments.BusinessProfile.dialog;
import static tieup.business_network.appmart.fragments.BusinessProfile.txtBusinessCategory;

public class SearchListViewAdapter extends BaseAdapter implements Filterable {

    Context c;
    ArrayList<CategorySearchModel> searchModels;
    CustomFilter filter;
    ArrayList<CategorySearchModel> filterList;


    public SearchListViewAdapter(Context ctx, ArrayList<CategorySearchModel> searchModels) {
        // TODO Auto-generated constructor stub

        this.c = ctx;
        this.searchModels = searchModels;
        this.filterList = searchModels;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return searchModels.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return searchModels.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return searchModels.indexOf(getItem(pos));
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.categories_list, null);
        }

        TextView nameTxt = (TextView) convertView.findViewById(R.id.txtCategory);

        //SET DATA TO THEM
        nameTxt.setText(searchModels.get(pos).getCategories());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtBusinessCategory.setText(searchModels.get(pos).getCategories());
                dialog.hide();
            }
        });

        return convertView;
    }


    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) {
            filter = new CustomFilter();
        }

        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase();

                ArrayList<CategorySearchModel> filters = new ArrayList<CategorySearchModel>();

                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getCategories().toUpperCase().contains(constraint)) {
                        CategorySearchModel p = new CategorySearchModel(filterList.get(i).getCategories());

                        filters.add(p);
                    }
                }

                results.count = filters.size();
                results.values = filters;

            } else {
                results.count = filterList.size();
                results.values = filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub

            searchModels = (ArrayList<CategorySearchModel>) results.values;
            notifyDataSetChanged();
        }

    }

}