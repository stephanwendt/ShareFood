package de.htwds.mada.foodsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditSearchActivity extends ActionBarActivity {
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_search);
        progress = new ProgressDialog(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editSearch(View view){
                    showProgress(view);

                    Intent i  = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(i);
    }

    private boolean testInput(View view){
        EditText et = (EditText) view;

        String input = et.getText().toString().trim();
        if (input.isEmpty()){
            Toast.makeText(getBaseContext(), Constants.NO_ARGUMENT, Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(getBaseContext(), Constants.WAIT_INFO, Toast.LENGTH_LONG).show();
        return true;
    }

    void showProgress(View view){

        progress.setMessage(Constants.PLEASE_WAIT);
        progress.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.show();

        final int totalProgressTime = 600;

        final Thread t = new Thread(){

            @Override
            public void run(){

                int jumpTime = 0;
                while(jumpTime < totalProgressTime){
                    try {
//                        sleep(200);
                        jumpTime ++;
                        progress.setProgress(jumpTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        t.start();

    }
}
