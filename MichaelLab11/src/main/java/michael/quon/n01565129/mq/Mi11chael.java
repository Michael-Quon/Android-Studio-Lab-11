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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // Add button click listener
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add course to the list
                String name = courseNameEdt.getText().toString().toUpperCase(); // Convert to uppercase
                String desc = courseDescEdt.getText().toString();
                if (!name.isEmpty() && !desc.isEmpty()) {
                    if (isValidCourseName(name)) {
                        courseModalArrayList.add(new CourseModal(name, desc));
                        adapter.notifyDataSetChanged(); // Notify RecyclerView adapter
                        saveDataToSharedPreferences(); // Save data to SharedPreferences
                        courseNameEdt.setText(getString(R.string.blank)); // Clear course name field
                        courseDescEdt.setText(getString(R.string.blank)); // Clear course description field
                    } else {
                        courseNameEdt.setError(getString(R.string.invalid_course_name_format));
                    }
                } else {
                    Toast.makeText(getContext(), R.string.please_fill_in_both_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete button click listener
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if there is data to delete
                if (courseModalArrayList.isEmpty()) {
                    // Display toast indicating no data to delete
                    Toast.makeText(getContext(), R.string.no_data_to_delete, Toast.LENGTH_SHORT).show();
                } else {
                    // Clear the list of courses
                    courseModalArrayList.clear();
                    adapter.notifyDataSetChanged(); // Notify RecyclerView adapter
                    clearSharedPreferences(); // Clear data in SharedPreferences
                    Toast.makeText(getContext(), R.string.deleted_all_courses, Toast.LENGTH_SHORT).show();
                }
            }
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
}
