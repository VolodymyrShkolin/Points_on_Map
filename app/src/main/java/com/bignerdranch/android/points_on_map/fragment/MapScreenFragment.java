package com.bignerdranch.android.points_on_map.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.points_on_map.R;
import com.bignerdranch.android.points_on_map.api.Api;
import com.bignerdranch.android.points_on_map.api.Request;
import com.bignerdranch.android.points_on_map.data.MapResponse;
import com.bignerdranch.android.points_on_map.data.PlacesItem;
import com.bignerdranch.android.points_on_map.fragment.childFragment.MapFragment;
import com.bignerdranch.android.points_on_map.recyclerview.RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapScreenFragment extends Fragment {
    TextView emailTitle;
    Button logout;
    List<PlacesItem> places1;

    RecyclerView date_dimeRV;
    GridLayoutManager gridLayoutManager;
    RecyclerViewAdapter adapter;
    NavController navController;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailTitle = view.findViewById(R.id.email_title);
        navController = Navigation.findNavController(view);
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                navController.navigate(R.id.mainFragment);
            }
        });

        date_dimeRV = view.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        date_dimeRV.setLayoutManager(gridLayoutManager);
        date_dimeRV.setAdapter(adapter);

        Fragment fragment = new MapFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.map_layout, fragment)
                .commit();

        String iEmail = getArguments().getString("TitleArg");
        String cleanTitle = iEmail.substring(0, iEmail.indexOf('@'));
        emailTitle.setText(cleanTitle);

        startRequest();
    }

    public void startRequest( ) {

        Api api = new Request().buildRetrofitConfig();
        Call<MapResponse> call = api.getMapResult();

        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(@NotNull Call<MapResponse> call,
                                   @NotNull Response<MapResponse> response) {


                if(response.isSuccessful()) {
                    places1 = new ArrayList<>(response.body().getPlaces());
                    adapter = new RecyclerViewAdapter(places1);
                    date_dimeRV.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(), R.string.try_again_error,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {
                Toast.makeText(getContext(), (CharSequence) t, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        navController.navigate(R.id.mainFragment);
    }


}