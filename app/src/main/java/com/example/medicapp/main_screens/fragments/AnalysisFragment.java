package com.example.medicapp.main_screens.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.medicapp.R;
import com.example.medicapp.databinding.FragmentAnalisisFragmentBinding;
import com.example.medicapp.main_screens.MainScreen;
import com.example.medicapp.main_screens.RedactSelectedAnalysisInCartInterface;
import com.example.medicapp.main_screens.adapters.CatalogAnalysisAdapter;
import com.example.medicapp.main_screens.adapters.NewsAdapter;
import com.example.medicapp.main_screens.objects.Analysis;
import com.example.medicapp.main_screens.objects.News;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnalysisFragment extends Fragment implements MainScreen.BackpressedListener {
    @Override
    public boolean onBackPressed() {
        if (closeApp){
            requireActivity().finishAffinity();
        }
        Toast.makeText(getContext(), "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        closeApp = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    closeApp = false;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return true;
    }
    private class BackGetAnalysisCatalog extends AsyncTask<Void, Void, String>{
        private final String GET_CATALOG_ANALYSIS_URL = "http://192.168.144.66:8080/api/catalog";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(3000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder().url(GET_CATALOG_ANALYSIS_URL)
                    .addHeader("accept", "application/json")
                    .build();

            try(Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    return null;
                }
                else {
                    String jsonResponse = response.body().string();
                    return jsonResponse;
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s == null){
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressView.setVisibility(View.GONE);
                        binding.errorConnectText.setVisibility(View.VISIBLE);
                    }
                });

            }
            else{
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    CollectionType listType =
                            objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Analysis.class);

                    analysisList = new ArrayList<>();
                    allGetAnalysisList = objectMapper.readValue(s, listType);

                    setAnalysisList();
                    selectFragment();
                } catch (JsonProcessingException e) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Ошибка, обновите страницу", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }

        }
    }
    private void setAnalysisList() {
        int b = countAnalysisInList+countSetAnalysis;

        if (allGetAnalysisList.size() - countAnalysisInList >= 15){
            for (int i = countAnalysisInList; i < b;i++){
                analysisList.add(allGetAnalysisList.get(i));
                countAnalysisInList+=1;
            }
        }
        else if (allGetAnalysisList.size() - countAnalysisInList <= 0){

        }
        else {
            for (int i = countAnalysisInList; i < allGetAnalysisList.size();i++){
                analysisList.add(allGetAnalysisList.get(i));
                countAnalysisInList+=1;
            }
        }

        catalogAnalisisAdapter = new CatalogAnalysisAdapter(analysisList, getContext(), onAnalysisClickListener, onAnalysisButtonClickListener, cartList);
        binding.rvCatalogAnalysis.setAdapter(catalogAnalisisAdapter);
        loadActive[0] = true;

    }
    private class BackGetNews extends AsyncTask<Void, Void, String>{
        private final String GET_NEWS_URL = "http://192.168.144.66:8080/api/news";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();
            Request request = new Request.Builder().url(GET_NEWS_URL).addHeader("content-type","application/json").build();
            try(Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    return null;
                }
                else {
                    if (response.code() == 200){
                        String jsonResponse = response.body().string();
                        return jsonResponse;
                    } else if (response.code() == 204) {
                        String jsonResponse = response.body().string();
                        return jsonResponse;
                    }
                }
            } catch (IOException e) {
                return null;

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                ObjectMapper objectMapper = new ObjectMapper();
                try {

                    CollectionType listType =
                            objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, News.class);
                    newsList = objectMapper.readValue(s, listType);
                    setNewsList();
                    selectFragment();
                } catch (JsonProcessingException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Ошибка соединения с сервером", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

        }

        private void setNewsList() {
            newsAdapter = new NewsAdapter(newsList, getContext());

            binding.rvNews.setAdapter(newsAdapter);
            loadActive[1] = true;
        }
    }
    private void selectFragment(){
        if (loadActive[0] == true && loadActive[1] == true){
            settingFragmentOk();
        }
    }
    private void settingFragmentOk(){
        binding.progressView.setVisibility(View.GONE);
        binding.searchBar.setVisibility(View.VISIBLE);
        binding.refreshLayout.setVisibility(View.VISIBLE);
        binding.mainRefreshLayout.setEnabled(false);

        if (priceNow == 0)
            binding.shoppingCardView.setVisibility(View.GONE);
        else {
            binding.shoppingCardView.setVisibility(View.VISIBLE);
            binding.cartPriceView.setText(priceNow+"");
        }
    }

    private boolean[] loadActive = new boolean[]{false, false};
    private BackGetAnalysisCatalog backGetAnalysis = new BackGetAnalysisCatalog();
    private BackGetNews backGetNews = new BackGetNews();
    private FragmentAnalisisFragmentBinding binding;
    private NewsAdapter newsAdapter;
    private CatalogAnalysisAdapter catalogAnalisisAdapter;
    private List<News> newsList = new ArrayList<>();
    private List<Analysis> analysisList = new ArrayList<>();
    private List<Analysis> allGetAnalysisList = new ArrayList<>();
    private int countSetAnalysis = 15;
    private int countAnalysisInList = 0;
    private List<SelectedAnalysis> cartList = new ArrayList<>();
    private RedactSelectedAnalysisInCartInterface redactSelectedAnalysisInCartInterface;
    private CatalogAnalysisAdapter.OnAnalysisClickListener onAnalysisClickListener;
    private CatalogAnalysisAdapter.OnAnalysisButtonClickListener onAnalysisButtonClickListener;
    private DataAnalysisDialogFragment.AddAnalysisInCartInterface addAnalysisInCartInterface;
    private int priceNow;
    private SharedPreferences preferences;
    boolean closeApp = false;
    Type listTypeSelAnalysis = new TypeToken<List<SelectedAnalysis>>() {}.getType();
    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAnalisisFragmentBinding.inflate(inflater, container, false);
        preferences = requireActivity().getSharedPreferences("CartTable", Context.MODE_PRIVATE);
        setting();

