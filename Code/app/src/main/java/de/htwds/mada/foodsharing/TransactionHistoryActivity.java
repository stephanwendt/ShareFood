package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class TransactionHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction_history, menu);
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
    public void transactionEdit(View view){
        Button btn = (Button) view;
        switch (btn.getId()) {
            case R.id.transaction_cancel_btn:
                fillIntent(TransactionHistoryActivity.class);
                break;
            case R.id.transaction_close_tr_btn:
                fillIntent(TransactionHistoryActivity.class);
                break;
            case R.id.transaction_back:
                fillIntent(ProfileDisplayActivity.class);
                break;

            default:
        }
    }

    void fillIntent(Class activity){
        Intent i;
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }
}
