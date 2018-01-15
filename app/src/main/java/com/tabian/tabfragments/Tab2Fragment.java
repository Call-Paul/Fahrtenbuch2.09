package com.tabian.tabfragments;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private LocationManager locationManager;
    private LocationListener listener;
    public float dis;
    public static int check=0;

    public Location l1 = new Location("");
    public Location l2 = new Location("");
public TextView txt;

    private Button start, stop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);
         txt=(TextView)view.findViewById(R.id.txt);

        start = (Button) view.findViewById(R.id.button);
        stop = (Button) view.findViewById(R.id.button2);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.dis = 0;
                if (check == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Es läuft bereit eine Fahrt. Beende die laufende, um eine neue zu Straten!", Toast.LENGTH_LONG).show();

                } else {
                    check = 1;
                    txt.setText("Fahrt läuft!");
                    Notification.Builder builder = new Notification.Builder(getActivity());
                    builder.setSmallIcon(R.drawable.custom)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentTitle("Fahrt gestartet!")

                            .setOngoing(true);

                    builder.setLights(0xff00ff00, 300, 100);
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(1, builder.build());

                    GpsTracker gt = new GpsTracker(getActivity().getApplicationContext());
                    Location l = gt.getLocation();


                    if (l == null) {
                        Toast.makeText(getActivity().getApplicationContext(), "GPS unable to get Value", Toast.LENGTH_SHORT).show();
                    } else {
                        double lat = l.getLatitude();
                        double lon = l.getLongitude();
                        l1.setLongitude(l.getLongitude());
                        l1.setLatitude(l.getLatitude());

                        Toast.makeText(getActivity().getApplicationContext(), "GPS Lat = " + lat + "\n lon = " + lon, Toast.LENGTH_SHORT).show();
                    }

                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates("gps", 8000, 0, listener);


                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check==1) {
                    txt.setText("");
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);


                    Notification.Builder builder = new Notification.Builder(getActivity());
                    builder.setSmallIcon(R.drawable.custom)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentTitle("Fahrt beendet!")

                            .setOngoing(true);

                    builder.setLights(0xff00ff00, 300, 100);
                    NotificationManager manager2 = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(2, builder.build());
                    manager.cancel(2);

                    locationManager.removeUpdates(listener);




                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
                    Date now = calendar.getTime();
                    String timestamp = simpleDate.format(now);

                    Toast.makeText(getActivity().getApplicationContext(),""+MainActivity.dis,Toast.LENGTH_LONG).show();
                    float zahl = MainActivity.dis;
                    float rund = (float) (((int) (zahl * 100)) / 100.0);

                    float fertig = rund / 1000;
                    float richtigfertig = (float) (((int) (fertig * 100)) / 100.0);
                    MainActivity.fahrtenbuch.add("" + richtigfertig);

                    double geld = fertig * 0.06;
                    double geldgerundet = (float) (((int) (geld * 100)) / 100.0);

                    DecimalFormat f = new DecimalFormat("#0.00");

                    MainActivity.fahrtenbuch.add("" + f.format(geldgerundet)  + " $");
                    MainActivity.fahrtenbuch.add(timestamp);


                    Tab1Fragment.fillList();
                    Tab1Fragment.speichen();
check=0;
                }


            }
        });


         locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GpsTracker gt = new GpsTracker(getActivity().getApplicationContext());
                Location l = gt.getLocation();
                l2.setLongitude(l.getLongitude());
                l2.setLatitude(l.getLatitude());

                MainActivity.dis=MainActivity.dis+l1.distanceTo(l2);

               l1.setLatitude(l.getLatitude());
               l1.setLongitude(l.getLongitude());





                Toast.makeText(getActivity().getApplicationContext(), "change", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        return view;



    }






}
