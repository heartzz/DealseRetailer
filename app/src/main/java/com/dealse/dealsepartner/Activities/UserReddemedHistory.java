package com.dealse.dealsepartner.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Interfaces.GetRedeemedOffersList;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.Objects.GetOfferRequest;
import com.dealse.dealsepartner.Objects.GetUsedOfferList;
import com.dealse.dealsepartner.Objects.RedeemedOfeersResponse;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;
import com.dealse.dealsepartner.Utility.LoaderDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserReddemedHistory extends AppCompatActivity implements
        MenuItemCompat.OnActionExpandListener{
    private Toolbar mToolbar;
    CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse;
    RecyclerView recyclerView;
    public static String TAG = "UserReddemedHistory";
    RedeemedListAdapter redeemedListAdapter;
    SearchView searchView;
    String oldString = "";
    GetUsedOfferList getOfferResponse;
    TextView tv_noredeemedtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userredeemedhistory);
        initToolbar();
        checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(UserReddemedHistory.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        tv_noredeemedtxt = (TextView)findViewById(R.id.tv_noredeemedtxt);

        checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();

        String storeDataString = DealseApplicationsManager.getInstance().getPref(UserReddemedHistory.this).getString("storeData","");

        if(!storeDataString.equalsIgnoreCase("")){

            checkStoreMobieNumberExistResponse = new Gson().fromJson(storeDataString, CheckStoreMobieNumberExistResponse.class);

            if(checkStoreMobieNumberExistResponse != null) {



                if(checkStoreMobieNumberExistResponse.getData().getLogoUrl() != null) {

                    LoaderDialog.showLoader(UserReddemedHistory.this);
                    getRedeemedOfferList();

                }
            }else {
                checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        ((EditText) searchView.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.white));
        ((EditText) searchView.findViewById(R.id.search_src_text)).setTextColor(getResources().getColor(R.color.white));
        searchView.setQueryHint("Search by offer name ...");
        searchItem.expandActionView();
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        // searchView.setIconifiedByDefault(false);

        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            UserReddemedHistory.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();
                redeemedListAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                // search goes here !!
                // listAdapter.getFilter().filter(query);

                /*getpeoplelist(user_id, newText);
                Log.e("queryText", newText);*/
                if (!oldString.equalsIgnoreCase(newText)) {
                    Log.e("queryText", newText);
                    if(newText.equalsIgnoreCase("")){
                        getRedeemedOfferList();
                    }else {
                        redeemedListAdapter.getFilter().filter(newText);
                    }
                }
                oldString = newText;
                return true;
            }

        });
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        UserReddemedHistory.this.finish();


        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                redeemedListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/

    public void getRedeemedOfferList(){

        GetOfferRequest getOfferRequest = new GetOfferRequest();
        getOfferRequest.setStoreId(checkStoreMobieNumberExistResponse.getData().getStoreId());
        getOfferRequest.setPageIndex(1);
        getOfferRequest.setPageSize(100);

        Log.d(TAG, "getRedeemedOfferList"+"Step1");

        final GetRedeemedOffersList getRedeemedOffersList = Dealsepartnerapplicationclass.retrofit.create(GetRedeemedOffersList.class);
        Call<GetUsedOfferList> call = getRedeemedOffersList.getRedeemedOffersList(DealseApplicationsManager.getInstance().getPref(UserReddemedHistory.this).getString(Constants.KEY_TOKEN,""),getOfferRequest);
        call.enqueue(new retrofit2.Callback<GetUsedOfferList>() {
            @Override
            public void onResponse(Call<GetUsedOfferList> call, Response<GetUsedOfferList> response) {
                getOfferResponse = new GetUsedOfferList();
                getOfferResponse = response.body();

                LoaderDialog.hideLoader();
                Log.d(TAG, "getRedeemedOfferList"+"Step2");

                if(getOfferResponse != null) {
                    try {

                        if(getOfferResponse.getCode() == 200) {

                            Log.d(TAG, "getRedeemedOfferList"+"Step3");

                            if(getOfferResponse.getData() != null){
                                redeemedListAdapter = new RedeemedListAdapter(UserReddemedHistory.this, getOfferResponse.getData());
                                recyclerView.setAdapter(redeemedListAdapter);
                                tv_noredeemedtxt.setVisibility(View.GONE);
                            }else {
                                recyclerView.setVisibility(View.GONE);
                                tv_noredeemedtxt.setVisibility(View.VISIBLE);
                            }
                        }else if(getOfferResponse.getCode() == 404){
                            tv_noredeemedtxt.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "getRedeemedOfferList"+"Step4"+e.toString());
                        Toast.makeText(UserReddemedHistory.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(TAG, "getRedeemedOfferList"+"Step5");
                    recyclerView.setVisibility(View.GONE);
                    //   Toast.makeText(ManageOffersScreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<GetUsedOfferList> call, Throwable t) {
                LoaderDialog.hideLoader();
                Log.d(TAG, "getRedeemedOfferList"+"Step6"+t.toString());
                Toast.makeText(UserReddemedHistory.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class RedeemedListAdapter extends RecyclerView.Adapter<RedeemedListAdapter.MyViewHolder> implements Filterable {


        private Context a;
        private List<RedeemedOfeersResponse> createddealsList;
        private List<RedeemedOfeersResponse> createddealsListFull;

        public RedeemedListAdapter(Context a, List<RedeemedOfeersResponse> createddealsList) {
            this.a = a;
            this.createddealsList = createddealsList;
            createddealsListFull = new ArrayList<>(createddealsList);
        }

        @Override
        public RedeemedListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_redeemedoffer, parent, false);

            return new RedeemedListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RedeemedListAdapter.MyViewHolder holder, final int position) {

            holder.tv_offerId.setText(String.valueOf(createddealsList.get(position).getOfferName()));
            holder.tv_redeemDate.setText(Constants.convertDate(createddealsList.get(position).getRedeemDate()));

            holder.tv_offername.setText(createddealsList.get(position).getOfferName());
            holder.tv_username.setText(createddealsList.get(position).getUserName());

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*Intent createofferIntent = new Intent(a, CreateNewOfferStepper.class);
                    createofferIntent.putExtra("offer",new Gson().toJson(createddealsList.get(position)));
                    a.startActivity(createofferIntent);*/

                }
            });
        }

        @Override
        public int getItemCount() {
            return createddealsList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_offerId,tv_redeemDate,tv_offername,tv_username;
            CardView card;



            public MyViewHolder(View view) {
                super(view);

                tv_offerId = (TextView)view.findViewById(R.id.tv_offerId);
                tv_redeemDate = (TextView)view.findViewById(R.id.tv_redeemDate);
                card = (CardView)view.findViewById(R.id.card);
                tv_offername = (TextView)view.findViewById(R.id.tv_offername);
                tv_username = (TextView)view.findViewById(R.id.tv_username);


            }
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<RedeemedOfeersResponse> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(createddealsList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (RedeemedOfeersResponse item : createddealsListFull) {
                        if (item.getOfferName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                createddealsList.clear();
                if(results.values != null)
                createddealsList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        getSupportActionBar().setTitle(null);
        //toolbar_title.setTypeface(GetFonts.getInstance().getRobotoFont(TravFeddHomeScreen.this));
        toolbar_title.setText("User redeemed offers");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
