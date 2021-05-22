package com.example.saverapp.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.saverapp.Model.GoalsModel;

import java.util.List;

public class Viewmodel extends ViewModel implements Repo.OnGoalsAdded {

    Repo repo = new Repo(this);
    MutableLiveData<List<GoalsModel>> mutableLiveData = new MutableLiveData<>();

    public Viewmodel() {

        repo.getAllGoals();
    }

    public LiveData<List<GoalsModel>> GetAllGoals(){

        return mutableLiveData;
    }

    @Override
    public void goalsAdding(List<GoalsModel> goalsModels) {

        mutableLiveData.setValue(goalsModels);

    }
}
