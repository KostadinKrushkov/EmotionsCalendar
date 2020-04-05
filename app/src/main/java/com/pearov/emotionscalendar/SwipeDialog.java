package com.pearov.emotionscalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SwipeDialog extends AppCompatDialogFragment {

    private RelativeLayout swipeDirRelativeLayout;
    private TextView verticalTextView;
    private TextView horizontalTextView;
    private String swipeType = MainActivity.getSwipeDirection();

    private void fillBackGroundColors() {


        if (MainActivity.themeName.equals("Light")) {

            swipeDirRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorDeadMainLight));
            verticalTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            horizontalTextView.setTextColor(getResources().getColor(R.color.colorWhite));

            if (swipeType.equals("Horizontal")) {
                verticalTextView.setBackgroundColor(getResources().getColor(R.color.colorMainLight));
                verticalTextView.setBackgroundResource(0);
                horizontalTextView.setBackgroundColor(getResources().getColor(R.color.colorLightBackground));
                horizontalTextView.setBackground(getResources().getDrawable(R.drawable.current_day_border));
            } else if (swipeType.equals("Vertical")) {
                horizontalTextView.setBackgroundColor(getResources().getColor(R.color.colorMainLight));
                horizontalTextView.setBackgroundResource(0);
                verticalTextView.setBackgroundColor(getResources().getColor(R.color.colorLightBackground));
                verticalTextView.setBackground(getResources().getDrawable(R.drawable.current_day_border));
            }
        } else if (MainActivity.themeName.equals("Dark")) {

            swipeDirRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorDarkBackground));
            verticalTextView.setTextColor(getResources().getColor(R.color.colorBlack));
            horizontalTextView.setTextColor(getResources().getColor(R.color.colorBlack));

            if (swipeType.equals("Horizontal")) {
                horizontalTextView.setBackgroundColor(getResources().getColor(R.color.colorDeadMainDark));
                horizontalTextView.setBackground(getResources().getDrawable(R.drawable.current_day_border));
                verticalTextView.setBackgroundColor(getResources().getColor(R.color.colorMainDark));
                verticalTextView.setBackgroundResource(0);

            } else if (swipeType.equals("Vertical")) {
                verticalTextView.setBackgroundColor(getResources().getColor(R.color.colorDeadMainDark));
                verticalTextView.setBackground(getResources().getDrawable(R.drawable.current_day_border));
                horizontalTextView.setBackgroundColor(getResources().getColor(R.color.colorMainDark));
                horizontalTextView.setBackgroundResource(0);

            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = null;
        if (MainActivity.themeName.equals("Dark")) {
            builder = new AlertDialog.Builder(getActivity(), R.style.NumberPickerThemeDark);
        } else if (MainActivity.themeName.equals("Light")) {
            builder = new AlertDialog.Builder(getActivity(), R.style.NumberPickerThemeLight);
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_swipe, null);
        builder.setView(view).setTitle("Choose swipe type")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Stub
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.setSwipeDirection(swipeType);
                        Toast.makeText(getActivity(), "Swipe type set to: " + swipeType, Toast.LENGTH_SHORT).show();
                    }
                });

        swipeDirRelativeLayout = view.findViewById(R.id.swipeDirRelativeLayout);
        verticalTextView = view.findViewById(R.id.verticalTextView);
        horizontalTextView = view.findViewById(R.id.horizontalTextView);

        verticalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeType = "Vertical";
                fillBackGroundColors();
            }
        });

        horizontalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeType = "Horizontal";
                fillBackGroundColors();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
        float height = MainActivity.getScreenHeight();
        float width = MainActivity.getScreenWidth();
        final float scale = CalendarActivity.context.getResources().getDisplayMetrics().density;
        int pixels = (int) ((height/width) * scale);

        dialog.getWindow().setLayout(200 * pixels, 250 * pixels);

//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = 400;
//        lp.height = 600;
//        lp.x=-170;
//        lp.y=100;
//
//        dialog.getWindow().setAttributes(lp);

        fillBackGroundColors();
        return dialog;

    }
}
