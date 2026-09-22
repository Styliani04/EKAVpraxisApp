package com.example.ekavpraxis.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ekavpraxis.R;

import java.util.Arrays;
import java.util.List;

public class ContrastHelper {

    private static final String PREFS_NAME = "ekav_prefs";
    private static final String KEY_CONTRAST = "high_contrast";

    // IDs των κουμπιών που πρέπει να έχουν Κίτρινο φόντο και Μαύρα γράμματα
    private static final List<Integer> ACTION_BUTTON_IDS = Arrays.asList(
            R.id.btnNext, R.id.btnLogin, R.id.btnConfirm, R.id.btnStudent, R.id.btnAdmin,
            R.id.btnMap, R.id.btnUpload, R.id.btnSchedule, R.id.btnSaveCase,
            R.id.btnSubmitSchedule, R.id.btnFinalSubmit, R.id.btnViewCards,
            R.id.btnPostAmbulances, R.id.btnSelectFile, R.id.btnRescan, R.id.btnSubmitOcr
    );

    /**
     * Εναλλαγή του Contrast mode και επανεκκίνηση της Activity
     */
    public static void toggleContrast(Activity activity) {
        if (activity == null) return;
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean current = prefs.getBoolean(KEY_CONTRAST, false);
        prefs.edit().putBoolean(KEY_CONTRAST, !current).apply();
        
        // Επανεκκίνηση για καθολική εφαρμογή
        activity.recreate();
    }

    /**
     * Έλεγχος αν είναι ενεργοποιημένο το High Contrast
     */
    public static boolean isHighContrast(Context context) {
        if (context == null) return false;
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(KEY_CONTRAST, false);
    }

    /**
     * Εφαρμογή των χρωμάτων στην τρέχουσα Activity
     */
    public static void applyCurrentContrast(Activity activity) {
        if (activity == null) return;
        boolean isEnabled = isHighContrast(activity);
        
        // 1. Background
        View backgroundView = activity.findViewById(R.id.background);
        if (isEnabled) {
            if (backgroundView != null) backgroundView.setVisibility(View.GONE);
            activity.getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        } else {
            if (backgroundView != null) backgroundView.setVisibility(View.VISIBLE);
            activity.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        }

        // 2. Αναδρομική αλλαγή χρωμάτων σε όλα τα Views
        View root = activity.findViewById(android.R.id.content);
        if (root instanceof ViewGroup) {
            applyRecursive((ViewGroup) root, isEnabled);
        }
    }

    private static void applyRecursive(ViewGroup parent, boolean isEnabled) {
        if (parent == null) return;
        
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int id = child.getId();

            if (ACTION_BUTTON_IDS.contains(id)) {
                // ACTION BUTTONS: Κίτρινο φόντο, Μαύρο κείμενο
                if (isEnabled) {
                    child.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FF00")));
                    if (child instanceof ViewGroup) {
                        setTextColorRecursive((ViewGroup) child, Color.BLACK);
                    } else if (child instanceof TextView) {
                        ((TextView) child).setTextColor(Color.BLACK);
                    }
                } else {
                    // Επαναφορά στο αρχικό (null tint αφήνει το drawable ως έχει)
                    child.setBackgroundTintList(null);
                    if (child instanceof ViewGroup) {
                        setTextColorRecursive((ViewGroup) child, Color.WHITE);
                    } else if (child instanceof TextView) {
                        ((TextView) child).setTextColor(Color.WHITE);
                    }
                }
            } else if (child instanceof CheckBox || child instanceof RadioButton) {
                // ΦΟΡΜΕΣ (CheckBoxes/RadioButtons): Κίτρινο κείμενο και κίτρινο check
                if (isEnabled) {
                    ((TextView) child).setTextColor(Color.parseColor("#F2FF00"));
                    if (child instanceof CompoundButton) {
                        ((CompoundButton) child).setButtonTintList(ColorStateList.valueOf(Color.parseColor("#F2FF00")));
                    }
                } else {
                    ((TextView) child).setTextColor(Color.WHITE);
                    if (child instanceof CompoundButton) {
                        ((CompoundButton) child).setButtonTintList(null);
                    }
                }
            } else if (child instanceof TextView) {
                // ΛΟΙΠΑ ΚΕΙΜΕΝΑ (Labels κλπ)
                if (isEnabled) {
                    ((TextView) child).setTextColor(Color.parseColor("#F2FF00"));
                } else {
                    ((TextView) child).setTextColor(Color.WHITE);
                }
            } else if (child instanceof ViewGroup) {
                // Συνέχεια στα παιδιά αν δεν ήταν κάποιο από τα παραπάνω
                applyRecursive((ViewGroup) child, isEnabled);
            }
        }
    }

    private static void setTextColorRecursive(ViewGroup viewGroup, int color) {
        if (viewGroup == null) return;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            } else if (child instanceof ViewGroup) {
                setTextColorRecursive((ViewGroup) child, color);
            }
        }
    }
}
