package tieup.business_network.appmart.activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import tieup.business_network.appmart.adapters.CategoriesAdapter;
import tieup.business_network.appmart.models.CategorySearchModel;
import tieup.business_network.appmart.R;

import static tieup.business_network.appmart.services.MyService.categoryArrayList;

public class ShowAll extends AppCompatActivity {
    EditText edtTextSearch;
   CategoriesAdapter categoriesAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all);
        listView = (ListView) findViewById(R.id.mobile_list);
        edtTextSearch = (EditText) findViewById(R.id.edtTextSearch);
        categoriesAdapter = new CategoriesAdapter(ShowAll.this, getFilterList());

        listView.setAdapter(categoriesAdapter);

        edtTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                categoriesAdapter.getFilter().filter(edtTextSearch.getText().toString());

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    private ArrayList<CategorySearchModel> getFilterList() {
        ArrayList<CategorySearchModel> searchModels = new ArrayList<CategorySearchModel>();

        CategorySearchModel p;
        for (int i = 0; i < categoryArrayList.size(); i++) {
            p = new CategorySearchModel(categoryArrayList.get(i));
            searchModels.add(p);
        }

        return searchModels;
    }
    public void setActionBarTitle(String title) {
       getSupportActionBar().setTitle(title);

    }




}