//        backGetAnalysis.execute();
//        backGetNews.execute();

        getCartList();
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setInterface();
                elListeners();

                addList();//////
                settingFragmentOk();/////
                setAnalysisList();//////
            }
        });



        return binding.getRoot();
    }

    private void addList() {
        allGetAnalysisList.add(new Analysis("авыаф","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический авф крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический пыфрф крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический фыа крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический аналипфз крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический аналиифмсфз крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический аналсямяиз крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический аналимиз крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический аналйке1из крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Кал формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Клинический анализ крови с лейкоцитарной формулировкой","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        allGetAnalysisList.add(new Analysis("Кал","jfdks;afas;","jfdk;ljfldskjf","2 дня","fdfads", 1000, "jf;dksjf;la"));
        }

    private void setting(){
        loadState();

        binding.categoryPopularBtn.setSelected(true);
        binding.etSearch.setIconifiedByDefault(false);
        binding.etSearch.setQueryHint("Искать анализы");
    }
    private void setInterface() {
        redactSelectedAnalysisInCartInterface = new RedactSelectedAnalysisInCartInterface() {
            @Override
            public void RedactSelectedAnalysis(List<SelectedAnalysis> selectedAnalysisList, int price) {
                binding.cartPriceView.setText(price+"");
                cartList = selectedAnalysisList;
                priceNow = price;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveCartList();
                    }
                }).start();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        catalogAnalisisAdapter = new CatalogAnalysisAdapter(analysisList, getContext(), onAnalysisClickListener, onAnalysisButtonClickListener, cartList);
                        binding.rvCatalogAnalysis.setAdapter(catalogAnalisisAdapter);
                    }
                });

                if (priceNow == 0){
                    binding.shoppingCardView.setVisibility(View.GONE);
                }
            }
        };

         onAnalysisClickListener = new CatalogAnalysisAdapter.OnAnalysisClickListener() {
             @Override
             public void onAnalysisClick(Analysis analysis, boolean checkCart) {
                 onDialogDataAnalysis(analysis, checkCart);
             }

        };

         onAnalysisButtonClickListener = new CatalogAnalysisAdapter.OnAnalysisButtonClickListener() {
            @Override
            public void onAnalysisButtonClick(SelectedAnalysis analysis, boolean action) {
                byte mark = 1;
                if (action){
                    cartList.add(analysis);
                }
                else{
                    mark = -1;
                    cartList.removeIf(n->(Objects.equals(n.getAnalysis().getTitle(), analysis.getAnalysis().getTitle())));
                }
                int oldPrice = Integer.parseInt(String.valueOf(binding.cartPriceView.getText()));
                priceNow = analysis.getAnalysis().getPrice()*mark+oldPrice;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveCartList();
                    }
                }).start();


                binding.cartPriceView.setText(String.valueOf(priceNow));
                if(priceNow>0){
                    binding.shoppingCardView.setVisibility(View.VISIBLE);
                }
                else{
                    binding.shoppingCardView.setVisibility(View.GONE);
                }
            }
        };

        addAnalysisInCartInterface = new DataAnalysisDialogFragment.AddAnalysisInCartInterface() {
            @Override
            public void addAnalysis(SelectedAnalysis selectedAnalysis) {
                cartList.add(selectedAnalysis);
                int oldPrice = Integer.parseInt(String.valueOf(binding.cartPriceView.getText()));
                priceNow = selectedAnalysis.getAnalysis().getPrice()+oldPrice;
                catalogAnalisisAdapter = new CatalogAnalysisAdapter(analysisList,getContext(), onAnalysisClickListener, onAnalysisButtonClickListener, cartList);
                binding.rvCatalogAnalysis.setAdapter(catalogAnalisisAdapter);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveCartList();
                    }
                }).start();
                binding.cartPriceView.setText(String.valueOf(priceNow));
                binding.shoppingCardView.setVisibility(View.VISIBLE);
            }
        };

    }

    int totalItemCount = 0;
    int lastVisibleItemPosition = 0;
    private void elListeners() {

        binding.etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final List<Analysis> filterModeList = filter(analysisList, query);
                catalogAnalisisAdapter.setFilter(filterModeList);
                if (!binding.etSearch.isIconified()){
                    binding.etSearch.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (catalogAnalisisAdapter != null){
                    final List<Analysis> filterModeList = filter(analysisList, newText);
                    catalogAnalisisAdapter.setFilter(filterModeList);
                    return true;
                }
                return true;
            }
        });
        binding.categoryPopularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.categoryComplexBtn.setSelected(false);
                binding.categoryCOVIDBtn.setSelected(false);
                binding.categoryPopularBtn.setSelected(true);

                binding.categoryPopularBtn.setTextColor(getResources().getColor(R.color.white));
                binding.categoryComplexBtn.setTextColor(getResources().getColor(R.color.Caption));
                binding.categoryCOVIDBtn.setTextColor(getResources().getColor(R.color.Caption));
            }
        });
        binding.categoryComplexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.categoryComplexBtn.setSelected(true);
                binding.categoryCOVIDBtn.setSelected(false);
                binding.categoryPopularBtn.setSelected(false);

                binding.categoryComplexBtn.setTextColor(getResources().getColor(R.color.white));
                binding.categoryPopularBtn.setTextColor(getResources().getColor(R.color.Caption));
                binding.categoryCOVIDBtn.setTextColor(getResources().getColor(R.color.Caption));
            }
        });
        binding.categoryCOVIDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.categoryComplexBtn.setSelected(false);
                binding.categoryCOVIDBtn.setSelected(true);
                binding.categoryPopularBtn.setSelected(false);

                binding.categoryCOVIDBtn.setTextColor(getResources().getColor(R.color.white));
                binding.categoryPopularBtn.setTextColor(getResources().getColor(R.color.Caption));
                binding.categoryComplexBtn.setTextColor(getResources().getColor(R.color.Caption));
            }
        });
        binding.shoppingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("analysisList", (ArrayList<? extends Parcelable>) cartList);
                bundle.putString("price", (String) binding.cartPriceView.getText());
                ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment(redactSelectedAnalysisInCartInterface);
                shoppingCartFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.most_mainscreen_view, shoppingCartFragment)
                        .addToBackStack("shoppingCart")
                        .commit();
            }
        });

        binding.mainRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    loadData();
                    binding.mainRefreshLayout.setRefreshing(false);
            }
        });
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadState();
                loadData();
                binding.mainRefreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
            }
        });

        binding.rvCatalogAnalysis.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 2) {
                    try {
                        if (lastVisibleItemPosition + 1 == totalItemCount) {

                            List<Analysis> newAnalysisList = new ArrayList<>();
                            int b = countAnalysisInList+countSetAnalysis;
                            if (allGetAnalysisList.size() - countAnalysisInList >= 15){
                                for (int i = countAnalysisInList; i < b;i++){
                                    newAnalysisList.add(allGetAnalysisList.get(i));
                                    countAnalysisInList+=1;
                                }
                            }
                            else if (allGetAnalysisList.size() - countAnalysisInList <= 0){

                            }
                            else {
                                for (int i = countAnalysisInList; i < allGetAnalysisList.size();i++){
                                    newAnalysisList.add(allGetAnalysisList.get(i));
                                    countAnalysisInList+=1;
                                }
                            }
                           binding.rvUpdateProgress.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1500);
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.rvUpdateProgress.setVisibility(View.GONE);
                                                catalogAnalisisAdapter.updateList(newAnalysisList);
                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(),"Ошибка, обновите страницу", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }).start();

                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(),"Произошла неизвестная ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                totalItemCount = binding.rvCatalogAnalysis.getLayoutManager().getItemCount();
                lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            }
        });
    }
    private List<Analysis> filter(List<Analysis> analysisList, String query){
        query = query.toLowerCase();
        final List<Analysis> filteredModeList = new ArrayList<>();
        for (Analysis analysis: analysisList){
            final String text = analysis.getTitle().toLowerCase();
            if (text.startsWith(query)){
                filteredModeList.add(analysis);
            }
        }
        return filteredModeList;
    }


    private void loadState(){
        binding.searchBar.setVisibility(View.GONE);
        binding.refreshLayout.setVisibility(View.GONE);
        binding.shoppingCardView.setVisibility(View.GONE);
        binding.rvUpdateProgress.setVisibility(View.GONE);

        binding.progressView.setVisibility(View.VISIBLE);
        binding.errorConnectText.setVisibility(View.GONE);
    }
    private void getCartList() {
        String cartListJson;
        try {
            cartListJson = preferences.getString("cart", null);
            priceNow = preferences.getInt("cartPrice", 0);
        }catch (Exception e){
            Toast.makeText(getActivity(), "При получении данных произошла ошибка", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartListJson != null){
            cartList = gson.fromJson(cartListJson, listTypeSelAnalysis);
        }
    }
    private void saveCartList() {
        SharedPreferences.Editor editor = preferences.edit();
        String cartListJson = gson.toJson(cartList, listTypeSelAnalysis);
        editor.remove("cart");
        editor.remove("cartPrice");
        editor.putString("cart", cartListJson);
        editor.putInt("cartPrice", priceNow);
        editor.apply();
    }
    public void loadData(){
        binding.progressView.setVisibility(View.VISIBLE);
        binding.errorConnectText.setVisibility(View.GONE);
        backGetAnalysis.cancel(true);
        backGetAnalysis = (BackGetAnalysisCatalog) new BackGetAnalysisCatalog().execute();
        backGetNews.cancel(true);
        backGetNews = (BackGetNews) new BackGetNews().execute();
    }


    void onDialogDataAnalysis(Analysis analysis, boolean checkCart){
        Fragment fragment = new DataAnalysisDialogFragment(addAnalysisInCartInterface);
        Bundle bundle = new Bundle();
        bundle.putParcelable("analysis",analysis);
        bundle.putBoolean("checkCart",checkCart);
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.most_mainscreen_view, fragment, "DialogFragment")
                .addToBackStack("dataDialog")
                .commit();
    }


}