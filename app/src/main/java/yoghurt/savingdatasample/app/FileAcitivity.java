package yoghurt.savingdatasample.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileAcitivity extends ActionBarActivity {

    int[] ids = {R.id.btn_save,R.id.btn_display,R.id.btn_delete,R.id.btn_update};
    private static final String FILE_NAME="Filefile";

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
            String str="";
            switch(v.getId()){
                case R.id.btn_save:
                    EditText et_name = (EditText)findViewById(R.id.et_name);
                    EditText et_value = (EditText)findViewById(R.id.et_value);
                    String item_name = et_name.getText().toString();
                    String item_value = et_value.getText().toString();
                    String msg=null;

                    try{
                        FileOutputStream stream = openFileOutput(FILE_NAME,MODE_APPEND);
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(stream));

                        out.write(getString(R.string.item_name)+":"+item_name+","+getString(R.string.item_value)+":"+item_value);
                        out.newLine();
                        out.close();

                        msg = getString(R.string.toast_save);
                    }catch(Exception e){
                        msg = getString(R.string.toast_failed);
                    }
                    Toast.makeText(FileAcitivity.this,msg,Toast.LENGTH_SHORT).show();

                    break;
                case R.id.btn_display:

                    try{
                        FileInputStream stream = openFileInput(FILE_NAME);
                        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
                        String line = "";
                        while((line=in.readLine())!=null){
                            str+=line+"\n";
                        }
                        in.close();

                    }catch(Exception e){
                        str = getString(R.string.toast_failed);
                    }
                    Toast.makeText(FileAcitivity.this,str,Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_delete:
                    try{
                        deleteFile(FILE_NAME);
                        str = getString(R.string.toast_delete);
                    }catch(Exception e){
                        str = getString(R.string.toast_failed);
                    }
                    Toast.makeText(FileAcitivity.this,str,Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_update:
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.file_acitivity, menu);
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
