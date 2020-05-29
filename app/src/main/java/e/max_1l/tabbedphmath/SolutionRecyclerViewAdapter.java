package e.max_1l.tabbedphmath;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ru.noties.jlatexmath.JLatexMathView;

public class SolutionRecyclerViewAdapter extends RecyclerView.Adapter<SolutionRecyclerViewAdapter.SolutionViewHolder> {
    private ArrayList<String> lines;
    private Context context;
    private TabLayout tab;

    public SolutionRecyclerViewAdapter(Context context, TabLayout tab) {
        ArrayList<String> tlist = MainActivity.tempHistoryList;
        if (tlist.size() > 0){
            if (tlist.get(0).equals("")){
                tlist.remove(0);
            }
        }
        this.lines = tlist;
        this.context = context;
        this.tab = tab;
    }

    @NonNull
    @Override
    public SolutionRecyclerViewAdapter.SolutionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_history_item, viewGroup, false);
        SolutionViewHolder mvh = new SolutionViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionRecyclerViewAdapter.SolutionViewHolder mViewHolder, int i) {
        mViewHolder.latexView.setLatex(lines.get(lines.size() - 1 - i));



        mViewHolder.latexView.setOnClickListener(v -> {
            MainActivity.changingEq = lines.get(lines.size() - 1 - i);
            MainActivity.needsToChange = true;
            tab.getTabAt(0).select();
        });

        mViewHolder.buttonDelete.setOnClickListener(v -> {
            lines.remove(lines.size() - 1 - i);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }



    public class SolutionViewHolder extends RecyclerView.ViewHolder{

        JLatexMathView latexView;
        RelativeLayout layout;
        ImageButton buttonDelete;

        public SolutionViewHolder(@NonNull View itemView) {
            super(itemView);
            latexView = itemView.findViewById(R.id.j_latex_math_view_history);
            layout = itemView.findViewById(R.id.item_history_layout);
            buttonDelete = itemView.findViewById(R.id.button_delete_history_piece);
        }
    }

}
