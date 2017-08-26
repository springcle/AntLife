package com.ant.antlife.antlife;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Created by NAKNAK on 2017-08-27.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawFragment extends Fragment {

    Context context;

    /**
     * Hamberger Header (닉네임, 주력드론명, 에디트버튼 등)
     **/
    TextView drawer_nick;
    TextView drone_name;
    ImageView drawer_image;
    Button edit; // 원 모양 에디트 버튼(드론사진)

    /**
     * Hamberger Body (내활동내역, 비행가이드, 설정)
     **/
    LinearLayout my_activity_btn;
    LinearLayout flying_guide_btn;
    LinearLayout setting_btn;

    /**
     * Hamberger Footer (회원약관, 프로그램정보)
     **/
    Button member_stipulation_btn;
    Button program_info_btn;

    public DrawFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.hamberger_bar, container, false);
        /** 햄버거 헤더 **/
        drawer_nick = (TextView) view.findViewById(R.id.nickname);
        drone_name = (TextView) view.findViewById(R.id.drone_name);
        drawer_image = (ImageView) view.findViewById(R.id.circle_image);
        edit = (Button) view.findViewById(R.id.hamberger_edit_btn);
        /** 햄버거 바디 **/
        my_activity_btn = (LinearLayout) view.findViewById(R.id.my_activity_btn);
        flying_guide_btn = (LinearLayout) view.findViewById(R.id.flying_guide_btn);
        setting_btn = (LinearLayout) view.findViewById(R.id.setting_btn);
        /** 햄버거 푸터 **/
        member_stipulation_btn = (Button) view.findViewById(R.id.member_stipulation_btn);
        program_info_btn = (Button) view.findViewById(R.id.program_info_btn);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
