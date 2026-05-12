package com.example.medievalapp;

import androidx.core.content.res.ResourcesCompat;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class StatsFragment extends Fragment {

    DatabaseHelper db;
    String currentUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        db = new DatabaseHelper(getContext());
        
        // KULLANICI ADINI AL
        SharedPreferences prefsUser = getContext().getSharedPreferences("MedievalPrefs", Context.MODE_PRIVATE);
        currentUsername = prefsUser.getString("current_active_user", "guest");
        
        TextView tvTotal = view.findViewById(R.id.tvTotalCompleted);
        TextView tvStreak = view.findViewById(R.id.tvStreak);
        
        // Yeni UI Elemanları
        android.widget.ImageView imgFlame = view.findViewById(R.id.imgFlame);
        android.widget.ImageView imgMedal = view.findViewById(R.id.imgMedal);
        TextView tvMedalText = view.findViewById(R.id.tvMedalText);
        
        // Animation Targets
        View cardGoal = view.findViewById(R.id.cardGoal);
        View cardStreak = view.findViewById(R.id.cardStreak);
        LinearLayout layoutRecent = view.findViewById(R.id.layoutRecentQuests);
        
        // --- BAŞLANGIÇ ANİMASYONU ---
        prepareViewForAnimation(cardGoal, 100);
        prepareViewForAnimation(cardStreak, 100);
        prepareViewForAnimation(layoutRecent, 200); 
        
        animateViewEntry(cardGoal, 200);
        animateViewEntry(cardStreak, 400);
        animateViewEntry(layoutRecent, 600); 

        LinearLayout containerLastQuests = view.findViewById(R.id.containerLastQuests);

        // --- İSTATİSTİK HESAPLAMA ---
        int completedCount = db.getCompletedQuestCount(currentUsername);
        int journalDays = db.getJournalCount(currentUsername);

        // 1. Streak Logic
        int months = journalDays / 30;
        int days = journalDays % 30;

        StringBuilder streakText = new StringBuilder();
        if (months > 0) {
            streakText.append(months).append(" Ay ");
        }
        streakText.append(days).append(" Gün");

        tvStreak.setText("Günlük Serisi: " + streakText.toString());

        // Alev Mantığı Basitleştirildi
        imgFlame.setVisibility(View.GONE);
        if (months > 0 || days >= 20) {
            imgFlame.setImageResource(R.drawable.ic_flame_red);
            imgFlame.setVisibility(View.VISIBLE);
        } else if (days >= 7) {
            imgFlame.setImageResource(R.drawable.ic_flame_yellow);
            imgFlame.setVisibility(View.VISIBLE);
        }

        // 2. Hedef ve Madalya Mantığı
        tvTotal.setText("Tamamlanan Görevler: " + completedCount);
        
        TextView tvEmojiMedals = view.findViewById(R.id.tvEmojiMedals);
        int medalCount = completedCount / 10;
        StringBuilder medals = new StringBuilder();
        for (int i = 0; i < medalCount; i++) {
            medals.append("🥇 ");
        }
        tvEmojiMedals.setText(medals.toString());
        
        checkAndSetGoal(completedCount, imgMedal, tvMedalText);

        loadLast5Quests(containerLastQuests);

        layoutRecent.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.fade_in, 
                        android.R.anim.fade_out, 
                        android.R.anim.fade_in, 
                        android.R.anim.fade_out
                    )
                    .replace(R.id.fragment_container, new CompletedQuestsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void prepareViewForAnimation(View view, float translationY) {
        if (view != null) {
            view.setAlpha(0f);
            view.setTranslationY(translationY);
        }
    }

    private void animateViewEntry(View view, long delay) {
        if (view != null) {
            view.animate()
                    .alpha(1f)
                    .translationY(0)
                    .setDuration(600)
                    .setStartDelay(delay)
                    .setInterpolator(new android.view.animation.OvershootInterpolator())
                    .start();
        }
    }

    private void checkAndSetGoal(int currentCompleted, View medalIcon, View medalText) {
        SharedPreferences prefs = getContext().getSharedPreferences("MedievalPrefs", Context.MODE_PRIVATE);
        String targetKey = "monthly_quest_target_" + currentUsername;
        int target = prefs.getInt(targetKey, -1);

        if (target == -1) {
            showGoalDialog(prefs, targetKey);
        } else {
            if (currentCompleted >= target && target > 0) {
                medalIcon.setVisibility(View.VISIBLE);
                medalText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showGoalDialog(SharedPreferences prefs, String targetKey) {
        android.widget.EditText input = new android.widget.EditText(getContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("Örn: 10");

        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Yeni Bir Hedef Belirle!")
                .setMessage("Bu ay kaç görev tamamlamak istersin lordum?")
                .setView(input)
                .setPositiveButton("Kaydet", (dialog, which) -> {
                    String val = input.getText().toString();
                    if (!val.isEmpty()) {
                        int goal = Integer.parseInt(val);
                        prefs.edit().putInt(targetKey, goal).apply();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void loadLast5Quests(LinearLayout container) {
        if (getContext() == null || db == null) return;

        try (Cursor cursor = db.getLast5CompletedQuests(currentUsername)) {
            container.removeAllViews();

            if (cursor.getCount() == 0) {
                TextView emptyView = new TextView(getContext());
                emptyView.setText("- Henüz bir zafer kazanılmadı -");
                emptyView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                emptyView.setTextColor(getResources().getColor(R.color.ink_color));
                container.addView(emptyView);
            }

            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));

                TextView item = new TextView(getContext());
                item.setText("⚔ " + title);
                item.setTextColor(getResources().getColor(R.color.ink_color));
                item.setTextSize(18f);
                item.setPadding(0, 10, 0, 10);

                try {
                    item.setTypeface(ResourcesCompat.getFont(getContext(), R.font.marck_script));
                } catch (Exception ignored) {}

                container.addView(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}