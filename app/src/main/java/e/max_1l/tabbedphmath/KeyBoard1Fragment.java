package e.max_1l.tabbedphmath;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class KeyBoard1Fragment extends Fragment implements View.OnClickListener {

    OnKeyBoard1FragmentListener myListener;

    public interface OnKeyBoard1FragmentListener{
        void OnDoKeyBoard1FragmentListener(String msg, int type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_keyboard1, container, false);
        Button keyButton00 = v.findViewById(R.id.key_button_0_0);
        keyButton00.setEnabled(false);
        //Button keyButton01 = v.findViewById(R.id.key_button_0_1);
        //Button keyButton02 = v.findViewById(R.id.key_button_0_2);
        Button keyButton03 = v.findViewById(R.id.key_button_0_3);
        Button keyButton04 = v.findViewById(R.id.key_button_0_4);
        Button keyButton05 = v.findViewById(R.id.key_button_0_5);
        keyButton05.setEnabled(false);
        //Button keyButton06 = v.findViewById(R.id.key_button_0_6);
        Button keyButton07 = v.findViewById(R.id.key_button_0_7);

        keyButton00.setOnClickListener(this);
        //keyButton01.setOnClickListener(this);
        //keyButton02.setOnClickListener(this);
        keyButton03.setOnClickListener(this);
        keyButton04.setOnClickListener(this);
        keyButton05.setOnClickListener(this);
        //keyButton06.setOnClickListener(this);
        keyButton07.setOnClickListener(this);


        Button keyButton10 = v.findViewById(R.id.key_button_1_0);
        Button keyButton11 = v.findViewById(R.id.key_button_1_1);
        Button keyButton12 = v.findViewById(R.id.key_button_1_2);
        Button keyButton13 = v.findViewById(R.id.key_button_1_3);
        Button keyButton14 = v.findViewById(R.id.key_button_1_4);
        Button keyButton15 = v.findViewById(R.id.key_button_1_5);
        Button keyButton16 = v.findViewById(R.id.key_button_1_6);
        Button keyButton17 = v.findViewById(R.id.key_button_1_7);

        keyButton10.setOnClickListener(this);
        keyButton11.setOnClickListener(this);
        keyButton12.setOnClickListener(this);
        keyButton13.setOnClickListener(this);
        keyButton14.setOnClickListener(this);
        keyButton15.setOnClickListener(this);
        keyButton16.setOnClickListener(this);
        keyButton17.setOnClickListener(this);


        Button keyButton20 = v.findViewById(R.id.key_button_2_0);
        Button keyButton21 = v.findViewById(R.id.key_button_2_1);
        Button keyButton22 = v.findViewById(R.id.key_button_2_2);
        Button keyButton23 = v.findViewById(R.id.key_button_2_3);
        Button keyButton24 = v.findViewById(R.id.key_button_2_4);
        Button keyButton25 = v.findViewById(R.id.key_button_2_5);
        Button keyButton26 = v.findViewById(R.id.key_button_2_6);
        Button keyButton27 = v.findViewById(R.id.key_button_2_7);

        keyButton20.setOnClickListener(this);
        keyButton21.setOnClickListener(this);
        keyButton22.setOnClickListener(this);
        keyButton23.setOnClickListener(this);
        keyButton24.setOnClickListener(this);
        keyButton25.setOnClickListener(this);
        keyButton26.setOnClickListener(this);
        keyButton27.setOnClickListener(this);


        Button keyButton30 = v.findViewById(R.id.key_button_3_0);
        Button keyButton31 = v.findViewById(R.id.key_button_3_1);
        Button keyButton32 = v.findViewById(R.id.key_button_3_2);
        Button keyButton33 = v.findViewById(R.id.key_button_3_3);
        Button keyButton34 = v.findViewById(R.id.key_button_3_4);
        Button keyButton35 = v.findViewById(R.id.key_button_3_5);
        Button keyButton36 = v.findViewById(R.id.key_button_3_6);
        Button keyButton37 = v.findViewById(R.id.key_button_3_7);

        keyButton30.setOnClickListener(this);
        keyButton31.setOnClickListener(this);
        keyButton32.setOnClickListener(this);
        keyButton33.setOnClickListener(this);
        keyButton34.setOnClickListener(this);
        keyButton35.setOnClickListener(this);
        keyButton36.setOnClickListener(this);
        keyButton37.setOnClickListener(this);


        Button keyButton40 = v.findViewById(R.id.key_button_4_0);
        Button keyButton41 = v.findViewById(R.id.key_button_4_1);
        Button keyButton42 = v.findViewById(R.id.key_button_4_2);
        Button keyButton43 = v.findViewById(R.id.key_button_4_3);
        Button keyButton44 = v.findViewById(R.id.key_button_4_4);
        Button keyButton45 = v.findViewById(R.id.key_button_4_5);
        Button keyButton46 = v.findViewById(R.id.key_button_4_6);
        Button keyButton47 = v.findViewById(R.id.key_button_4_7);

        keyButton40.setOnClickListener(this);
        keyButton41.setOnClickListener(this);
        keyButton42.setOnClickListener(this);
        keyButton43.setOnClickListener(this);
        keyButton44.setOnClickListener(this);
        keyButton45.setOnClickListener(this);
        keyButton46.setOnClickListener(this);
        keyButton47.setOnClickListener(this);



        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnKeyBoard1FragmentListener){
            myListener = (OnKeyBoard1FragmentListener) context;
        }
        else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.key_button_0_0:
                myListener.OnDoKeyBoard1FragmentListener("", -1);
                break;
            /*case R.id.key_button_0_1:
                myListener.OnDoKeyBoard1FragmentListener("", -1);
                break;
            case R.id.key_button_0_2:
                myListener.OnDoKeyBoard1FragmentListener("", -1);
                break;*/
            case R.id.key_button_0_3:
                myListener.OnDoKeyBoard1FragmentListener("", 1);
                break;
            case R.id.key_button_0_4:
                myListener.OnDoKeyBoard1FragmentListener("", 2);
                break;
            case R.id.key_button_0_5:
                myListener.OnDoKeyBoard1FragmentListener("", -1);
                break;
            /*case R.id.key_button_0_6:
                myListener.OnDoKeyBoard1FragmentListener("", -1);
                break;*/
            case R.id.key_button_0_7:
                myListener.OnDoKeyBoard1FragmentListener("", 3);
                break;
            case R.id.key_button_1_0:
                myListener.OnDoKeyBoard1FragmentListener("F", 0);
                break;
            case R.id.key_button_1_1:
                myListener.OnDoKeyBoard1FragmentListener("l", 0);
                break;
            case R.id.key_button_1_2:
                myListener.OnDoKeyBoard1FragmentListener("E", 0);
                break;
            case R.id.key_button_1_3:
                myListener.OnDoKeyBoard1FragmentListener("7", 0);
                break;
            case R.id.key_button_1_4:
                myListener.OnDoKeyBoard1FragmentListener("8", 0);
                break;
            case R.id.key_button_1_5:
                myListener.OnDoKeyBoard1FragmentListener("9", 0);
                break;
            case R.id.key_button_1_6:
                myListener.OnDoKeyBoard1FragmentListener("()/()", 4);
                break;
            case R.id.key_button_1_7:
                myListener.OnDoKeyBoard1FragmentListener("+", 0);
                break;
            case R.id.key_button_2_0:
                myListener.OnDoKeyBoard1FragmentListener("m", 0);
                break;
            case R.id.key_button_2_1:
                myListener.OnDoKeyBoard1FragmentListener("t", 0);
                break;
            case R.id.key_button_2_2:
                myListener.OnDoKeyBoard1FragmentListener("q", 0);
                break;
            case R.id.key_button_2_3:
                myListener.OnDoKeyBoard1FragmentListener("4", 0);
                break;
            case R.id.key_button_2_4:
                myListener.OnDoKeyBoard1FragmentListener("5", 0);
                break;
            case R.id.key_button_2_5:
                myListener.OnDoKeyBoard1FragmentListener("6", 0);
                break;
            case R.id.key_button_2_6:
                myListener.OnDoKeyBoard1FragmentListener("(", 0);
                break;
            case R.id.key_button_2_7:
                myListener.OnDoKeyBoard1FragmentListener("-", 0);
                break;
            case R.id.key_button_3_0:
                myListener.OnDoKeyBoard1FragmentListener("k", 0);
                break;
            case R.id.key_button_3_1:
                myListener.OnDoKeyBoard1FragmentListener("U", 0);
                break;
            case R.id.key_button_3_2:
                myListener.OnDoKeyBoard1FragmentListener("I", 0);
                break;
            case R.id.key_button_3_3:
                myListener.OnDoKeyBoard1FragmentListener("1", 0);
                break;
            case R.id.key_button_3_4:
                myListener.OnDoKeyBoard1FragmentListener("2", 0);
                break;
            case R.id.key_button_3_5:
                myListener.OnDoKeyBoard1FragmentListener("3", 0);
                break;
            case R.id.key_button_3_6:
                myListener.OnDoKeyBoard1FragmentListener(")", 0);
                break;
            case R.id.key_button_3_7:
                myListener.OnDoKeyBoard1FragmentListener("*", 0);
                break;
            case R.id.key_button_4_0:
                myListener.OnDoKeyBoard1FragmentListener("n", 0);
                break;
            case R.id.key_button_4_1:
                myListener.OnDoKeyBoard1FragmentListener("a", 0);
                break;
            case R.id.key_button_4_2:
                myListener.OnDoKeyBoard1FragmentListener("B", 0);
                break;
            case R.id.key_button_4_3:
                myListener.OnDoKeyBoard1FragmentListener("_{}", 5);
                break;
            case R.id.key_button_4_4:
                myListener.OnDoKeyBoard1FragmentListener("0", 0);
                break;
            case R.id.key_button_4_5:
                myListener.OnDoKeyBoard1FragmentListener("...", 6);
                break;
            case R.id.key_button_4_6:
                myListener.OnDoKeyBoard1FragmentListener("=", 0);
                break;
            case R.id.key_button_4_7:
                myListener.OnDoKeyBoard1FragmentListener("/", 0);
                break;
        }
    }
}
