package com.example.myapplication;

import android.app.Activity;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class addsales extends Activity {

    private AutoCompleteTextView productSpinner;
    private AutoCompleteTextView cusname;
    private EditText salprice;
    private EditText salquantity;

    private MaterialButton save;
    private MaterialButton reset;

    private List<String> customerNames = new ArrayList<>();
    private List<String> productNames = new ArrayList<>();

    private Map<String, String> customerNameToIdMap = new HashMap<>();
    private Map<String, String> productNameToIdMap = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

}
