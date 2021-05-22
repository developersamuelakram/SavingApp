package com.example.saverapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.saverapp.Model.GoalsModel;
import com.example.saverapp.R;

import java.util.List;


public class AllGoalsAdapter extends RecyclerView.Adapter<AllGoalsAdapter.myViewHolder>{

    List<GoalsModel> goalsModelList;
    OnGoalClicked onGoalClicked;

    public AllGoalsAdapter(OnGoalClicked onGoalClicked) {
        this.onGoalClicked = onGoalClicked;
    }

    @Override
    public myViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allgoallist, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder( AllGoalsAdapter.myViewHolder holder, int position) {


        holder.goalName.setText(goalsModelList.get(position).getGoalname());
        holder.goalAmount.setText(goalsModelList.get(position).getGoalamount());
        int maxprogress = Integer.parseInt(goalsModelList.get(position).getGoalamount());
        int currentprogress = goalsModelList.get(position).getCurrentAmount();


        int finalprecent = (currentprogress * 100)/maxprogress;


        holder.goalpercent.setText(String.valueOf(finalprecent)+"%");



        holder.pb.setMax(maxprogress);
        holder.pb.setProgress(currentprogress);





    }

    public void setGoalsModelList(List<GoalsModel> goalsModelList) {
        this.goalsModelList = goalsModelList;
    }

    @Override
    public int getItemCount() {
        return goalsModelList.size();
    }

    class myViewHolder extends ViewHolder implements View.OnClickListener{

        TextView goalName, goalAmount, goalpercent;
        ProgressBar pb;


        public myViewHolder( View itemView) {
            super(itemView);

            goalName = itemView.findViewById(R.id.Goalname);
            goalAmount = itemView.findViewById(R.id.totalAmount);
            goalpercent = itemView.findViewById(R.id.goalpercent);
            pb = itemView.findViewById(R.id.pb);
            goalName.setOnClickListener(this);
            goalAmount.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onGoalClicked.goalClicked(getAdapterPosition(), goalsModelList);
        }
    }

    public interface OnGoalClicked{

        void goalClicked(int position, List<GoalsModel> goalsModels);
    }
}
