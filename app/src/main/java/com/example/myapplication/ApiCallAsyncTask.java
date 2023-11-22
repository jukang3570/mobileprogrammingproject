package com.example.myapplication;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;

public class ApiCallAsyncTask extends AsyncTask<Void, Void, Integer>{
    private Integer temp;
    private String a = "Dongjak-gu";

    // Constructor that accepts a TextView as a parameter
    public ApiCallAsyncTask() {
        this.temp = temp;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            // Call the ApiExplorer class in the background
            return ApiExplorer.makeApiCall(a);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    protected void onPostExecute(Integer result) {
    }

    public Integer getTemp(Integer result){
        return result;
    }

}
