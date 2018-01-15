package com.tabian.tabfragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 2/28/2017.
 */

public class Tab1Fragment extends Fragment {

    public static ListView listView;
    private static final String TAG = "Tab1Fragment";
    public static ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";
    private Button btnTEST;

    public static Activity c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
         listView=(ListView)view.findViewById(R.id.listView1);
        fillList();
        ListViewAdapter adapter=new ListViewAdapter(getActivity(), list);

        c=getActivity();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            MainActivity.fahrtenbuch.remove(i*3);
              MainActivity.fahrtenbuch.remove(i*3);

             MainActivity.fahrtenbuch.remove(i*3);

speichen();
fillList();
            }
        });
        SharedPreferences settings=c.getSharedPreferences("PREFS",0);
        SharedPreferences.Editor editor=settings.edit();
        editor.clear();
        editor.commit();

laden();
fillList();



        return view;
    }

    public static void fillList() {
        // TODO Auto-generated method stub

        list=new ArrayList<HashMap<String,String>>();
          int zahl=1;


        for(int i=0;i<MainActivity.fahrtenbuch.size();i++){


            HashMap<String,String> hashmap=new HashMap<String, String>();

            int nummer =zahl;
            hashmap.put(FIRST_COLUMN, ""+nummer);
            hashmap.put(FOURTH_COLUMN, MainActivity.fahrtenbuch.get(i)+" Km");
            hashmap.put(THIRD_COLUMN, ""+MainActivity.fahrtenbuch.get(i+1));
            hashmap.put(SECOND_COLUMN, ""+MainActivity.fahrtenbuch.get(i+2));


            list.add(hashmap);
             i=i+2;

            zahl++;
            ListViewAdapter adapter=new ListViewAdapter(c, list);

            listView.setAdapter(adapter);

        }
        if(MainActivity.fahrtenbuch.size()==0){

            list.clear();
            ListViewAdapter adapter=new ListViewAdapter(c, list);
            listView.setAdapter(adapter);

        }




    }

    public static void laden(){



         SharedPreferences settings=c.getSharedPreferences("PREFS",0);


        String test= settings.getString("words",null);

        if(test!=null){

            String[]itemwors=test.split(",");
            ArrayList<String>worte=new ArrayList<>();
            for(int i=0;i<itemwors.length;i++) {

                worte.add(itemwors[i]);
                MainActivity.fahrtenbuch = worte;


            }
        }


    }
    public static void speichen(){


        if(MainActivity.fahrtenbuch.size()>0){

            StringBuilder stringBuilder=new StringBuilder();
            for(String s: MainActivity.fahrtenbuch){

                stringBuilder.append(s);
                stringBuilder.append(",");


            }


            SharedPreferences settings=  c.getSharedPreferences("PREFS",0);
            SharedPreferences.Editor editor=settings.edit();

            editor.putString("words",stringBuilder.toString());

            editor.commit();

        }else if(MainActivity.fahrtenbuch.size()==0){

            SharedPreferences settings=c.getSharedPreferences("PREFS",0);
            SharedPreferences.Editor editor=settings.edit();
            editor.clear();
            editor.commit();

        }




    }
}
