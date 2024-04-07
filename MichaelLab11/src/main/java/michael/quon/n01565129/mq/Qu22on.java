package michael.quon.n01565129.mq;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Qu22on extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private TextView fullNameTextView;
    private GoogleMap mMap;

    private static final String CHANNEL_ID = "location_channel";
    private static final int NOTIFICATION_ID = 1001;
    private LocationNotificationHelper notificationHelper;

    public Qu22on() {
        // Required empty public constructor
    }

    class LocationNotificationHelper {

        private final Context mContext;
        private final NotificationManager mNotificationManager;

        public LocationNotificationHelper(Context context) {
            mContext = context;
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        public void showLocationNotification(String address) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(mContext.getString(R.string.current_location))
                    .setContentText(address)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            CharSequence name = mContext.getString(R.string.location_channel);
            String description = mContext.getString(R.string.channel_for_location_notifications);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);

            Notification notification = builder.build();
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }


        private Notification createNotification(String title, String content) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            CharSequence name = getString(R.string.location_channel);
            String description = getString(R.string.channel_for_location_notifications);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);

            return builder.build();
        }

        private boolean checkLocationPermission() {
            return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        private Location getLocation() {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && checkLocationPermission()) {
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qu22on, container, false);

        // Initialize the TextView
        fullNameTextView = rootView.findViewById(R.id.Mic_fullNameTextView);

        // Set the full name and student ID
        fullNameTextView.setText(getString(R.string.name_id));

        // Initialize the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.Mic_mapFragment);
        mapFragment.getMapAsync(this);

        // Initialize the notification helper
        notificationHelper = new LocationNotificationHelper(requireContext());

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move camera to a location of your preference
        LatLng location = new LatLng(43.72891, -79.60656); // Example location: Humber College North Campus
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));

        // Set up map click listener
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Use Geocoder to find an address
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Zoom into the clicked location
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // Add Marker with title and message
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(address.getFeatureName())
                        .snippet(address.getLocality() + getString(R.string._michael)));
                // Update TextView with address
                updateTextViewWithAddress(address);
                // Display Snackbar with the address
                showAddressSnackbar(address);
                // Show notification
                notificationHelper.showLocationNotification(address.getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTextViewWithAddress(Address address) {
        // Update TextView with the address
        String addressString = address.getAddressLine(0); // Get the first address line
        fullNameTextView.setText(addressString);
    }

    private void showAddressSnackbar(Address address) {
        // Display Snackbar with the address
        String addressString = address.getAddressLine(0); // Get the first address line
        Snackbar.make(getView(), addressString, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.dismiss, view -> {
                    // Dismiss Snackbar
                })
                .show();
    }
}
