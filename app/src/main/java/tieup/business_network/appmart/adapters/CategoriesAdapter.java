package tieup.business_network.appmart.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tieup.business_network.appmart.DBHelper;
import tieup.business_network.appmart.fragments.AllProfiles;
import tieup.business_network.appmart.models.CategorySearchModel;
import tieup.business_network.appmart.R;


public class CategoriesAdapter extends BaseAdapter implements Filterable {

    Context c;
    ArrayList<CategorySearchModel> category_model;
    CustomFilter filter;
    Fragment fragment;
    ArrayList<CategorySearchModel> filterList;
    private static int lastClickedItem = -1;
    DBHelper mydb;

    public CategoriesAdapter(Context ctx, ArrayList<CategorySearchModel> model_category) {
        // TODO Auto-generated constructor stub
        this.c = ctx;
        this.category_model = model_category;
        this.filterList = model_category;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return category_model.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return category_model.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return category_model.indexOf(getItem(pos));
    }

    @Override
    public View getView(final int pos, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.categories_list, null);
        }

        final TextView txtCategory = convertView.findViewById(R.id.txtCategory);

        //SET DATA TO THEM
        txtCategory.setText(category_model.get(pos).getCategories());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("business_category", category_model.get(pos).getCategories());
                mydb = new DBHelper(c);
                ArrayList array_list = mydb.getAllCategories();


            //    mydb.getTableAsString(category_model.get(pos).getCategories());
                // Toast.makeText(c, "" + Integer.toString(count), Toast.LENGTH_SHORT).show();

                Toast.makeText(c, "" + array_list, Toast.LENGTH_LONG).show();

                //  mydb.updateCount(category_model.get(pos).getCategories(), mydb.getCurrentCount(category_model.get(pos).getCategories()));

                fragment = new AllProfiles();
                fragment.setArguments(bundle);
                loadFragment();

                //Toast.makeText(c, "" + category_model.get(pos).getCategories(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    private void loadFragment() {

        FragmentManager fm = ((AppCompatActivity) c).getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .addToBackStack(String.valueOf(fm))
                .commit();
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

            category_model = (ArrayList<CategorySearchModel>) results.values;
            notifyDataSetChanged();
        }

    }

}