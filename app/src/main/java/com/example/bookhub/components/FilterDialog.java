package com.example.bookhub.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.bookhub.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class FilterDialog extends MaterialAlertDialogBuilder {



    private View dialogView;
    private MaterialAutoCompleteTextView etUniversity;
    private MaterialAutoCompleteTextView etDepartments;
    private MaterialAutoCompleteTextView etCategory;

    public FilterDialog(@NonNull Context context) {
        super(context);

        init();
    }

    public FilterDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        // Inflate the custom view
        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.filter_dialog, null);

        // Set the custom view to the dialog
        super.setView(dialogView);

        // Set the title
        super.setTitle("Filter");

        // Make the dialog not cancellable
        super.setCancelable(false);

        // Initialize the MaterialAutoCompleteTextView elements
        etUniversity = dialogView.findViewById(R.id.etUniversity);
        etDepartments = dialogView.findViewById(R.id.etDepartments);
        etCategory = dialogView.findViewById(R.id.etCategory);
        String[] universities = super.getContext().getResources().getStringArray(R.array.jordan_universities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(super.getContext(), R.layout.list_item, universities);
        etUniversity.setAdapter(adapter);

        String[] departments = super.getContext().getResources().getStringArray(R.array.departments_array);
        ArrayAdapter<String> departmentsAdapter = new ArrayAdapter<>(super.getContext(), R.layout.list_item, departments);
        etDepartments.setAdapter(departmentsAdapter);
        // Add your custom view handling logic here
        // For example, set adapters or add text change listeners to the AutoCompleteTextViews
    }


    // Add additional methods if needed
    public String getUniversityText() {
        return etUniversity.getText().toString();
    }

    public String getDepartmentsText() {
        return etDepartments.getText().toString();
    }

    public String getCategoryText() {
        return etCategory.getText().toString();
    }
}

