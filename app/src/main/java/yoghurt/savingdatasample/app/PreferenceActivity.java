package yoghurt.savingdatasample.app;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PreferenceActivity extends ActionBarActivity {

    int[] ids = {R.id.btn_save,R.id.btn_display,R.id.btn_delete,R.id.btn_update};
    private static final String FILE_NAME="Preferencefile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_set_layout);
        Button[] btns = new Button[ids.length];
        for(int i=0;i< btns.length;i++){
            btns[i] = (Button)findViewById(ids[i]);
            btns[i].setOnClickListener(new DataHandleListener());
        }
    }

    class DataHandleListener implements View.OnClickListener{
        public void onClick(View v){

            SharedPreferences preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String item_name=null;
            String item_value=null;
            switch(v.getId()){
                case R.id.btn_save:
                    EditText et_name = (EditText)findViewById(R.id.et_name);
                    EditText et_value = (EditText)findViewById(R.id.et_value);
                    item_name = et_name.getText().toString();
                    item_value = et_value.getText().toString();

                    editor.putString(getString(R.string.name_key),item_name);
                    editor.putString(getString(R.string.value_key),item_value);
                    editor.commit();

                    Toast.makeText(PreferenceActivity.this,getString(R.string.toast_save),Toast.LENGTH_SHORT).show();

                    break;
                case R.id.btn_display:
                    String nameKey = getString(R.string.name_key);
                    String valueKey = getString(R.string.value_key);
                    item_name = preferences.getString(nameKey,"Nothing");
                    item_value = preferences.getString(valueKey,"Nothing");
                    String dis = getString(R.string.toast_display)+getString(R.string.item_name)+":"+item_name+","+getString(R.string.item_value)+item_value;

                    Toast.makeText(PreferenceActivity.this,dis,Toast.LENGTH_LONG).show();

                    break;
                case R.id.btn_delete:

//                    editor.clear();
                    editor.remove(getString(R.string.name_key));
                    editor.remove(getString(R.string.value_key));
                    editor.commit();
                    Toast.makeText(PreferenceActivity.this,getString(R.string.toast_delete),Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_update:
                    break;
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preference, menu);
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
