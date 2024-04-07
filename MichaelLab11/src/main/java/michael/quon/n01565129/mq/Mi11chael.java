// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Mi11chael extends Fragment {

    private EditText courseNameEdt, courseDescEdt;
    private Button addBtn, deleteBtn;
    private RecyclerView courseRV;

    private ArrayList<CourseModal> courseModalArrayList;
    private CourseAdapter adapter;

    public Mi11chael() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi11chael, container, false);

        // Initializing UI components
        courseNameEdt = view.findViewById(R.id.Mic_idEdtCourseName);
        courseDescEdt = view.findViewById(R.id.Mic_idEdtCourseDescription);
        addBtn = view.findViewById(R.id.Mic_idBtnAdd);
        deleteBtn = view.findViewById(R.id.Mic_delete_button);
        courseRV = view.findViewById(R.id.Mic_idRVCourses);

        // Load data from SharedPreferences
        loadDataFromSharedPreferences();

        // Build RecyclerView
        buildRecyclerView();

        addBtn.setOnClickListener(v -> {
            // Add course to the list
            String name = courseNameEdt.getText().toString().toUpperCase(); // Convert to uppercase
            String desc = courseDescEdt.getText().toString();
            if (!name.isEmpty() && !desc.isEmpty()) {
                if (isValidCourseName(name)) {
                    // Store course into Firebase Database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.courses));
                    String courseId = databaseReference.push().getKey(); // Generate unique key for the course
                    CourseModal newCourse = new CourseModal(courseId, name, desc); // Use the constructor that accepts courseId, name, and description
                    assert courseId != null;
                    databaseReference.child(courseId).setValue(newCourse); // Add course to Firebase

                    // Add course to RecyclerView
                    courseModalArrayList.add(newCourse);
                    adapter.notifyDataSetChanged(); // Notify RecyclerView adapter

                    // Save data to SharedPreferences
                    saveDataToSharedPreferences();

                    // Clear input fields
                    courseNameEdt.setText(getString(R.string.blank)); // Clear course name field
                    courseDescEdt.setText(getString(R.string.blank)); // Clear course description field
                } else {
                    courseNameEdt.setError(getString(R.string.invalid_course_name_format));
                }
            } else {
                Toast.makeText(getContext(), R.string.please_fill_in_both_fields, Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            if (courseModalArrayList.isEmpty()) {
                // If no data available
                Toast.makeText(getContext(), R.string.no_data_to_delete, Toast.LENGTH_SHORT).show();
            } else {
                // Clear the RecyclerView and the list of courses
                courseModalArrayList.clear();
                adapter.notifyDataSetChanged(); // Notify RecyclerView adapter

                // Delete the entire records from Firebase database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.courses));
                databaseReference.removeValue().addOnSuccessListener(aVoid -> {
                    // Data successfully deleted from Firebase
                    Toast.makeText(getContext(), R.string.deleted_all_courses, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    // Failed to delete data from Firebase
                    Toast.makeText(getContext(), getString(R.string.failed_to_delete_data_from_firebase) + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

                // Clear data in SharedPreferences
                clearSharedPreferences();
            }
        });

        adapter.setOnItemLongClickListener(position -> {
            // Prompt the user to confirm deletion
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.confirm_delete_message)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        // If user confirms deletion, remove the selected record
                        deleteCourse(position);
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        return view;
    }

    private void buildRecyclerView() {
        courseModalArrayList = new ArrayList<>();
        adapter = new CourseAdapter(courseModalArrayList, getContext());
        courseRV.setLayoutManager(new LinearLayoutManager(getContext()));
        courseRV.setAdapter(adapter);
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.courses), null);
        Type type = new TypeToken<ArrayList<CourseModal>>() {}.getType();
        courseModalArrayList = gson.fromJson(json, type);

        if (courseModalArrayList == null) {
            courseModalArrayList = new ArrayList<>();
        }
    }

    private void saveDataToSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(courseModalArrayList);
        editor.putString(getString(R.string.courses), json);
        editor.apply();

        Toast.makeText(getContext(), R.string.saved_data_to_sharedpreferences, Toast.LENGTH_SHORT).show();
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getString(R.string.courses));
        editor.apply();
    }

    private boolean isValidCourseName(String name) {
        // Validate course name format: 4 letters, - , 3 to 4 numbers
        return name.matches("[A-Z]{4}-\\d{3,4}");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Add a ValueEventListener to listen for changes in Firebase database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.courses));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing data list
                courseModalArrayList.clear();
                // Iterate through the dataSnapshot to retrieve data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Deserialize the data and add it to the list
                    CourseModal courseModal = snapshot.getValue(CourseModal.class);
                    if (courseModal != null) {
                        courseModalArrayList.add(courseModal);
                    }
                }
                // Notify the adapter of the data change
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Toast.makeText(getContext(), getString(R.string.failed_to_fetch_data_from_firebase) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to delete a specific course from RecyclerView and Firebase Realtime Database
    private void deleteCourse(int position) {
        // Get the courseId of the course to be deleted
        CourseModal deletedCourse = courseModalArrayList.get(position);
        String courseId = deletedCourse.getCourseId();

        // Remove the course from the RecyclerView
        courseModalArrayList.remove(position);
        adapter.notifyItemRemoved(position);

        // Remove the course from the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.courses));
        databaseReference.child(courseId).removeValue().addOnSuccessListener(aVoid -> {
            // Course successfully deleted from Firebase
            Toast.makeText(getContext(), R.string.course_deleted, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Failed to delete course from Firebase
            Toast.makeText(getContext(), getString(R.string.failed_to_delete_course) + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
