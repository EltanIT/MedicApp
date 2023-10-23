package com.example.medicapp.main_screens.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.MainScreen;
import com.example.medicapp.main_screens.objects.Analysis;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;

public class DataAnalysisDialogFragment extends Fragment {

    private TextView name_analysis_data, description_analysis_data, description2_analysis_data, dayCount_data, material;
    private ImageButton close_data_view;
    private Button add_button;
    private CardView card_view;
    private ConstraintLayout close_data_view_box;
    private Analysis analysis;
    private boolean checkCart;
    interface AddAnalysisInCartInterface{
        void addAnalysis(SelectedAnalysis selectedAnalysis);
    }
    private AddAnalysisInCartInterface addAnalysisInCartInterface;
    public DataAnalysisDialogFragment(AddAnalysisInCartInterface addAnalysisInCartInterface){
        this.addAnalysisInCartInterface = addAnalysisInCartInterface;
    }
    public DataAnalysisDialogFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_analysis_dialog, container, false);
        elementIdentification(view);
        openAnimCard();

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            analysis = bundle.getParcelable("analysis");
            checkCart = bundle.getBoolean("checkCart");

            name_analysis_data.setText(analysis.getTitle());
            dayCount_data.setText(analysis.getDeadline());
            description_analysis_data.setText(analysis.getDescription());
            description2_analysis_data.setText(analysis.getProcessDescription());
            dayCount_data.setText(analysis.getDeadline());
            material.setText(analysis.getMaterial());

            add_button.setEnabled(checkCart);
            add_button.setText("Добавить за "+analysis.getPrice()+" ₽");

        }


        close_data_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAnimCard();
            }
        });

        close_data_view_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAnimCard();
            }
        });

        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedAnalysis selectedAnalysis = new SelectedAnalysis(analysis,true);
                addAnalysisInCartInterface.addAnalysis(selectedAnalysis);
                closeAnimCard();


            }
        });
        return view;
    }

    void elementIdentification(View view){
        name_analysis_data = view.findViewById(R.id.name_analysis_data);
        description_analysis_data = view.findViewById(R.id.description_analysis_data);
        description2_analysis_data = view.findViewById(R.id.description2_analysis_data);
        dayCount_data = view.findViewById(R.id.dayCount_data);
        material = view.findViewById(R.id.material);

        close_data_view = view.findViewById(R.id.close_data_view);
        close_data_view_box = view.findViewById(R.id.close_data_view_box);
        add_button = view.findViewById(R.id.add_button);
        card_view = view.findViewById(R.id.card_view);

    }

    void openAnimCard(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_mainscreen_open_datadialog);
        card_view.startAnimation(animation);

    }
    void closeAnimCard(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_mainscreen_close_datadialog);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        card_view.startAnimation(animation);

    }
}