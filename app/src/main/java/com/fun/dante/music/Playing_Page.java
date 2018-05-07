package com.fun.dante.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Playing_Page.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Playing_Page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Playing_Page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button Loop;
    private Button Pause;
    private Button Stop;
    private Button Next;
    private Button Last;
    private Button Random_;
    private String Music_Path;
    private MediaPlayer mediaPlayer;
    private TextView textView;
    private TextView PlayTime;
    private TextView TotalTime;
    private SeekBar seekBar;
    private Handler handler;
    private boolean playable;
    private ArrayList<String> List_player;
    private MainActivity mainActivity;
    private ArrayAdapter arrayAdapter_;
    private ListView listView;
    private Calendar calendar;
    private int num;
    private boolean random_play = false;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String tag = "testing...";

    private OnFragmentInteractionListener mListener;

    public Playing_Page() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Playing_Page.
     */
    // TODO: Rename and change types and number of parameters
    public static Playing_Page newInstance(String param1, String param2) {
        Playing_Page fragment = new Playing_Page();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mediaPlayer==null)
        {
            mediaPlayer = new MediaPlayer();
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        textView = getActivity().findViewById(R.id.stateText);
        textView.setText(Music_Path);

        seekBar = getActivity().findViewById(R.id.seekBar);

        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setMax(mediaPlayer.getDuration());

        PlayTime = (TextView) getActivity().findViewById(R.id.playingTime);
        TotalTime = (TextView) getActivity().findViewById(R.id.totalTime);

        Stop = (Button)getActivity().findViewById(R.id.Stop);
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                mediaPlayer.stop();
                seekBar.setProgress(0);
                PlayTime.setText("0:00");
                playable = false;
            }
        });

        Pause = (Button)getActivity().findViewById(R.id.Pause);
        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                }
                else
                {
                    mediaPlayer.start();
                }
            }
        });

        Loop = (Button)getActivity().findViewById(R.id.Loop);
        Loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isLooping())
                {
                    mediaPlayer.setLooping(false);
                }
                else {
                    mediaPlayer.setLooping(true);
                }
            }
        });

        Next = (Button)getActivity().findViewById(R.id.Next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }
                if(num==List_player.size()-1)
                {
                    num=-1;
                }
                if(random_play)
                {
                    calendar = Calendar.getInstance();
                    Random rand = new Random(calendar.get(Calendar.MINUTE)*calendar.get(Calendar.SECOND));
                    num = rand.nextInt(List_player.size());
                    num--;
                }
                Music_Path = List_player.get(++num);
                music_play();
            }
        });

        Last = (Button)getActivity().findViewById(R.id.Last);
        Last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }
                if(num==0)
                {
                    num=List_player.size();
                }
                if(random_play)
                {
                    calendar = Calendar.getInstance();
                    Random rand = new Random(calendar.get(Calendar.MINUTE)*calendar.get(Calendar.SECOND));
                    num = rand.nextInt(List_player.size());
                }
                Music_Path = List_player.get(--num);
                music_play();
            }
        });


        Random_ = (Button)getActivity().findViewById(R.id.Random);
        Random_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(random_play==true)
                {
                    random_play =false;
                }
                else
                {
                    random_play = true;
                }
            }
        });

        listView = getActivity().findViewById(R.id.List);
        List_player = new ArrayList<>();
        List_player = mainActivity.getArrayList();
        arrayAdapter_ = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,List_player);
        listView.setAdapter(arrayAdapter_);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }
                num = i;
                Music_Path = List_player.get(i);
                music_play();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity = (MainActivity)getActivity();
        playable = true;
        if(Music_Path!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        Music_Path = mainActivity.getMsg();


        music_play();
        return inflater.inflate(R.layout.fragment_playing__page, container, false);
    }

    private void music_play(){
        handler = new Handler();
        if(Music_Path!=null&&playable)
        {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(Music_Path);
            }catch (Exception e){
                e.printStackTrace();
            }

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    handler.post(start);
                    seekBar.setOnSeekBarChangeListener(seeklistener);
                    Log.i(tag,"start...");
                }
            });
        }
    }

    private SeekBar.OnSeekBarChangeListener seeklistener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    };



    Runnable start = new Runnable() {
        @Override
        public void run() {
            mediaPlayer.start();
            handler.post(update_seekbar);
        }
    };

    Runnable update_seekbar = new Runnable() {
        @Override
        public void run() {
            if(playable)
            {
                Log.i(tag,String.valueOf(formatTime(mediaPlayer.getCurrentPosition())));
                PlayTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                TotalTime.setText(formatTime(mediaPlayer.getDuration()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(update_seekbar,1000);
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
