package e.max_1l.tabbedphmath;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.noties.jlatexmath.JLatexMathView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputFragment extends Fragment {
    String eq = "";
    String x;
    EditText editEq;
    EditText editX;
    JLatexMathView latexView;
    ImageButton clearButton;
    KeyBoard1Fragment keyBoard1Fragment;
    //Button btn;
    Handler h;


    public InputFragment() {
        // Required empty public constructor
    }


    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input, container, false);
        editEq = v.findViewById(R.id.editTextEq);
        editX = v.findViewById(R.id.editTextX);
        latexView = v.findViewById(R.id.j_latex_math_view_input);
        clearButton = v.findViewById(R.id.button_clear_input);


        clearButton.setOnClickListener(v1 -> {
            editEq.setText("");
            editX.setText("");
        });

        h = new Handler() {   // создание хэндлера для доступа к UI фрагмента ввода
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                eq = editEq.getText().toString();
                x = editX.getText().toString();
                Log.d("eq1", "Input.eq = " + eq);
            }
        };

        class InputThread extends Thread { // класс для считывания полей ввода фрагмента в переменные
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(500);
                        h.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }

        InputThread inputThread = new InputThread();
        inputThread.start(); // поток считывания ввода


        FragmentManager fragmentManager = getFragmentManager();
        keyBoard1Fragment = new KeyBoard1Fragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.keyboard_container, keyBoard1Fragment, "keyBoard1Fragment added");
        fragmentTransaction.commit();


        return v;
    }

}
