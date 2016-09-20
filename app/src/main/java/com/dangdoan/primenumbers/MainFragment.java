package com.dangdoan.primenumbers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


/**
 * Created by dangdoan on 9/15/16.
 */
public class MainFragment extends Fragment{
    private EditText editTextFindN;
    private EditText editTextFindNth;
    private Button buttonSubmit;
    private Button buttonSubmitNth;
    private LinearLayout linearLayoutResultNContainer;
    private int input;
    private int inputNth;
    private RecyclerView mRecyclerView;
    private TextView textViewCount;
    private TextView textViewFindNthResult;
    private PrimeRecyclerAdapter mAdapter;
    private static final int READ_BLOCK_SIZE = 100;
    private static final String OUT = "prime";
    private int count = 0;
    private ProgressBar mProgress;
    private Handler mHandler;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        View root = (ViewGroup)inflater.inflate(R.layout.frag_main, container, false);
        editTextFindN = (EditText) root.findViewById(R.id.editTextFindN);
        editTextFindN.addTextChangedListener(mTextWatch);

        buttonSubmit = (Button) root.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(buttonSubmitClicked);
        buttonSubmit.setEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycleView);

        //frameLayoutRecyclerView = (FrameLayout) root.findViewById(R.id.frameLayoutRecyclerView);
        float rcvHeight = StaticMethods.getScreenHeight(getActivity())/4f;
        mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, (int)rcvHeight));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PrimeRecyclerAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        textViewCount = (TextView) root.findViewById(R.id.textViewCount);


        linearLayoutResultNContainer = (LinearLayout) root.findViewById(R.id
                .linearLayoutResultNContainer);
        linearLayoutResultNContainer.setVisibility(View.GONE);

        editTextFindNth = (EditText) root.findViewById(R.id.editTextFindNth);
        editTextFindNth.addTextChangedListener(mTextWatchNth);
        textViewFindNthResult = (TextView) root.findViewById(R.id.textViewFindNthResult);
        textViewFindNthResult.setVisibility(View.GONE);
        buttonSubmitNth = (Button) root.findViewById(R.id.buttonSubmitNth);
        buttonSubmitNth.setEnabled(false);
        buttonSubmitNth.setOnClickListener(buttonSubmitNthClicked);

        return root;
    }

    @Override
    public void onStop(){
        clearOldOutput();
        super.onStop();
    }

    private TextWatcher mTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0) {
                clearInput();
                input = Integer.parseInt(s.toString());
                buttonSubmit.setEnabled(true);
            }else{
                buttonSubmit.setEnabled(false);
            }
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override public void afterTextChanged(Editable s) {}
    };


    private TextWatcher mTextWatchNth = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0) {
                inputNth = Integer.parseInt(s.toString());
                buttonSubmitNth.setEnabled(true);
            }else{
                hideNthResultView();
                buttonSubmitNth.setEnabled(false);
            }
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override public void afterTextChanged(Editable s) {}
    };



    private void clearInput(){
        mAdapter.reset();
        mAdapter.notifyDataSetChanged();
        hideNResultView();
    }

    private View.OnClickListener buttonSubmitClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showProgress();
            computePrimeNumbers();
        }
    };

    private View.OnClickListener buttonSubmitNthClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getNthPrime();
            showNthResultView();
        }
    };

    private void getNthPrime(){
        if(inputNth > count){
            textViewFindNthResult.setText(getString(R.string
                    .search_nth_prime_not_found_result_text, "" + inputNth, "" + input));
        }else{
            String data = readFile();
            if(data == null){
                textViewFindNthResult.setText(getString(R.string
                        .search_nth_prime_not_found_result_text, "" + inputNth, "" + input));
            }else{
                String[] s = data.split(",");
                String found = s[inputNth - 1];
                textViewFindNthResult.setText(getString(R.string
                        .search_nth_prime_found_result_text, found));
            }
        }
    }

    private void computePrimeNumbers(){
        clearOldOutput();
        ComputePrimeNumbersAsync computePrimeNumbersAsync = new ComputePrimeNumbersAsync();
        Integer []params = {input};
        computePrimeNumbersAsync.execute(params);

//        if(input < 2){
//            Toast toast = Toast.makeText(getActivity(), "Pleas enter number greater than 1", Toast.LENGTH_LONG);
//            toast.show();
//            return;
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//        int start = 2;
//        int limit = input;
//
//
//        boolean [] isPrime = new boolean[limit];
//        for(int i=start; i<limit; i++){
//            isPrime[i] = true;
//        }
//
//        for(int i=start; i<Math.sqrt(limit); i++){
//            if(isPrime[i]){
//                for(int j = (int) Math.pow(i,2); j<limit; j = j+i){
//                    isPrime[j] = false;
//
//                }
//            }
//        }
//
//        for(int i=start; i<limit; i++) {
//            if(isPrime[i]){
//                stringBuilder.append(i + ",");
//                count++;
//            }
//        }


    }

    private void showNResultView(){
        linearLayoutResultNContainer.setVisibility(View.VISIBLE);
        ViewAnimationHelper.expand(linearLayoutResultNContainer);
    }

    private void hideNResultView(){
        ViewAnimationHelper.collapse(linearLayoutResultNContainer);
        linearLayoutResultNContainer.setVisibility(View.GONE);
    }

    private void showNthResultView(){
        textViewFindNthResult.setVisibility(View.VISIBLE);
        ViewAnimationHelper.expand(textViewFindNthResult);
    }

    private void hideNthResultView(){
        ViewAnimationHelper.collapse(textViewFindNthResult);
        textViewFindNthResult.setVisibility(View.GONE);
    }

    private void clearOldOutput(){
        writeToFile("", Context.MODE_PRIVATE);

    }

    private void storePrimesToBin(String data){
        writeToFile(data, Context.MODE_APPEND);
    }

    private void writeToFile(String strToWrite, int mode){
        String filename = OUT;
        FileOutputStream outputStream;

        try {
            outputStream = getActivity().openFileOutput(filename, mode);
            outputStream.write(strToWrite.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFile() {
        //reading text from file
        String s = "";
        try {
            String fileName = OUT;
            FileInputStream fileIn = getActivity().openFileInput(fileName);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];

            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private class PrimeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private Context mContext;
        private int [] mPrimes;

        public PrimeRecyclerAdapter(Context context){
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, int pos){
            View parent = LayoutInflater.from(mContext).inflate(R.layout.rec_item,
                    container, false);
            return new ViewHolderItem(parent);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos){
            if(holder instanceof ViewHolderItem){
                ((ViewHolderItem) holder).updateView(mPrimes[pos]);
            }

        }

        @Override
        public int getItemCount(){
            if(mPrimes == null){
                return 0;
            }
            return mPrimes.length;
        }

        public class ViewHolderItem extends RecyclerView.ViewHolder{
            private View mParent = null;
            private TextView textViewItem;

            public ViewHolderItem(View view){
                super(view);
                mParent = view;
                textViewItem = (TextView) view.findViewById(R.id.textViewItem);
            }

            public void updateView(int prime){
                textViewItem.setText("" + prime);
            }
        }

        public void setData(){
            String data = readFile();
            if(data == null){
                mPrimes = new int[0];
            }else{
                String[] s = data.split(",");
                mPrimes = new int[s.length];
                for(int i=0; i<s.length; i++){
                    mPrimes[i] = Integer.valueOf(s[i]);
                }
            }
        }

        public void reset(){
            mPrimes = new int[0];
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // do nothing for now
        } else {
            // do nothing for now
        }
    }

    private void showProgress(){
        ((MainActivity)getActivity()).showProgress();
    }

    private void hideProgress(){
        ((MainActivity)getActivity()).hideProgress();
    }

    public void onProcessComplete(StringBuilder stringBuilder, int count){
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        storePrimesToBin(stringBuilder.toString());
        int strId;
        if(count == 1){
            strId = R.string.count_result_single;
        }else{
            strId = R.string.count_result_plural;
        }
        textViewCount.setText(getString(strId, count));
        mAdapter.setData();
        mAdapter.notifyDataSetChanged();
        showNResultView();
    }


    public class ComputePrimeNumbersAsync extends AsyncTask<Integer, Long, Boolean> {
        private int count;
        StringBuilder stringBuilder;

        @Override
        protected Boolean doInBackground(Integer... params) {
            boolean success = false;
            int input = params[0];
            if (input > 2) {
                stringBuilder = new StringBuilder();
                int start = 2;
                int limit = input;
                boolean[] isPrime = new boolean[limit];
                for (int i = start; i < limit; i++) {
                    isPrime[i] = true;
                }

                for (int i = start; i < Math.sqrt(limit); i++) {
                    if (isPrime[i]) {
                        for (int j = (int) Math.pow(i, 2); j < limit; j = j + i) {
                            isPrime[j] = false;

                        }
                    }
                }

                for (int i = start; i < limit; i++) {
                    if (isPrime[i]) {
                        stringBuilder.append(i + ",");
                        count++;
                    }
                }
                success = true;
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            onProcessComplete(stringBuilder, count);
        }
    }


    Runnable r = new Runnable() {
        @Override
        public void run() {
            hideProgress();
        }
    };
}
