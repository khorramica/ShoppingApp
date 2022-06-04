package ir.finto.shoppingapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ir.finto.shoppingapp.Global.Link;
import ir.finto.shoppingapp.R;
import ir.finto.shoppingapp.adapter.CategoryAdapter;
import ir.finto.shoppingapp.adapter.SliderAdapter;
import ir.finto.shoppingapp.model.Banner;
import ir.finto.shoppingapp.model.Category;

public class HomeFragment extends Fragment {
    View view;
    RequestQueue requestQueue;

    //Slider
    List<Banner> bannerList = new ArrayList<>();
    SliderAdapter sliderAdapter;
    ViewPager viewPager;
    TabLayout tabs;


    //Category
    List<Category> categoryList = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    RecyclerView recyclerViewCategory;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        requestQueue = Volley.newRequestQueue(getContext());
        GetBannerSlider();


       recyclerViewCategory = view.findViewById(R.id.recyclerView_Category);
       recyclerViewCategory.setHasFixedSize(true);
//       recyclerViewCategory.setLayoutManager(new GridLayoutManager(getContext(),3));
       recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
       categoryAdapter = new CategoryAdapter(getContext(),categoryList);
       recyclerViewCategory.setAdapter(categoryAdapter);

        String url = Link.LINK_CATEGORY_BY_LIMIT;
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Category[] categories = gson.fromJson(response.toString(), Category[].class);

                for (int i = 0; i < categories.length; i++) {
                    categoryList.add(categories[i]);
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR : " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("Shopping Error : ", error.getMessage());
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);
        requestQueue.add(request);

        return view;

    }

    private void GetBannerSlider() {
//        requestQueue = Volley.newRequestQueue(getContext());

        viewPager = view.findViewById(R.id.view_Pager);
        tabs =  view.findViewById(R.id.tabs_Layout);

        sliderAdapter = new SliderAdapter(getContext().getApplicationContext(), bannerList);
        viewPager.setAdapter(sliderAdapter);
        tabs.setupWithViewPager(viewPager, true);

        viewPager.setRotationY(180);

        String url = Link.LINK_BANNER_SLIDER;
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Banner[] banners = gson.fromJson(response.toString(), Banner[].class);

                for (int i = 0; i < banners.length; i++) {
                    bannerList.add(banners[i]);
                    sliderAdapter.notifyDataSetChanged();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Slider ERROR : " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("Shopping Error : ", error.getMessage());
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);
        requestQueue.add(request);


        boolean running_thread = true;

        Thread thread = new Thread(){
            @Override
            public void run() {
                while (running_thread){

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(viewPager.getCurrentItem() < bannerList.size() -1 )
                            {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() +1);
                            }
                            else
                                viewPager.setCurrentItem(0);
                        }
                    });
                }
            }
        };

        thread.start();
    }
}