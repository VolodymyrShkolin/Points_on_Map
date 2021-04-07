package com.bignerdranch.android.points_on_map.fragment.childFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.android.points_on_map.R;
import com.bignerdranch.android.points_on_map.api.Api;
import com.bignerdranch.android.points_on_map.api.Request;
import com.bignerdranch.android.points_on_map.data.MapResponse;
import com.bignerdranch.android.points_on_map.data.PlacesItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MapFragment extends Fragment {
    List<PlacesItem> places;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        startRequest();
        return view;
    }


    public void mapRequest (){

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                for (int i = 0; i <places.size() ; i++) {
                    double lat = places.get(i).getLat();
                    double lng = places.get(i).getLng();
                    LatLng mLatLng = new LatLng(lat,  lng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(mLatLng);
                    markerOptions.title(places.get(i).getName());
                    googleMap.addMarker(markerOptions);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13));
                }
            }
        });
    }

    public void startRequest( ) {

        Api api = new Request().buildRetrofitConfig();
        Call<MapResponse> call = api.getMapResult();

        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(@NotNull Call<MapResponse> call,
                                   @NotNull Response<MapResponse> response) {

                if(response.isSuccessful()) {
                    places = new ArrayList<>(response.body().getPlaces());
                    mapRequest();
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
}