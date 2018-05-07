package com.fun.dante.music;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Manager_Page.OnFragmentInteractionListener,Music_List_Page.OnFragmentInteractionListener,Playing_Page.OnFragmentInteractionListener,Music_List_Page.transpath{

    private EditText editText;
    private String Msg;
    private Music_List_Page music_list_page = new Music_List_Page();
    private ArrayList<String> List_Name = new ArrayList<>();

    public void setMsg(String Msg){
        this.Msg = Msg;
    }

    public String getMsg(){
        return Msg;
    }

    public void setArrayList(ArrayList Array){
        this.List_Name = Array;
    }

    public ArrayList getArrayList(){
        return List_Name;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//这里是调用menu文件夹中的main.xml，在登陆界面label右上角的三角里显示其他功能
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Add_Song_List:
                final View ViewDialog = (View)getLayoutInflater().inflate(R.layout.music_list,null);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("歌单添加")
                        .setView(ViewDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editText = ViewDialog.findViewById(R.id.list_name);
                                List_Name.add(editText.getText().toString());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
                break;
        }
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.Fragment fragment = null;
            Class fragmentclass = null;
            switch (item.getItemId()) {
                case R.id.Manager:
                    fragmentclass = Manager_Page.class;
                    break;
                case R.id.Music_List:
                    fragmentclass = Music_List_Page.class;
                    break;
                case R.id.Playing:
                    fragmentclass = Playing_Page.class;
                    break;
            }
            try{
                fragment = (android.support.v4.app.Fragment)fragmentclass.newInstance();
            }catch (Exception e){
                e.printStackTrace();
            }
            
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Fragment_main,fragment).commit();
            setTitle(item.getTitle());
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Fragment_main,new Music_List_Page()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        music_list_page.setOnListClick(new Music_List_Page.OnListClick() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Fragment_main,new Playing_Page()).commit();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void showMessage(String message) {
        System.out.println(message);
        Msg = message;
    }
}
