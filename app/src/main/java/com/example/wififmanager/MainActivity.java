package com.example.wififmanager;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, ExampleDialogOpenFile.ExampleDialogListener {

    private TextView entry, display;
    private Button start, stop, save, getinfo, getfiles;
    private boolean flag;
    ArrayList<String> files = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display= (TextView)findViewById(R.id.display);
        entry= (TextView)findViewById(R.id.entry);
        display.setMovementMethod(new ScrollingMovementMethod());
        entry.setMovementMethod(new ScrollingMovementMethod());
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        save = (Button)findViewById(R.id.save);
        getinfo = (Button)findViewById(R.id.getinfo);
        getfiles =(Button)findViewById(R.id.getfiles);


        flag = false;


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                String st = "";
//                for(int i=0;i<60;i++){
//
//                }

                final Handler handler = new Handler();
                final int delay = 1000; //milliseconds

                handler.postDelayed(new Runnable(){
                    public void run(){
                        //do something
                        //String st = "";
                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        //st = st + wifiInfo.getRssi() + "\n";
                        display.setText(display.getText().toString() + wifiInfo.getRssi() + "\n");


                        handler.postDelayed(this, delay);
                    }
                }, delay);


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                openDialogFile();
//                try {
//                    //entry.setText(read("t1.txt"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        getinfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String st = wifiInfo.getRssi() + "\n" + wifiInfo.getIpAddress() + "\n" + wifiInfo.getLinkSpeed()
                        + "\n" +  wifiInfo.getNetworkId() + "\n" + wifiInfo.getMacAddress() + "\n"
                        + wifiInfo.getSSID() + "\n" + wifiInfo.getFrequency() + "\n" + wifiInfo.getBSSID() + "\n";
                display.setText(st);
            }
        });

        getfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st="";
                //for(int i=0;i<files.size();i++){
                //    st += files.get(i) + "\n";
                //}
                try {
                    entry.setText(read("NamesOfFile.txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void openDialogFile() {

        ExampleDialogOpenFile exampleDialogOpenFile = new ExampleDialogOpenFile();
        exampleDialogOpenFile.show(getSupportFragmentManager(), "eaxmple dialog  open file");
    }

    private void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialogue");
    }


    public void save(String file, String text) throws IOException {
        files.add(file);
        //save("NamesOfFile.txt", "\n"+file);
        FileOutputStream fos = openFileOutput(file, Context.MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();
        Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
    }


    public String read(String file) throws IOException {
        String text= "";



        FileInputStream fis = openFileInput(file);
        int size = fis.available();
        byte[] buffer = new byte[size];
        fis.read(buffer);
        fis.close();
        text = new String(buffer);

        return text;
    }

    @Override
    public void createFile(String filename) {
        try {
            save(filename, display.getText().toString());
            save("NamesOfFile.txt", "\n"+filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openFile(String filename) {
        try {
            entry.setText(read(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
