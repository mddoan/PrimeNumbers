//package com.dangdoan.primenumbers;
//
//import android.os.AsyncTask;
//import android.widget.ProgressBar;
//import android.widget.Toast;

/**
 * Created by dangdoan on 9/19/16.
 */
//public class ComputePrimeNumbersAsync extends AsyncTask<Integer, Long, Boolean> {
//    public static final String TAG = ComputePrimeNumbersAsync.class.getSimpleName();
//    private Callback mCallback;
//    private int count;
//    StringBuilder stringBuilder;
//    private ProgressBar progressBar;
//
//    public interface Callback {
//        void onProcessComplete(StringBuilder stringBuilder, int count);
//
//    }
//
//    public ComputePrimeNumbersAsync(Object obj){
//        mCallback = (Callback) obj;
//    }
//
//    @Override
//    protected Boolean doInBackground(Integer... params) {
//        boolean success = false;
//        int input = params[0];
//        if (input > 2) {
//            stringBuilder = new StringBuilder();
//            int start = 2;
//            int limit = input;
//            boolean[] isPrime = new boolean[limit];
//            for (int i = start; i < limit; i++) {
//                isPrime[i] = true;
//            }
//
//            for (int i = start; i < Math.sqrt(limit); i++) {
//                if (isPrime[i]) {
//                    for (int j = (int) Math.pow(i, 2); j < limit; j = j + i) {
//                        isPrime[j] = false;
//
//                    }
//                }
//            }
//
//            for (int i = start; i < limit; i++) {
//                if (isPrime[i]) {
//                    stringBuilder.append(i + ",");
//                    count++;
//                }
//            }
//            success = true;
//        }
//
//        return success;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        // SHOW THE SPINNER WHILE LOADING FEEDS
//        linlaHeaderProgress.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected void onPostExecute(Boolean success) {
//        mCallback.onProcessComplete(stringBuilder, count);
//    }
//}
