package com.example.raymond.share;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.raymond.share.driverList.DriverAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView driverList;
    private DriverAdapter driverAdapter;

    public DriverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_driver, container, false);

        driverList = (RecyclerView) v.findViewById(R.id.driverList);
        driverList.setLayoutManager(new LinearLayoutManager(getActivity()));
        driverAdapter = new DriverAdapter();
        driverList.setAdapter(driverAdapter);

       // driverListAdapter.setTripList(getData());

//        fab = (FloatingActionButton) v.findViewById(R.id.addTrip);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), RegTrip.class);
//                intent.putExtra("role", "driver");
//                startActivity(intent);
//            }
//        });

        return v;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_card_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
