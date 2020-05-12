package com.aplikasi.dolan.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.aplikasi.dolan.Adapters.RecommendationAdapter;
import com.aplikasi.dolan.Fragments.Recommendation;
import com.aplikasi.dolan.R;
import com.aplikasi.dolan.model.Booking;
import com.aplikasi.dolan.model.Hotel;
import com.aplikasi.dolan.model.DolanResult;
import com.aplikasi.dolan.model.UserDolan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DolanInfo extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView hotelImage;
    private TextView hotelDesc, views, drafts, completed;
    private Button book, draftBook;
    private RecommendationAdapter.HotelViewHolder hotelViewHolder;
    Hotel hotel;
    int pos;
    DolanResult dolanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

        toolbar = findViewById(R.id.toolbarInfo);
        hotelImage = findViewById(R.id.hotelImage);
        hotelDesc = findViewById(R.id.hotelDesc);
        book = findViewById(R.id.confirmBooking);
        draftBook = findViewById(R.id.draftBooking);
        views = findViewById(R.id.views);
        drafts = findViewById(R.id.draftText);
        completed = findViewById(R.id.completedText);


    }

    @Override
    protected void onResume() {
        super.onResume();

        hotel = (Hotel) getIntent().getExtras().getSerializable("data");


        dolanResult = new Gson().fromJson(getHotels(), DolanResult.class);
        pos = getIntent().getExtras().getInt("pos");

        toolbar.setTitle(hotel.getName());

        setSupportActionBar(toolbar);
        Picasso
                .with(DolanInfo.this)
                .load(Uri.parse(hotel.getImageUrl()))
                .into(hotelImage);
        hotelDesc.setText(hotel.getDescription());

        views.setText(dolanResult.getHotels().get(pos).getVisits() + " Kunjungan");
        drafts.setText(dolanResult.getHotels().get(pos).getDraftBookings() + " Suka");
        completed.setText(dolanResult.getHotels().get(pos).getCompletedBookings() + " Tidak Suka");


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(true);
                finish();

            }
        });
        draftBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(false);
                MainActivity.updatec(1);
                Log.d("ononon", "onClick: ");
                finish();

            }
        });
    }

    public String getHotels() {
        SharedPreferences sp = getSharedPreferences("hotel", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        if (sp.contains("data")) {
            return sp.getString("data", null);
        } else {
            return null;
        }

    }

    private void setBooking(Boolean complete) {


        UserDolan hotel1 = new UserDolan();
        hotel1.setName(hotel.getName());
        hotel1.setCompleted(complete);
        hotel1.setTags(hotel.getTags());

        Booking booking = new Booking();

        List<UserDolan> userDolans = MainActivity.bookings.getUserDolans();
        userDolans.add(hotel1);

        MainActivity.bookings.setUserDolans(userDolans);

        Set<String> s = new HashSet<>();
        for (UserDolan userDolan : MainActivity.bookings.getUserDolans()) {
            if (userDolan.getCompleted()) {
                for (String ss : userDolan.getTags().split("\n")) {
                    ss = ss.replace("null", "");
                    s.add(ss);
                }

            }
        }
        String sa = "";

        for (String sss : s) {
            sa += sss;
            Recommendation.tagSet.add(sss);
        }

        if (complete) {
            int c = Integer.valueOf(dolanResult.getHotels().get(pos).getCompletedBookings());
            dolanResult.getHotels().get(pos).setCompletedBookings(String.valueOf(c + 1));
            storeUpdates(dolanResult);
        } else {
            int c = Integer.valueOf(dolanResult.getHotels().get(pos).getDraftBookings());
            dolanResult.getHotels().get(pos).setDraftBookings(String.valueOf(c + 1));
            storeUpdates(dolanResult);
        }

    }

    public void storeUpdates(DolanResult dolanResult) {
        SharedPreferences.Editor spe = getSharedPreferences("hotel", Context.MODE_PRIVATE).edit();
        String save = new Gson().toJson(dolanResult);
        spe.putString("data", save);
        spe.apply();


    }
}
