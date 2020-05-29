package e.max_1l.tabbedphmath;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.noties.jlatexmath.JLatexMathView;

public class MRecyclerViewAdapter extends RecyclerView.Adapter<MRecyclerViewAdapter.MViewHolder> {
    private ArrayList<String> lines;
    private Context context;

    public MRecyclerViewAdapter(Context context, ArrayList<String> lines) {
        this.lines = lines;
        this.context = context;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        MViewHolder mvh = new MViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder mViewHolder, int i) {
        mViewHolder.latexView.setLatex(lines.get(i));
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }


    public class MViewHolder extends RecyclerView.ViewHolder{

        JLatexMathView latexView;
        RelativeLayout layout;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            latexView = itemView.findViewById(R.id.j_latex_math_view);
            layout = itemView.findViewById(R.id.item_layout);
        }
    }

}
