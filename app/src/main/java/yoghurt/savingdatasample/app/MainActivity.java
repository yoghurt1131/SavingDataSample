package yoghurt.savingdatasample.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    int[] ids = {R.id.btn1,R.id.btn2,R.id.btn3};
    Button[] btns = new Button[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i=0;i<ids.length;i++){
            btns[i] = (Button)findViewById(ids[i]);
            btns[i].setOnClickListener(new BtnClickListener());
        }
    }

    class BtnClickListener implements View.OnClickListener{
        public void onClick(View v){
            Intent i = null;
            Context ac  = MainActivity.this;
            switch(v.getId()){
                case R.id.btn1:
                    i = new Intent(ac,PreferenceActivity.class);
                    startActivity(i);
                    break;
                case R.id.btn2:
                    i = new Intent(ac,FileAcitivity.class);
                    startActivity(i);
                    break;
                case R.id.btn3:
                    i = new Intent(ac,DatabaseActivity.class);
                    startActivity(i);
                    break;
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
