package src.nuit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import org.json.simple.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends Activity {

    // Google Map
    private GoogleMap googleMap;
    CameraPosition cameraPosition = new CameraPosition.Builder().target(
            new LatLng(17.385044, 78.486671)).zoom(12).build();

    //Define marker
    double latitude = 50;
    double longitude = 50;
    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("You");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            // Loading map
            initilizeMap();

            googleMap.setMyLocationEnabled(true);
            googleMap.addMarker(marker);
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            String donneZone = "{    point0:    {        lat: 100.00,        lng: 100.00    },    point1:     {        lat: 150.00,        lng: 100.00    },    point2:     {        lat: 150.00,        lng: 150.00    },    point3:     {        lat: 100.00,        lng:150.00    }}";
            afficheZoneChoisie(donneZone);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    public void afficheZoneChoisie(String donneeZone)
    {
        ArrayList<ArrayList<Double>> listeZone = new ArrayList<ArrayList<Double>>();
        JSONParser parser = new JSONParser();

        try {

            JSONArray array = (JSONArray)(parser.parse(donneeZone));

            for(int i = 0; i < array.size(); i ++)
            {

                JSONArray array2 = (JSONArray)(parser.parse((String)(array.get(i))));

                listeZone.add(new ArrayList<Double>());

                listeZone.get(i).add(Double.parseDouble((String)(array2.get(0))));
                listeZone.get(i).add(Double.parseDouble((String)(array2.get(1))));
                System.out.println(Double.parseDouble((String)(array2.get(0))));
                System.out.println(Double.parseDouble((String)(array2.get(1))));
            }
            afficheZone(listeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afficheZone(ArrayList<ArrayList<Double>> listeZone)
    {
        PolygonOptions rectOptions = new PolygonOptions();
        rectOptions.strokeColor(Color.BLACK);
        rectOptions.fillColor(0x5500ff00);
        rectOptions.strokeWidth(2);

        for (int i = 0; i < listeZone.size(); i++)
            rectOptions.add(new LatLng(listeZone.get(i).get(0), listeZone.get(i).get(1)));

        googleMap.addPolygon(rectOptions);
    }
}